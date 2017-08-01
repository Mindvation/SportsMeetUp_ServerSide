package com.newlife.meetup.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.newlife.meetup.config.SmsConfig;
import com.newlife.meetup.domain.CheckCode;
import com.newlife.meetup.repository.CheckCodeRepository;
import com.newlife.meetup.service.ISendSmsService;
import com.newlife.meetup.service.IUserService;
import com.newlife.meetup.util.EncryptionUtil;
import com.newlife.meetup.util.ResponseUtil;

@Service
public class SendSmsServiceImpl implements ISendSmsService {

	private static final Logger LOG = LoggerFactory.getLogger(SendSmsServiceImpl.class);
	
	@Autowired
	private SmsConfig smsConfig;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	@Autowired
	private CheckCode checkCode;
	
	@Autowired
	private CheckCodeRepository checkCodeRepository;
	
	
	/**
	 * 修改/忘记密码获取验证码 0 注册 1 修改/忘记密码
   	 *4.1 校验用户是否已注册
   	 *4.2 发送验证码
     *4.2.1 生成验证码
     *4.2.2 更新验证码
     *4.2.3 发送验证码给平台
	 * @param phoneNumber
	 * @param flag
	 * @return
	 * @throws ClientException
	 */
	@Override
	public ResponseUtil getVerificationCode(String phoneNumber, int flag) throws ClientException {
//		1. 校验用户是否存在 checkPhoneNumber(phoneNumber) "Y" 用户存在 
		String isUsed = userService.checkPhoneNumber(phoneNumber);
		QuerySendDetailsResponse querySendDetailsResponse = null;
		SendSmsResponse response = null;
		if(0==flag) {
			if(isUsed.equals("Y")) {
				responseUtil.setResponseCode("USER_ERROR_503");
				responseUtil.setMessage("用户已存在.");
				return responseUtil;
			}
			String result = "";
			checkCode = generateVerificationCode(phoneNumber);
			String verificationCode = checkCode.getCode();
			if(LOG.isDebugEnabled()) {
				LOG.debug("the generated verificatgionCode is {}", verificationCode);
			}
			if(isUsed.equals("Y")) {
				responseUtil.setResponseCode("USER_ERROR_503");
				responseUtil.setMessage("用户已存在.");
			}
			if(isUsed.equals("N")) {
				try {
					response = sendSms(phoneNumber, verificationCode);
					if("OK".equals(response.getCode())) {
						responseUtil.setResponseCode("000");
						responseUtil.setMessage("验证码已发送.");
						//更新checkCode数据
						updateCheckCode(phoneNumber, checkCode);
					}else {
						LOG.error("验证码发送失败.");
						responseUtil.setResponseCode("USER_ERROR_504");
						responseUtil.setMessage("验证码发送失败,请重新获取.");
					}
					//查明细
					getDetail(response, phoneNumber);
				} catch (ClientException e) {
					responseUtil.setResponseCode("USER_ERROR_504");
					responseUtil.setMessage("验证码发送失败,请重新获取.");
					e.printStackTrace();
				}
			}
			
			
			return responseUtil;
		}
		
		if(1==flag) {
			if(isUsed.equals("N")) {
				LOG.error("用户不存在,请注册."); 
				responseUtil.setResponseCode("USER_ERROR_501");
				responseUtil.setMessage("用户不存在,请注册.");
				return responseUtil;
			}
//			2.1 生成验证码
			checkCode = generateVerificationCode(phoneNumber);
			String verificationCode = checkCode.getCode();
			if(LOG.isDebugEnabled()) {
				LOG.debug("the generated verificatgionCode is {}", verificationCode);
			}
//			2.2 更新验证码
			updateCheckCode(phoneNumber, checkCode);
//			2.3 发送验证码给平台
			String result = sendSms(phoneNumber, verificationCode).getCode();
			if(result.equals("OK")) {
				responseUtil.setResponseCode("000");
				responseUtil.setMessage("请求成功!");
			}else {
				responseUtil.setResponseCode("USER_ERROR_504");
				responseUtil.setMessage("验证码发送失败,请重新获取.");
			}
			//查明细
			getDetail(response, phoneNumber);
		}
			
		return responseUtil;
	}
	
//	获取明细
	public void getDetail(SendSmsResponse sendSmsResponse, String phoneNumber ) throws ClientException {
		QuerySendDetailsResponse querySendDetailsResponse = null;
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            querySendDetailsResponse = querySendDetails(sendSmsResponse.getBizId(), phoneNumber);
            if(LOG.isDebugEnabled()) {
				LOG.debug("短信明细查询返回数据----------------");
				LOG.debug("Code is {}, Message is {}", querySendDetailsResponse.getCode(),
						querySendDetailsResponse.getMessage());
				int i = 0;
				for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse
						.getSmsSendDetailDTOs()) {
					LOG.debug("SmsSendDetailDTO[" + i + "]:");
					LOG.debug("Content=" + smsSendDetailDTO.getContent());
					LOG.debug("ErrCode=" + smsSendDetailDTO.getErrCode());
					LOG.debug("OutId=" + smsSendDetailDTO.getOutId());
					LOG.debug("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
					LOG.debug("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
					LOG.debug("SendDate=" + smsSendDetailDTO.getSendDate());
					LOG.debug("SendStatus=" + smsSendDetailDTO.getSendStatus());
					LOG.debug("Template=" + smsSendDetailDTO.getTemplateCode());
				}
				LOG.debug("TotalCount=" + querySendDetailsResponse.getTotalCount());
				LOG.debug("RequestId=" + querySendDetailsResponse.getRequestId());
			}
        }
	}
	
