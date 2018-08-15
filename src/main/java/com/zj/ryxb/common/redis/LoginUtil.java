package com.zj.ryxb.common.redis;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
/**
 * 登录通用功能类
 * @author whq-mac
 *
 */
public class LoginUtil {
	private static Log LOGGER = LogFactory.getLog(LoginUtil.class);
	
	/**
	 * 存放登录票据的Cookie变量名
	 */
	private static final String COOK_NAME_TICKET = "g_cookTicket";
	
	public static final String CACHE_USER_KEY = "user";
	public static final String CACHE_USER_FUNCTION_KEY = "userFunctions";
	
	/**
	 * 保存登录信息到Cookie或者Session中 
	 * @param request
	 * @param response
	 * @param ticket
	 */
	public static void saveLoginTicket(HttpServletRequest request,
			HttpServletResponse response, String ticket) {
		saveLoginTicket(request, response, ticket, null);
	}
	
	/**
	 * 保存登录信息到Cookie中 
	 * @param request
	 * @param response
	 * @param loginInfo
	 * @param maxAge 有效时间(秒数)
	 */
	@SuppressWarnings("unused")
	public static void saveLoginTicket(HttpServletRequest request,
			HttpServletResponse response, String ticket, Integer maxAge) {
		String ssoDomain = LoginUtil.getSsoDomainFromWholeDomain(request.getServerName());
		
		try{
			Cookie cookUserID = new Cookie(COOK_NAME_TICKET, ticket);
			//cookUserID.setDomain(".");
			cookUserID.setPath("/");
			if (maxAge == null) {
				maxAge = 8 * 60 * 60; //设置登录有效时间为8小时
			}
			cookUserID.setMaxAge(maxAge.intValue());
			response.addCookie(cookUserID);
			
		} catch(Exception ex){
			LOGGER.error("保存登录信息到Cookie中出错,错误信息: ", ex);
		}
	}
	
	/**
	 * 将Cookie中的登录信息清空
	 * @param request
	 * @param response
	 */
	public static void clearLoginInfo(HttpServletRequest request,
			HttpServletResponse response) {
		//String ssoDomain = LoginUtil.getSsoDomainFromWholeDomain(request.getServerName());
		
		try{
			Cookie cookUserName = new Cookie(COOK_NAME_TICKET, null);
			//cookUserName.setDomain(ssoDomain);
			cookUserName.setPath("/");
			response.addCookie(cookUserName);
		} catch(Exception ex) {
			LOGGER.error("清除Cookie中的登录信息出错,错误信息: ", ex);
		}
	}
	
	/**
	 * 判断是否登录
	 * @return
	 */
	public static boolean chkIsLogin(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		
		String ticket = LoginUtil.getCookieValue(request, COOK_NAME_TICKET);
		return StringUtils.isNotBlank(ticket);
	}
	
	/**
	 * 如果登录返回登录票据, 如果没有登录再返回null
	 * @return
	 */
	public static String getLoginTicket(HttpServletRequest request){
		if (!chkIsLogin(request)) {
			return null;
		}
		
		return LoginUtil.getCookieValue(request, COOK_NAME_TICKET);
	}

	
	/**
	 * 从一个完成的域名得到用来使用Cookie实现SSO登录的域名 比如 从"test.585m.com" 得到 ".585m.com"
	 * 
	 * @param wholeDomain
	 * @return
	 */
	public static String getSsoDomainFromWholeDomain(String wholeDomain) {
		String destDomain = "";
		if (wholeDomain != null) {
			String[] arrDomain = wholeDomain.split("\\.");
			if ((arrDomain != null) && (arrDomain.length >= 2)) {
				destDomain = "." + arrDomain[arrDomain.length - 2] 
				           + "." + arrDomain[arrDomain.length - 1];
			}
		}
		return destDomain;
	}

	/**
	 * 获取一个cookie变量值
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName())) {
				String tmp = cookie.getValue();
				try {
					tmp = URLDecoder.decode(tmp, "UTF-8");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return tmp;
			}
		}
		return null;
	}
	
	/**
	 * 设置缓存用户
	 * @param ticket
	 * @param userId
	 * @param sysUserStr
	 */
	public static void setCacheLoginUser(String ticket, String userId, String sysUserStr, String userFunctions){
		CommonJedisUtil.set(ticket, userId);
		CommonJedisUtil.expire(ticket, 8 * 60 * 60);
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put(CACHE_USER_KEY, sysUserStr);
		map.put(CACHE_USER_FUNCTION_KEY, userFunctions);
		CommonJedisUtil.set(userId, JSON.toJSONString(map));
	}
	
	/**
	 * 获取缓存用户
	 * @param ticket
	 * @param userId
	 * @param sysUserStr
	 */
	public static String getCacheLoginUser(String ticket){
		String uid = CommonJedisUtil.get(ticket);
		if(uid == null) return "";
		return CommonJedisUtil.get(uid);
	}
	
	/**
	 * 获取登录用户权限
	 * @param userKey
	 * @return
	 */
	public static String getLoginUserPermissions(String userKey){
		return CommonJedisUtil.get(userKey);
	}
}
