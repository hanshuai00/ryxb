package com.zj.ryxb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 *  系统用户管理
 * @author zxf
 *
 */
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.sms.CheckBuilder;
import com.zj.ryxb.common.sms.SmsCodeUtil;
import com.zj.ryxb.common.sms.SmsCodeVO;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.MD5;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.model.BRechargeLog;
import com.zj.ryxb.model.CAmount;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.SUsers;
import com.zj.ryxb.service.DictionaryService;
import com.zj.ryxb.service.UserService;
@Controller
@RequestMapping("/")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private DictionaryService dictionaryService;
	

	/**
     * 转向系统用户管理列表页面
     */
    @RequestMapping(value = "pc/modifyPassword.act")
    public String userIndex(HttpServletRequest request, ModelMap model) {
        return "/modules/user/index";
    }
    
	/**
	 * 服务端获取所有系统用户
	 */
	@RequestMapping(value = "pc/getUserListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getUserListPC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		map.put("userName", "admin");//查询用户名为admin的用户
		
		List<SUsers> userList = userService.queryListByPage(map);
		// 总数据条数
		Integer totalCount = userService.findTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		model.put("resultList", userList);
		model.put("pageInfo", pageInfo);
		
		return "/modules/user/data";
	}
	/**
	 * 进密码重置页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/toModifyPassword.act")
    public String toModifyPassword(HttpServletRequest request, ModelMap model) {
		String userId = request.getParameter("objectId");//用户id
		
		model.put("userId", userId);
        return "/modules/user/modifyPassword";
    }
	/**
	 * 获取服务端重置密码时的——短信验证码
	 * @param domain
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/getPasswordSmsCode.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getPasswordSmsCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		//获取密码重置验证手机号码
		String phone = dictionaryService.getDicValueByCode("RECHARGE_MOBILE", "reMobile");
		if(StringUtils.isNotBlank(phone)){
			String result = SmsCodeUtil.sendCode(phone,CheckBuilder.PASSWORD_TEMPLATE);
			SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
			if(codeVO !=null &&"200".equals(codeVO.code)){
				returnMap.put("state", true);
				returnMap.put("msg", "验证码发送成功,请查看手机获取！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "验证码发送失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "获取验证手机失败！");
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 重置修改系统用户密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/saveModifyPasswordPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveModifyPasswordPC(HttpServletRequest request, HttpServletResponse response){
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String userId = request.getParameter("userId");
		String newPassword = request.getParameter("newPassword");
		String smsCode = request.getParameter("smsCode");
		
		if(StringUtils.isBlank(userId)){
			returnMap.put("state", false);
			returnMap.put("msg", "用户不能为空！");
            return gson.toJson(returnMap);
		}
		if(StringUtils.isBlank(newPassword)){
			returnMap.put("state", false);
			returnMap.put("msg", "新密码不能为空！");
            return gson.toJson(returnMap);
		}
		if(StringUtils.isBlank(smsCode)){
			returnMap.put("state", false);
			returnMap.put("msg", "重置密码短信验证码不能为空！");
            return gson.toJson(returnMap);
		}
		
		//校验短信验证码是否正确
		String result="";
		String phone = dictionaryService.getDicValueByCode("RECHARGE_MOBILE", "reMobile");
		if(StringUtils.isBlank(phone)){
			returnMap.put("state", false);
			returnMap.put("msg", "获取重置密码验证手机号错误！");
			return gson.toJson(returnMap);
		}
		try {
			result = SmsCodeUtil.verifyCode(phone, smsCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
		if(codeVO !=null && "200".equals(codeVO.code)){
			//重置密码
			SUsers user = new SUsers();
			user.setId(Long.valueOf(userId));
			user.setPassword(MD5.getMD5(newPassword));
			int m = userService.saveOrUpdate(user);
			if(m>0){
				returnMap.put("state", true);
				returnMap.put("msg", "重置密码成功！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "重置密码失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "重置密码短信验证码错误！");
		}
		
		return gson.toJson(returnMap);
	}
	
}
