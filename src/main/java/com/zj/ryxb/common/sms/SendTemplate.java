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
 * 发送模板短信
 * @author netease
 *
 */
@SuppressWarnings("deprecation")
public class SendTemplate {
	
	/*public static void main(String[] args) throws Exception {
			@SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
	        String url = "https://api.netease.im/sms/sendtemplate.action";
	        HttpPost httpPost = new HttpPost(url);
	        
	        String appKey = "";
	        String appSecret = "";
	        String nonce =  "";//nonce随机数
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);//time
	        String checkSum = CheckBuilder.getCheckSum(CheckBuilder.APP_SECRET, CheckBuilder.NONCE ,CheckBuilder.CUR_TIME);//参考计算CheckSum的java代码

	        // 设置请求的header
	        httpPost.addHeader("AppKey", CheckBuilder.APP_KEY);
	        httpPost.addHeader("Nonce", CheckBuilder.NONCE);
	        httpPost.addHeader("CurTime", CheckBuilder.CUR_TIME);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        
	        //设置请求的的参数
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        //模板id			
			nvps.add(new BasicNameValuePair("templateid", "3057259"));
			//接收手机号      参数jsonArray形式
			nvps.add(new BasicNameValuePair("mobiles", "['18266625783']"));
			//模板参数  jsonArray形式
			nvps.add(new BasicNameValuePair("params", "['苹果手机','顺丰快递','123799932']"));
	        
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
	        
	        // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);
	        
	        // 打印执行结果
	        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	       
	}*/
	public static void main(String[] args) throws Exception {
		String params="['6673','苹果手机','顺丰快递','123799932']";
		//sendNoticeTemplate("3054236","18661696673",params);
		SmsCodeUtil.sendWinTemplate(CheckBuilder.WIN_NOTICE_TEMPLATE, "18661696673", "寻宝6673", "笔记本电脑");
	}
	
	@SuppressWarnings("resource")
	public static String sendNoticeTemplate(String templateId,String phone,String params)throws Exception{		
		String url = "https://api.netease.im/sms/sendtemplate.action";
        HttpPost httpPost = new HttpPost(url);
        String checkSum = CheckBuilder.getCheckSum(CheckBuilder.APP_SECRET, CheckBuilder.NONCE ,CheckBuilder.CUR_TIME);//参考计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", CheckBuilder.APP_KEY);
        httpPost.addHeader("Nonce", CheckBuilder.NONCE);
        httpPost.addHeader("CurTime", CheckBuilder.CUR_TIME);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        //设置请求的的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        //模板id			
		nvps.add(new BasicNameValuePair("templateid",templateId));
		//接收手机号      参数jsonArray形式
		//nvps.add(new BasicNameValuePair("mobiles", "['18266625783']"));
		nvps.add(new BasicNameValuePair("mobiles", "['"+phone+"']"));
		//模板参数  jsonArray形式
		//nvps.add(new BasicNameValuePair("params", "['苹果手机','顺丰快递','123799932']"));
		 nvps.add(new BasicNameValuePair("params", params));
		//nvps.add(new BasicNameValuePair("params", "['"+param1+"','"+param2+"','"+param3+"','"+param4+"']"));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        
        // 执行请求
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
		
		// 打印执行结果
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		System.out.println(result);
		
		return result;
	}
}

