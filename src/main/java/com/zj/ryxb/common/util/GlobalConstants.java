package com.zj.ryxb.common.util;

import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 全局变量类
 * @ClassName: GlobalConstants 
 * @Author： zxf   
 * @Date： 2017年3月23日 下午11:28:01
 */
public class GlobalConstants {
	
	public static WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext(); 
	public static ServletContext context = webApplicationContext.getServletContext(); 

	/**
	 * 接口状态-失败
	 */
	//public static final String STATE_ERROR="0";
	/**
	 * 接口状态-成功
	 */
	//public static final String STATE_SUCCESS="1";
	/**
	 * 接口状态-缺少参数
	 */
	//public static final String STATE_NO_PARAM="2";
	/**
	 * 接口状态-无权限操作
	 */
	//public static final String STATE_NO_AUTHOR="-1";	
	
	/**
	 * 登陆用户token的key值前缀
	 */
	public static final String REDIS_CUS = "cus_";
	/**
	 * 接口状态说明信息——成功
	 */
	public static final String MSG_SUCCESS="查询成功";
	/**
	 * 接口状态说明信息——失败
	 */
	public static final String MSG_ERROR="查询失败";
	/**
	 * 接口状态说明信息——无参数
	 */
	public static final String MSG_NO_PARAM="缺少参数";
	/**
	 * 接口状态说明信息——无权限操作
	 */
	public static final String MSG_NO_AUTHOR="无权限操作";
	
	/**
	 * 接口状态说明信息——token无效
	 */
	public static final String MSG_TOKEN_ERROR ="登录错误，请检查确认重新登录";
	
	/**
	 * 接口状态说明信息——token失效
	 */
	public static final String MSG_TOKEN_INVALID ="登录失效，请重新登录";
	
	/**
	 * 接口状态说明信息——无效的商品ID
	 */
	public static final String MSG_NO_GOODSID ="无效的商品ID";
	
	/**
	 * 接口状态说明信息——保存成功
	 */
	public static final String MSG_SAVE_SUCCESS="保存成功";
	/**
	 * 接口状态说明信息——保存失败
	 */
	public static final String MSG_SAVE_FAILED="保存失败";
	
	/**
	 * 接口状态说明信息——删除成功
	 */
	public static final String MSG_DEL_SUCCESS="删除成功";
	/**
	 * 接口状态说明信息——删除失败
	 */
	public static final String MSG_DEL_FAILED="删除失败";
	
	
}
