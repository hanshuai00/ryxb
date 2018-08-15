package com.zj.ryxb.common.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：1.0
 *日期：2016-06-06
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	//合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
	public static String partner = "2088621698314215";
	
	public static String app_id ="2017033006481065";
	
	//请求支付宝网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
		
	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALvhm+Ajf13V0YH0sd62x5HHWLj8wgbJyx6Y4gefY8WA8G+p7NdWhX2Wo6Z9/GIV2Wi386WiLM5ozzrupYhJ+rw7ZVvg1F6Zh6kAqeUhDXgfJlcGotebjk/LR6EuPcwapbieQNQ7HxN9JYp/Viy2dA/oUBXAqma3filXbQEkTr5LAgMBAAECgYBCAJTQ3ceNwGK0Y7Dky5M6bCyH +At1dQiNPCoTgXP8WzqcD7brQzLsFrJw98tk7cYNmqnWYy2YaYt7aYDvawXQ5iv4VA4jdozqm4cOCoQ4tUkVbJIFN00wGwk9zgV9E/Y9AFuSJcuqXMVnmesP/3cy1J4uNwnbZYn8NWvIGbphWQJBAOmHiRlGQe8TR9/v6iUNDXBpllhm7hxYubrvjhDghofPmMcwT59lq8+eU18ddsrRdhymylaqRZLwSi5iQHN3r0cCQQDN9aKnozMV+WML6jWtSPqYH3khtAOihRABw0RCWAtqIAYUxTZeZGVkvmV3s295RVQilLQWv9gHZST8L9GWIyLdAkBueFkYha3HBJ3phzqCEGydErW2V+qnqikiDxRrU2sQb9nb0DS6OIRQEJtr0SpSw0dWQmhRGvcSjWzqCDxo4uMdAkAtgwQoayXUy0EZ1u15l1DY4MuRqQfMl/7LhOzGhFIxZ1YwrMuw3BqTwocFhn8xmyJWaenJxSblq3Ia0C5OMQF5AkAfC/sUr9wzWMYcE7q6Cg8AZie4/yoWd3iW1xR0KunT9d9yRO7iLtzs+xfVAhaPUYw6MLOvMDoSYebcBz8w5Dbl";
	
	//接口签名方式
	public static String sign_type = "RSA";
	
	//支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
	public static String alipay_public_key  ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhI0Q0ZD+SSMIVm2eFPiSys8E8MZiPjOum+uXNJTPKJs8OKdE2x7WHX+ZUM2H9nSkWNmDBQBM4dHhh3gNwEOePB7k/uq/GG6KZf5f7ehqT+ybkflQmq1hW54JFOFJ03tiBzczJ/WNeo6KpOYHI+Y04WQZfwbtwnFGJloSEShYAqQYaj7rPTljnqcSsVw8Bee5Auaznl4f/+KVk+4R5mpGOgwOwoY2nvpsddZ0OeTKxQnsf83DCNpm13qRSthFj4zWrlu2KbEXZojDKE87kuIxBq+24inNl+m0DOMoUtQZ2a816mrsoZjNAp8pkafCGCRmHjdQUueIdKdNZR4bF8FeXQIDAQAB";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path ="C://";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";

	// 接收通知的接口名
	public static String service = "mobile.securitypay.pay";
	
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://www.ryxb.info/notify_url.jsp";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://www.ryxb.info/return_url.jsp";

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}

