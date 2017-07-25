package com.newlife.meetup.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newlife.meetup.domain.SmsConfig;


@Component
public class VCodeUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(VCodeUtil.class);
	private static final String VERIFY_CODES = "0123456789";
	
	@Autowired
	private static SmsConfig smsCofig;

	/**
     * 使用指定源生成验证码
     * @param verifySize    验证码长度
     * @param sources   验证码字符源
     * @return
     */
    public static String generateVerifyCode(int verifySize, String sources){
        if(sources == null || sources.length() == 0){
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for(int i = 0; i < verifySize; i++){
        	char ch = sources.charAt(rand.nextInt(codesLen-1));
        	if(i>0&&ch==verifyCode.charAt(i-1)) {
        		continue;
        	}
            verifyCode.append(ch);
        }
        return verifyCode.toString();
    }
}
