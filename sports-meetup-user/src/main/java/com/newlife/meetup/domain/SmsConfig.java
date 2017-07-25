package com.newlife.meetup.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
	
	private String signName;
	
	private String templateCode;
	
	private String accessKeyId;
	
	private String accessKeySecret;
	
	private String 	product;
	
	private String domain;
	
	private String regionId;
	
	private String endpointName;
	
	private String connect_timeout_key;
	
	private String read_timeout_key;
	
	private String timeout_value;
	
	private String soureces;
	
	private Integer size;

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public String getConnect_timeout_key() {
		return connect_timeout_key;
	}

	public void setConnect_timeout_key(String connect_timeout_key) {
		this.connect_timeout_key = connect_timeout_key;
	}

	public String getRead_timeout_key() {
		return read_timeout_key;
	}

	public void setRead_timeout_key(String read_timeout_key) {
		this.read_timeout_key = read_timeout_key;
	}

	public String getTimeout_value() {
		return timeout_value;
	}

	public void setTimeout_value(String timeout_value) {
		this.timeout_value = timeout_value;
	}

	public String getSoureces() {
		return soureces;
	}

	public void setSoureces(String soureces) {
		this.soureces = soureces;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
}
