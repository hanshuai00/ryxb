package com.zj.ryxb.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.MD5;
import com.zj.ryxb.model.BLoginLog;
import com.zj.ryxb.model.SResources;
import com.zj.ryxb.model.SUsers;
import com.zj.ryxb.service.LoginLogService;
import com.zj.ryxb.service.UserService;

/**
 * 
 * @Description:用户登录控制类
 * @ClassName: LoginController 
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private LoginLogService loginLogService;
		
	/**
	 * 管理员登录验证
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "loginIn.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getFunctionListByUserId(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(StringUtils.isBlank(userName)){
			returnMap.put("state", false);
			returnMap.put("msg", "用户名不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}		
		if(StringUtils.isBlank(password)){
			returnMap.put("state", false);
			returnMap.put("msg", "密码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userName", userName);
		map.put("password", MD5.getMD5(password));//转换成MD5加密后进行密码校验

		//验证是否存在当前帐号和密码
		SUsers userDomain = userService.loginIn(map);
		
		//用户不存在
		if(userDomain == null){
			returnMap.put("state", false);
			returnMap.put("msg", "用户名或密码不正确！");
		}else{
			request.getSession().setAttribute("userName", userName);
			request.getSession().setAttribute("userId", userDomain.getId());
			request.getSession().setAttribute("loginTime", CalendarUtil.getLongFormatDate());			
			returnMap.put("state", true);
			returnMap.put("msg", "index.act");
			
			//记录登陆日志
			try{
				BLoginLog loginLog = new BLoginLog();
				loginLog.setUserId(userDomain.getId());
				loginLog.setUserName(userName);
				loginLog.setUserType(1);
				loginLog.setAction(1);
				String lastip = KeyUtil.getIpAddr( request);
				loginLog.setLastip(lastip);
				loginLog.setLastlogintime(CalendarUtil.getLongFormatDate());
				loginLogService.insert(loginLog);
			}catch (Exception e) {
				System.out.println("error!" + e.toString());
			}
			
		}
		str = gson.toJson(returnMap);
		return str;
	}
	
	
	/**
	 * 管理员登录获取主目录信息
	 * @param request
	 * @param response
	 * 2015-7-10下午2:03:08
	 * @author han
	 * @version 1.0
	 */
	@RequestMapping(value = "getMenu.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		HttpSession session = request.getSession();
		if(session == null || session.getAttribute("userId") ==null){
			return str;
		}
		long id = (long) session.getAttribute("userId");
		Map<String,List<SResources>> menulist = userService.getMenu(id);

		returnMap.put("menulist", menulist);
		str = gson.toJson(returnMap);
		return str;
	}


	/**
     * 登陆成功——转向主页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "index.act")
    public String index(HttpServletRequest request, ModelMap model) {
        return "home";
    }

	
	/**
	 * 退出登陆——转向登录页
	 */
	@RequestMapping(value = "loginOut.act")
	public String loginOut(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		//记录登陆日志
		try{
			if(session != null && session.getAttribute("userId") != null){
				long id = (long) session.getAttribute("userId");
				String username = (String) session.getAttribute("userName");
				//记录登陆日志
				BLoginLog loginLog = new BLoginLog();
				loginLog.setUserId(id);
				loginLog.setUserName(username);
				loginLog.setUserType(1);
				loginLog.setAction(2);
				String lastip = KeyUtil.getIpAddr( request);
				loginLog.setLastip(lastip);
				loginLog.setLastlogintime(CalendarUtil.getLongFormatDate());
				loginLogService.insert(loginLog);
				
			}	
		}catch (Exception e) {
			System.out.println("error!" + e.toString());
		}
		request.getSession().invalidate();	
		return "/login";
	}
}