	/**
	 * 用户获取验证码：
	 * 1. 校验手机号
	 * 2. 请求短信平台发送验证码给用户端
	 */
	@Override
	@Transactional
	public ResponseUtil getVerificationCode(String phoneNumber) {
		String isUsed = userService.checkPhoneNumber(phoneNumber);
		if(isUsed.equals("Y")) {
			responseUtil.setResponseCode("USER_ERROR_503");
			responseUtil.setMessage("用户已存在.");
		}
		String result = "";
		checkCode = generateVerificationCode(phoneNumber);
		String verificationCode = checkCode.getCode();
		if(LOG.isDebugEnabled()) {
			LOG.debug("the generated verificatgionCode is {}", verificationCode);
		}
		if(isUsed.equals("Y")) {
			responseUtil.setResponseCode("USER_ERROR_503");
			responseUtil.setMessage("用户已存在.");
		}
		if(isUsed.equals("N")) {
			try {
				result = sendSms(phoneNumber, verificationCode).getCode();
				if(result.equals("OK")) {
					responseUtil.setResponseCode("000");
					responseUtil.setMessage("验证码已发送.");
					//更新checkCode数据
					updateCheckCode(phoneNumber, checkCode);
				}else {
					responseUtil.setResponseCode("USER_ERROR_504");
					responseUtil.setMessage("验证码发送失败,请重新获取.");
				}
			} catch (ClientException e) {
				responseUtil.setResponseCode("USER_ERROR_504");
				responseUtil.setMessage("验证码发送失败,请重新获取.");
				e.printStackTrace();
			}
		}
	
		
		return responseUtil;
	}
	
	//更新验证码数据库
	public void updateCheckCode(String phoneNumber, CheckCode chekCode){
		checkCode = checkCodeRepository.findOne(phoneNumber);
		if(checkCode==null) {
			checkCodeRepository.save(chekCode);
		}else {
//			checkCodeRepository.updateCheckCode();
			checkCodeRepository.saveAndFlush(chekCode);
		}
	}
	public CheckCode generateVerificationCode(String phoneNumber) {
		String verificationCode = Integer.toString((int)((Math.random()*9+1)*100000));
		Long now = System.currentTimeMillis();
		Long expireAt = now + Long.valueOf(smsConfig.getTimeout_value());
		verificationCode = EncryptionUtil.getEncryptString(verificationCode);
		checkCode.setCode(verificationCode);
		checkCode.setCrateAt(new Timestamp(now));
		checkCode.setExpireAt(new Timestamp(expireAt));
		checkCode.setIsUsed(false);
		checkCode.setPhoneNumber(phoneNumber);
		checkCode.setCode(verificationCode);
		return checkCode;
	}
	  
    public IAcsClient initAcsClientAndSetTimeout() throws ClientException {
    	//可自助调整超时时间
        System.setProperty(smsConfig.getConnect_timeout_key(), smsConfig.getTimeout_value());
        System.setProperty(smsConfig.getRead_timeout_key(), smsConfig.getTimeout_value());

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(smsConfig.getRegionId(), smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
        DefaultProfile.addEndpoint(smsConfig.getEndpointName(), smsConfig.getRegionId(), smsConfig.getProduct(), smsConfig.getDomain());
        return new DefaultAcsClient(profile);
    	
    }
    
    /**
     * 2 发送验证码
	2.1 生成验证码
	2.2 保存验证码(phoneNumber)
     * @param phoneNumber
     * @return
     * @throws ClientException
     */
	public SendSmsResponse sendSms(String phoneNumber, String verificationCode) throws ClientException {
        //初始化acsClient,暂不支持region化
		IAcsClient acsClient = initAcsClientAndSetTimeout();
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsConfig.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsConfig.getTemplateCode());
        verificationCode = EncryptionUtil.getDecryptString(verificationCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        String templateParam = "{\"code\":\""+verificationCode+"\"}";
        request.setTemplateParam(templateParam);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(smsConfig.getBizId());
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
        	  sendSmsResponse = acsClient.getAcsResponse(request);
        }catch (Exception e) {
			LOG.error("获取 SendSmsResponse 发生异常：{}", e.getCause());
		}
      
        return sendSmsResponse;
	}

	
    public QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber) throws ClientException {
    	IAcsClient acsClient = initAcsClientAndSetTimeout();
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }
    


}
