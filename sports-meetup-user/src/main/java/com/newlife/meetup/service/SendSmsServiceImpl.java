package com.newlife.meetup.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.newlife.meetup.domain.SmsConfig;
import com.newlife.meetup.util.ResponseUtil;
import com.newlife.meetup.util.VCodeUtil;

@Service
public class SendSmsServiceImpl implements ISendSmsService {

	private static final Logger LOG = LoggerFactory.getLogger(SendSmsServiceImpl.class);
	
	@Autowired
	private SmsConfig smsCofig;
	
	@Autowired
	private IUserService userService;
	/*
	@Value(value="${sms.signName}")
    private String signName;
    
	@Value(value="${sms.templateCode}")
    private String templateCode;
	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	@Value(value="${sms.accessKeyId}")
	private String accessKeyId;
		
    @Value(value="${sms.accessKeySecret}")
	private String accessKeySecret;
	
    //产品名称:云通信短信API产品,开发者无需替换
	@Value(value="${sms.default_profile.product}")
    private String product;
    //产品域名,开发者无需替换
	@Value(value="${sms.default_profile.domain}")
    private String domain;

	@Value(value="${sms.default_profile.regionId}")
	private String regionId;
	
	@Value(value="${sms.default_profile.endpointName}")
	private String endpointName;
   
	@Value(value="${sms.system.connect_timeout_key}")
	private String connect_timeout_key;
	
	@Value(value="${sms.system.read_timeout_key}")
	private String read_timeout_key;
	
	@Value(value="${sms.system.timeout_value}")
	private String timeout_value;
	*/
	@Autowired
	private ResponseUtil responseUtil;
	
	@Autowired
	private VCodeUtil vCodeUtil;

	/**
	 * 用户获取验证码：
	 * 1. 校验手机号
	 * 2. 请求短信平台发送验证码给用户端
	 */
	@Override
	public ResponseUtil getVerificationCode(String phoneNumber) {
		String isUsable = userService.checkPhoneNumber(phoneNumber);
		String result = "";
		if(isUsable.equals("Y")) {
			try {
				result = sendSms(phoneNumber);
				if(result.equals("OK")) {
					responseUtil.setResponseCode("SS100");
					responseUtil.setMessage("验证码已发送.");
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
    /*public SendSmsResponse sendSms() throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers("13474118442");
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("觉醒网络");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_78715030");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"1234\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("v000");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }*/
    
    /**
     * 
     */
    public SendSmsRequest getSendSmsRequest(String phoneNumber) {
    	 //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsCofig.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsCofig.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        String verificationCode = vCodeUtil.generateVerifyCode(smsCofig.getSize(), smsCofig.getSoureces());
        String templateParam = "{\"code\":\""+verificationCode+"\"}";
        request.setTemplateParam(templateParam);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("v000");
        return request;
    }
    
	public String sendSms(String phoneNumber) throws ClientException {
			//初始化acsClient
			IAcsClient acsClient = initAcsClientAndSetTimeout();
			//组装请求对象
			SendSmsRequest request = getSendSmsRequest(phoneNumber);
	        //hint 此处可能会抛出异常，注意catch
	        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
	
			 //发短信
	        System.out.println("短信接口返回的数据----------------");
	        System.out.println("Code=" + sendSmsResponse.getCode());
	        System.out.println("Message=" + sendSmsResponse.getMessage());
	        System.out.println("RequestId=" + sendSmsResponse.getRequestId());
	        System.out.println("BizId=" + sendSmsResponse.getBizId());
		
        try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //查明细
        /*if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(sendSmsResponse.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }*/
        return sendSmsResponse.getCode();
	}


}
