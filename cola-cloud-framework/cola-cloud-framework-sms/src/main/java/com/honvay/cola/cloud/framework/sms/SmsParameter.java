package com.honvay.cola.cloud.framework.sms;

import java.util.List;

/**
 * 短信
 * @author LIQIU
 *
 */
public class SmsParameter {

	private String templateCode;
	private String signName;
	private List<String> phoneNumbers;
	private String params;
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
}
