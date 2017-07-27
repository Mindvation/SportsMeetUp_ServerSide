package com.newlife.meetup.service;

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
import com.newlife.meetup.util.ResponseUtil;

@Service
public class SendSmsServiceImpl implements ISendSmsService {

	private static final Logger LOG = LoggerFactory.getLogger(SendSmsServiceImpl.class);
	
	@Autowired
	private SmsConfig smsCofig;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ResponseUtil responseUtil;
	
	@Autowired
	private CheckCode checkCode;
	
	@Autowired
	private CheckCodeRepository checkCodeRepository;
	
	/**
	 * 用户获取验证码：
	 * 1. 校验手机号
	 * 2. 请求短信平台发送验证码给用户端
	 */
	@Override
	@Transactional
	public ResponseUtil getVerificationCode(String phoneNumber) {
		String isUsable = userService.checkPhoneNumber(phoneNumber);
		String result = "";
		checkCode = generateVerificationCode(phoneNumber);
		String verificationCode = checkCode.getCode();
		if(LOG.isDebugEnabled()) {
			LOG.debug("the generated verificatgionCode is {}", verificationCode);
		}
		if(isUsable.equals("Y")) {
			try {
				result = sendSms(phoneNumber, verificationCode).getCode();
				if(result.equals("OK")) {
					responseUtil.setResponseCode("000");
					responseUtil.setMessage("验证码已发送.");
					//更新checkCode数据
					updateCheckCode(phoneNumber, checkCode);
				}else {
					responseUtil.setResponseCode("SS001");
					responseUtil.setMessage("验证码发送失败, 请重试.");
				}
			} catch (ClientException e) {
				responseUtil.setResponseCode("SS001");
				responseUtil.setMessage("验证码发送失败, 请重试.");
				e.printStackTrace();
			}
		}
		if(isUsable.equals("N")) {
			responseUtil.setResponseCode("SE100");
			responseUtil.setMessage("账户已经存在.");
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
		Long expireAt = now + Long.valueOf(smsCofig.getTimeout_value());
		checkCode.setCode(verificationCode);
		checkCode.setCrateAt(new Timestamp(now));
		checkCode.setExpireAt(new Timestamp(expireAt));
		checkCode.setIsUsed(false);
		checkCode.setPhoneNumber(phoneNumber);
		checkCode.setCode(verificationCode);
		return checkCode;
	}
	
    public QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {
    	IAcsClient acsClient = initAcsClientAndSetTimeout();
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("13474118442");
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
    
    
    public IAcsClient initAcsClientAndSetTimeout() throws ClientException {
    	//可自助调整超时时间
        System.setProperty(smsCofig.getConnect_timeout_key(), smsCofig.getTimeout_value());
        System.setProperty(smsCofig.getRead_timeout_key(), smsCofig.getTimeout_value());

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(smsCofig.getRegionId(), smsCofig.getAccessKeyId(), smsCofig.getAccessKeySecret());
        DefaultProfile.addEndpoint(smsCofig.getEndpointName(), smsCofig.getRegionId(), smsCofig.getProduct(), smsCofig.getDomain());
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
			//初始化acsClient
			IAcsClient acsClient = initAcsClientAndSetTimeout();
			 //组装请求对象-具体描述见控制台-文档部分内容
	        SendSmsRequest request = new SendSmsRequest();
	        //必填:待发送手机号
	        request.setPhoneNumbers(phoneNumber);
	        //必填:短信签名-可在短信控制台中找到
	        request.setSignName(smsCofig.getSignName());
	        //必填:短信模板-可在短信控制台中找到
	        request.setTemplateCode(smsCofig.getTemplateCode());
	        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
	        String templateParam = "{\"code\":\""+verificationCode+"\"}";
	        request.setTemplateParam(templateParam);
	        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
	      
        return sendSmsResponse;
	}


}
