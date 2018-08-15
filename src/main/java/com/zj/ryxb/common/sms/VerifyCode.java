package com.zj.ryxb.common.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 校验验证码
 * @author netease
 *
 */
@SuppressWarnings("deprecation")
public class VerifyCode {
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "https://api.netease.im/sms/verifycode.action";
		HttpPost httpPost = new HttpPost(url);

		String appKey = "";
		String appSecret = "";
		String nonce = "";// nonce随机数
		String curTime = String.valueOf((new Date()).getTime() / 1000L);// time
		String checkSum = CheckBuilder.getCheckSum(CheckBuilder.APP_SECRET, CheckBuilder.NONCE ,CheckBuilder.CUR_TIME);//参考计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", CheckBuilder.APP_KEY);
        httpPost.addHeader("Nonce", CheckBuilder.NONCE);
        httpPost.addHeader("CurTime", CheckBuilder.CUR_TIME);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 设置请求的的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//参数 jsonArray形式
		nvps.add(new BasicNameValuePair("mobile", "18661696673"));
		//参数
		nvps.add(new BasicNameValuePair("code", "4172"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);

		// 打印执行结果
		System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));

	}
}
