package com.zj.ryxb.common.sms;

import java.io.Serializable;

/**
 * 网易云短信请求返回对象
 * @author zxf
 *
 */
@SuppressWarnings("serial")
public class SmsCodeVO implements Serializable {
	/**
	 * 
	 */
	public String code;
	
	public String msg;
	
	public String obj;
	
}
