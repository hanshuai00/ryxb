package com.zj.ryxb.common.sms;

import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Pattern;
/**
 * 手机短信功能 常用设置
 * @author zxf
 *
 */
public class CheckBuilder {
	
	//网易云短信App key
	public static final String APP_KEY="b375e242da574df11a59147c253633cf";//"b375e242da574df11a59147c253633cf";
	public static final String APP_SECRET="12a14a7b6013";//"12a14a7b6013";
	public static final String NONCE="123456";//nonce随机数
	public static final String CUR_TIME= String.valueOf((new Date()).getTime() / 1000L);// time
	
	/**
	 * 用户注册验证码模板ID
	 */
	public static final String REGISTER_TEMPLATE="3054234";
	/**
	 * 密码找回验证码模板ID
	 */
	public static final String PASSWORD_TEMPLATE="3057274";
	/**
	 * 会员充值验证码模板ID
	 */
	public static final String RECHARGE_TEMPLATE="3049397";
	/**
	 * 奖品发货通知模板ID
	 */
	public static final String NOTICE_TEMPLATE="3054236";
	/**
	 * 中奖通知模板ID
	 */
	public static final String WIN_NOTICE_TEMPLATE="3055508";
	
	/**
	 * 判断是否手机号
	 * @param phone
	 * @return
	 */
	public static boolean isMobile(String phone){
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
	     //返回Boolean
	    return p.matcher(phone).matches();
	}
	
	 
	
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest= MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}