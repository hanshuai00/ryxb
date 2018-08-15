package com.zj.ryxb.common.sms;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 手机短信功能控制操作
 * @author zxf
 *
 */
@SuppressWarnings("deprecation")
public class SmsCodeUtil {
	
	/**
	 * 发送校验码
	 * @param phone 手机号
	 * @param templateId  短信模块id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String sendCode(String phone,String templateId) throws Exception{		
		String url = "https://api.netease.im/sms/sendcode.action";
		HttpPost httpPost = new HttpPost(url);
		
		String checkSum = CheckBuilder.getCheckSum(CheckBuilder.APP_SECRET, CheckBuilder.NONCE ,CheckBuilder.CUR_TIME);//参考计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", CheckBuilder.APP_KEY);
        httpPost.addHeader("Nonce", CheckBuilder.NONCE);
        httpPost.addHeader("CurTime", CheckBuilder.CUR_TIME);
        httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 设置请求的的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// 参数 jsonArray形式
		nvps.add(new BasicNameValuePair("mobile", phone));
		nvps.add(new BasicNameValuePair("templateid", templateId));//验证码模板ID

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 执行请求
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(httpPost);
		
		// 打印执行结果
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		System.out.println(result);
		
		return result;
	}
	/**
	 * 校验验证码
	 * @param phone 手机号
	 * @param code  验证码
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String verifyCode(String phone,String code)throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "https://api.netease.im/sms/verifycode.action";
		HttpPost httpPost = new HttpPost(url);
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
		nvps.add(new BasicNameValuePair("mobile", phone));
		//参数
		nvps.add(new BasicNameValuePair("code",code));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);

		// 打印执行结果
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		System.out.println(result);
		
		return result;

	}
	
	/**
	 * 发送发货物流通知短信
	 * @param templateId 短信模板ID
	 * @param phone 发送手机号码，多个用['18266625783','18266425783','18266525783']
	 * @param params 模块变量,多个用 ['"+param1+"','"+param2+"','"+param3+"','"+param4+"']
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	//public static String sendNoticeTemplate(String templateId,String phone,String params)throws Exception{	
	public static String sendNoticeTemplate(String templateId,String phone,String param1,String param2,String param3,String param4)throws Exception{		
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
		//nvps.add(new BasicNameValuePair("params", "['寻宝6673','苹果手机','顺丰快递','123799932']"));
		//nvps.add(new BasicNameValuePair("params", params));
		nvps.add(new BasicNameValuePair("params", "['"+param1+"','"+param2+"','"+param3+"','"+param4+"']"));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        
        // 执行请求
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
		
		// 打印执行结果
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		System.out.println(result);
		
		return result;
	}
	/**
	 * 发送中奖提醒短信
	 * @param templateId短信模板ID
	 * @param phone 发送手机号码，多个用['18266625783','18266425783','18266525783']
	 * @param param1
	 * @param param2
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String sendWinTemplate(String templateId,String phone,String param1,String param2)throws Exception{		
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
		nvps.add(new BasicNameValuePair("mobiles", "['"+phone+"']"));
		//模板参数  jsonArray形式
		nvps.add(new BasicNameValuePair("params", "['"+param1+"','"+param2+"']"));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        
        // 执行请求
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
		
		// 打印执行结果
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		System.out.println(result);
		
		return result;
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		/*String result =sendCode("18661696673",CheckBuilder.REGISTER_TEMPLATE);
		Gson gson = new Gson();
		SmsCodeVO codoVO = gson.fromJson(result,SmsCodeVO.class);
		
		System.out.println(codoVO.code);*/

	}
}
