package com.zj.ryxb.controller;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.zj.ryxb.common.redis.CommonJedisUtil;
import com.zj.ryxb.common.sms.CheckBuilder;
import com.zj.ryxb.common.sms.SmsCodeUtil;
import com.zj.ryxb.common.sms.SmsCodeVO;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.MD5;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.BBonus;
import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.model.BDistrict;
import com.zj.ryxb.model.BLoginLog;
import com.zj.ryxb.model.CAmount;
import com.zj.ryxb.model.CBonus;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.service.BonusService;
import com.zj.ryxb.service.CAmountService;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.DictionaryService;
import com.zj.ryxb.service.LoginLogService;

/**
 * 
 * @Description:用户登录控制类
 * @ClassName: LoginController 
 */
@Controller
@RequestMapping("/")
public class CustomerController extends BaseController{	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BonusService bonusService;
	@Autowired
	private LoginLogService loginLogService;
	
	@Autowired
	private CAmountService amountService;
	@Autowired
	private DictionaryService dictionaryService;
	
	/**
	 * 客户登录验证
	 */
	@RequestMapping(value = "customerLoginIn.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String customerLoginIn(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String version = request.getParameter("version");//用户登陆版本
		
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
		CCustomer cusDomain = customerService.customerLoginIn(map);
		
		//用户不存在
		if(cusDomain == null){
			returnMap.put("state", false);
			returnMap.put("msg", "用户名或密码不正确！");
		}else{	
			// 设置这个token的生命时间
	        Date expiry = KeyUtil.getExpiryDate(EXPIRY_DAY * 24 * 60);    // 365天的有效日期
	        Map<String,Object> claims = new HashMap<String,Object>();
	        claims.put("id", cusDomain.getId());
	        claims.put("name", userName);
	        claims.put("nickname",cusDomain.getNickname());
	        
	        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext(); 
	        ServletContext servletContext = webApplicationContext.getServletContext();  
	        //生成获取token
	        String userToken = TokenUtil.getJWTString(userName, expiry,KeyUtil.getKey(servletContext),
	        											cusDomain.getId().toString(),claims);

			returnMap.put("state", true);
			returnMap.put("token", userToken);
			returnMap.put("customerId", cusDomain.getId());
			returnMap.put("msg","登陆成功！");
			//传入参数version 大于等于数据库保存的版本时返回no，
			//传入version小于系统保存的版本时返回yes，当不传入version参数或者version为""值时返回yes
			if(StringUtils.isNotBlank(version)){
				String sysVersion = dictionaryService.getDicValueByCode("VERSION_INFO","iosVersion");
				if(Double.valueOf(version)>=Double.valueOf(sysVersion)){
					returnMap.put("isShow", "no");
				}else{
					returnMap.put("isShow", "yes");
				}
			}else{
				returnMap.put("isShow", "yes");
			}
			
			//记录登陆日志
			try{
				BLoginLog loginLog = new BLoginLog();
				loginLog.setUserId(cusDomain.getId());
				loginLog.setUserName(userName);
				loginLog.setUserType(2);
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
	 * 会员退出登陆
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "customerLoginOut.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String customerLoginOut(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String strToken = request.getParameter("token");
		
		Key key= KeyUtil.getKey(GlobalConstants.context);	
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}
		String username = TokenUtil.getName(strToken, key);
		
		//记录登陆日志
		try{
			//记录登陆日志
			BLoginLog loginLog = new BLoginLog();
			loginLog.setUserId(Long.valueOf(customerId));
			loginLog.setUserName(username);
			loginLog.setUserType(2);
			loginLog.setAction(2);
			String lastip = KeyUtil.getIpAddr( request);
			loginLog.setLastip(lastip);
			loginLog.setLastlogintime(CalendarUtil.getLongFormatDate());
			loginLogService.insert(loginLog);
		}catch (Exception e) {
			System.out.println("error!" + e.toString());
		}
		//删除token
		TokenUtil.delToken(GlobalConstants.REDIS_CUS+customerId);
		
		returnMap.put("state",true);
		returnMap.put("msg","退出成功！");	
		str = gson.toJson(returnMap);
		return str;
	}
	/**
	 * 获取短信验证码
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSmsCode.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getSmsCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String userName = request.getParameter("userName");//注册、密码找回的手机号
		String smsType = request.getParameter("smsType");//验证码类型 1：注册 2：密码找回
		if(StringUtils.isBlank(userName)){
			returnMap.put("state", false);
			returnMap.put("msg", "手机号码为空！");
			return gson.toJson(returnMap);
		}
		boolean  isMobile = CheckBuilder.isMobile(userName);
		if(isMobile){
			String result ="";
			if("1".equals(smsType)){ //1：注册
				result = SmsCodeUtil.sendCode(userName,CheckBuilder.REGISTER_TEMPLATE);
			}else if("2".equals(smsType)){//2：密码找回
				result = SmsCodeUtil.sendCode(userName,CheckBuilder.PASSWORD_TEMPLATE);
			}
			SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
			if(codeVO !=null &&"200".equals(codeVO.code)){
				returnMap.put("state", true);
				returnMap.put("msg", "验证码发送成功！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "验证码发送失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "手机格式错误！");
		}
		
		str = gson.toJson(returnMap);
		return str;
	}
	
	
	/**
	 * 客户注册
	 */
	@RequestMapping(value = "customerRegister.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@Transactional
	public String customerRegister(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		String trueName = request.getParameter("trueName");
		String userGender = request.getParameter("userGender");
		
		String smsCode = request.getParameter("smsCode");//短信验证码
		
		CCustomer cusDomain = new CCustomer();		
		if(StringUtils.isNotBlank(userName)){
			cusDomain.setUserName(userName);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "用户名不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		//判断当前用户名(手机)是否已注册
		CCustomer oldCustomer = customerService.findCustomerByName(userName);
		if(oldCustomer !=null){
			returnMap.put("state", false);
			returnMap.put("msg", "该用户名已经被注册！");
			str = gson.toJson(returnMap);
			return str;
		}
		
		if(StringUtils.isBlank(smsCode)){
			returnMap.put("state", false);
			returnMap.put("msg", "短信验证码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		
		if(StringUtils.isNotBlank(password)){
			cusDomain.setPassword(MD5.getMD5(password));
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "密码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		
		if(StringUtils.isNotBlank(nickname)){
			cusDomain.setNickname(nickname);
		}else{
			cusDomain.setNickname("寻宝"+StringUtils.right(userName, 4));//设置默认昵称
		}
		
		if(StringUtils.isNotBlank(trueName)){
			cusDomain.setTrueName(trueName);
		}
		
		if(StringUtils.isNotBlank(userGender)){
			cusDomain.setUserGender(Integer.valueOf(userGender));
		}
		cusDomain.setUserImage("default.png");//设置默认头像
		cusDomain.setEnabled(1);
		cusDomain.setCreateTime(CalendarUtil.getLongFormatDate());
		
		//校验短信验证码是否正确
		String result="";
		try {
			result = SmsCodeUtil.verifyCode(userName, smsCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
		if(codeVO !=null && "200".equals(codeVO.code)){
			int m = customerService.insertSelective(cusDomain);
			if(m == 1){ //注册成功，自动添加新手红包
				Map<String,Object> map = new HashMap<String,  Object>();
				map.put("bonusType", 1);//红包类型——1：新手红包
				map.put("enabled", 1);//0无效1有效
				List<BBonus> bBonusList = bonusService.queryBBonusList(map);
				if(bBonusList!=null && bBonusList.size()>0){
					for(int i=0;i<bBonusList.size();i++){//自增6个新手红包
						BBonus bBonus = bBonusList.get(i);
						CBonus cBonus =new CBonus();
						cBonus.setBonusId(bBonus.getId());
						cBonus.setCustomerId(cusDomain.getId());
						cBonus.setBonusName(bBonus.getBonusName());
						cBonus.setAmount(bBonus.getAmount());
						cBonus.setStatus(0);//0：未使用
						Date  expiryDate = CalendarUtil.getNextDate(new Date(), bBonus.getIndate());
						cBonus.setExpiryDate(CalendarUtil.transLongFormatDate(expiryDate));//截至日期=注册日期+红包有效天数
						int k = bonusService.saveOrUpdateCBonus(cBonus);
					}
				}
				
				returnMap.put("state", true);
				returnMap.put("msg", "注册成功！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "注册失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "短信验证码错误！");
		}

		str = gson.toJson(returnMap);
		return str;
	}
	
	/**
	 * 会员信息查询(包含最新红包个数、夺宝币金额)
	 */
	@RequestMapping(value = "findCustomerById.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String findCustomerById(HttpServletRequest request, 
								HttpServletResponse response, ModelMap model){
		String str = "";	
		Gson gson = new Gson();
		Map<String, Object> rMap = new HashMap<String , Object>();
		//获取token
		String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			rMap.put("state", false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(rMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(rMap);
		}

		CCustomer customer = customerService.findById(Long.valueOf(customerId));
		
		if(customer != null){
			//查询会员当前夺宝币金额
			CAmount  cAmount = amountService.getAmountByCustomerId(Long.valueOf(customerId));
			//查询会员当前红包个数
			Map<String,Object> map = new HashMap<String,  Object>();
			map.put("customerId", Long.valueOf(customerId));
			map.put("enabled", 1);
			map.put("status", 0);//0未使用1已使用2已失效
			int bonusCount  = bonusService.findCBonusTotalCount(map);
			
			
			rMap.put("state",true);
			if(cAmount !=null){
				rMap.put("cusAmount", cAmount.getRemainAmout());//客户当前夺宝币金额
			}else{
				rMap.put("cusAmount", 0);
			}
			
			customer.setNickname(customer.getNickname()==null?"":customer.getNickname());
			customer.setTrueName(customer.getTrueName()==null?"":customer.getTrueName());
			customer.setBirthday(customer.getBirthday()==null?"":customer.getBirthday());
			customer.setUserImage(customer.getUserImage()==null?"":customer.getUserImage());
			customer.setUserGender(customer.getUserGender()==null?1:customer.getUserGender());
			customer.levelName=dictionaryService.getDicValueByCode("USER_LEVEL",String.valueOf(customer.getUserLevel()));
			//获取apk下载地址
			String apkUrl = dictionaryService.getDicValueByCode("VERSION_INFO", "url");
			String iosUrl = dictionaryService.getDicValueByCode("VERSION_INFO", "iosUrl");
			
			rMap.put("apkUrl", apkUrl);
			rMap.put("iosUrl", iosUrl);
			rMap.put("bonusCount", String.valueOf(bonusCount));//会员当前红包个数
			rMap.put("userPath", USER_IMAGE_PATH);
			rMap.put("record",customer);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		str = gson.toJson(rMap);
		
		return str;
	}
	
	
	/**
	 * 客户信息修改
	 */
	@RequestMapping(value = "modifyCustomer.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@Transactional
	public String modifyCustomer(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//获取token
		String strToken = request.getParameter("token");
		String imageName = request.getParameter("imageName");//头像名称
		String nickname = request.getParameter("nickname");//昵称
		String birthday = request.getParameter("birthday");//生日
		String userGender = request.getParameter("userGender");//性别 1男0女
		
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}
		
		CCustomer cusDomain = new CCustomer();
		cusDomain.setId(Long.valueOf(customerId));
		if(StringUtils.isNotBlank(nickname)){
			cusDomain.setNickname(nickname);
		}
		
		if(StringUtils.isNotBlank(imageName)){
			cusDomain.setUserImage(imageName);
		}
		if(StringUtils.isNotBlank(birthday)){
			cusDomain.setBirthday(birthday);
		}
		
		if(StringUtils.isNotBlank(userGender)){
			cusDomain.setUserGender(Integer.valueOf(userGender));
		}
		
		int m = customerService.update(cusDomain);
		if(m>0){
			returnMap.put("state", true);
			returnMap.put("msg", "修改成功！");
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "修改失败！");
		}
		
		str = gson.toJson(returnMap);
		return str;
	}
	
	
	
	
	/**
	 * 忘记重置会员客户密码
	 */
	@RequestMapping(value = "resetCusPassword.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String resetCusPassword(HttpServletRequest request, HttpServletResponse response, 
			ModelMap model){
		String str = "";
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");	
		String smsCode = request.getParameter("smsCode");//短信验证码
		
		if(StringUtils.isBlank(userName)){
			returnMap.put("state", false);
			returnMap.put("msg", "用户名不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		if(StringUtils.isBlank(smsCode)){
			returnMap.put("state", false);
			returnMap.put("msg", "短信验证码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		if(StringUtils.isBlank(password)){
			returnMap.put("state", false);
			returnMap.put("msg", "新密码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		CCustomer oldCustomer = customerService.findCustomerByName(userName);
		if(oldCustomer ==null){
			returnMap.put("state", false);
			returnMap.put("msg", "该用户不存在！");
			str = gson.toJson(returnMap);
			return str;
		}
		String result="";
		try {
			result = SmsCodeUtil.verifyCode(userName, smsCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
		if(codeVO !=null &&"200".equals(codeVO.code)){
			CCustomer record = new CCustomer();
			record.setId(oldCustomer.getId());
			record.setPassword(MD5.getMD5(password));//设置新密码
			int i = customerService.update(record);
			if(i>0){
				returnMap.put("state", true);
				returnMap.put("msg", "重置密码成功！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "重置密码失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "短信验证码错误！");
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 会员登陆后——修改密码
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyCusPassword.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String modifyCusPassword(HttpServletRequest request, HttpServletResponse response, 
			ModelMap model){
		String str = "";
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String oldPassword = request.getParameter("oldPassword");//旧密码
		String newPassword = request.getParameter("newPassword");//新密码
		String newPassword2 = request.getParameter("newPassword2");//
		
		//获取token
		String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}
		
		if(StringUtils.isBlank(newPassword)){
			returnMap.put("state", false);
			returnMap.put("msg", "新密码不能为空！");
			str = gson.toJson(returnMap);
			return str;
		}
		if(!newPassword.equals(newPassword2)){
			returnMap.put("state", false);
			returnMap.put("msg", "两次输入的新密码不一致！");
			str = gson.toJson(returnMap);
			return str;
		}
		
		CCustomer oldCustomer = customerService.findById(Long.valueOf(customerId));
		if(oldCustomer ==null){
			returnMap.put("state", false);
			returnMap.put("msg", "该用户不存在！");
			str = gson.toJson(returnMap);
			return str;
		}
		if(oldPassword == null){
			oldPassword = ""; 
		}else{
			oldPassword = MD5.getMD5(oldPassword);//转换成MD5加密后进行密码校验
		}
		if(!oldPassword.equals(oldCustomer.getPassword())){
			returnMap.put("state", false);
			returnMap.put("msg", "旧密码错误！");
			str = gson.toJson(returnMap);
			return str;
		}
		

		CCustomer record = new CCustomer();
		record.setId(oldCustomer.getId());
		record.setPassword(MD5.getMD5(newPassword));//设置新密码
		int i = customerService.update(record);
		if(i>0){
			returnMap.put("state", true);
			returnMap.put("msg", "修改密码成功！");
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "修改密码失败！");
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 根据父类id获取地区列表
	 */
	@RequestMapping(value = "getDistrictByParentId.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getDistrictByParentId(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String parentId = request.getParameter("parentId");
		if(StringUtils.isBlank(parentId)){
			parentId = "0";
		}
		List<BDistrict> districtList = customerService.selectByPrimaryKey(Long.valueOf(parentId));
		
		if(districtList!=null){
			returnMap.put("state", true);
			returnMap.put("total", districtList.size());
			returnMap.put("items", districtList);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 获取客户服务 联系方式
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getContactMode.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String  getContactMode(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//去掉token验证   2017-05-15
		/*String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}*/
		List<BDictionary>  list = dictionaryService.findByParentCode("SERVICE_MODE");
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		
		if(list!=null){
			if(list.size()>0){
				Map<String,Object> map = new HashMap<String, Object>();
				for(BDictionary record:list){
					map.put(record.getDicCode(), record.getDicValue());
				}
				listMap.add(map);
			}
			returnMap.put("total", list.size());
			returnMap.put("items", listMap);
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 检查版本更新
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkAppVersion.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String  checkAppVersion(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Gson gson = new Gson();
		List<BDictionary> versionList = dictionaryService.findByParentCode("VERSION_INFO");
		Map<String, String> versionMap = versionList.stream().collect(Collectors.toMap(t -> t.getDicCode(),t -> t.getDicValue()));
		return gson.toJson(versionMap);
	}
	/**
	 * 获取系统引导页手机端
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getBootImage.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String  getBootImage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Gson gson = new Gson();
		List<BDictionary> bootList = dictionaryService.findByParentCode("BOOT_IMAGE");
		Map<String, String> bootMap = bootList.stream().collect(Collectors.toMap(t -> t.getDicCode(),t -> t.getDicValue()));
		bootMap.put("bootPath", BOOT_IMAGE_PATH);
		return gson.toJson(bootMap);
	}
	
	/**
	 * 跳转进入apk分享下载页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shareDownload.act")
    public String shareDownload(HttpServletRequest request, ModelMap model) {
		//获取apk下载地址
		String apkUrl = dictionaryService.getDicValueByCode("VERSION_INFO", "url");
		String iosUrl = dictionaryService.getDicValueByCode("VERSION_INFO", "iosUrl");
		model.put("apkUrl", apkUrl);
		model.put("iosUrl", iosUrl);
		
        return "download";
    }
	

	
	
	/**
     * 转向会员管理列表页面
     */
    @RequestMapping(value = "hygl.act")
    public String spflIndex(HttpServletRequest request, ModelMap model) {
        return "/modules/customer/index";
    }
    
	/**
	 * 服务端获取所有会员
	 */
	@RequestMapping(value = "pc/getCustomerListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getCatalogue(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		String userLevel = request.getParameter("userLevel");
		String userName = request.getParameter("userName");
		
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		if(StringUtils.isNotBlank(userLevel)){
			map.put("userLevel", userLevel);
		}
		if(StringUtils.isNotBlank(userName)){
			map.put("userName", "%"+userName+"%");
		}
		
		List<CCustomer> customerList = customerService.queryListByPage(map);
		// 总数据条数
		Integer totalCount = customerService.findTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		if(customerList !=null && customerList.size()>0){
			for(CCustomer record:customerList){
				//查询会员当前夺宝币金额
				CAmount  cAmount = amountService.getAmountByCustomerId(Long.valueOf(record.getId()));
				if(cAmount !=null){
					record.setCusAmount(cAmount.getRemainAmout());//客户当前夺宝币金额
				}else{
					record.setCusAmount(0.0);
				}
			}
		}
		model.put("resultList", customerList);
		model.put("pageInfo", pageInfo);
		
		return "/modules/customer/data";
	}
	/**
	 * 服务端删除会员
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/deleteCustomerPC.act",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String deleteCustomerPC(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String objectId = request.getParameter("objectId");//红包id
		if (StringUtils.isBlank(objectId)) {
			returnMap.put("state", false);
			returnMap.put("msg", "请选择至少一条记录！");
            return gson.toJson(returnMap);
		}
		
		int m = this.customerService.deleteCustomerById(objectId);
		if (m > 0) {
			returnMap.put("state", true);
			returnMap.put("msg", "删除成功！");
        } else {
        	returnMap.put("state", false);
        	returnMap.put("msg", "删除失败！");
        }
        return gson.toJson(returnMap);
	}
}
