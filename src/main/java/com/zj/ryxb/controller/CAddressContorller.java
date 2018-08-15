package com.zj.ryxb.controller;

import java.security.Key;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.CAddress;
import com.zj.ryxb.service.CAddressService;

/**
 * 
 * @Description:用户地址控制类
 * @ClassName: CAddressContorller 
 */
@Controller
@RequestMapping("/")
public class CAddressContorller extends BaseController {
	
	@Autowired
	private CAddressService addressService;
	
	
	/**
	 * 会员客户收货地址维护
	 */
	@RequestMapping(value = "saveCAddress.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveCAddress(HttpServletRequest request, 
								HttpServletResponse response, ModelMap model){
		String str = "";	
		Gson gson = new Gson();
		Map<String, Object> rMap = new HashMap<String , Object>();
		//获取token
		String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		String addressID = request.getParameter("addressID");//收货地址id
		String receiverName = request.getParameter("receiverName");//收货人姓名
		String cellPhone = request.getParameter("cellPhone");//手机
		String streetInfo = request.getParameter("streetInfo");//街道详细信息
		String postalCode = request.getParameter("postalCode");//邮政编码
		
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
		
		if(StringUtils.isBlank(receiverName)){
			rMap.put("state",false);
			rMap.put("msg", "收货人姓名不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		if(StringUtils.isBlank(cellPhone)){
			rMap.put("state",false);
			rMap.put("msg", "收货人手机不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		//新增、修改会员收货地址
		CAddress  record = new CAddress();
		/*if(addressID != null && addressID !=""){
			record.setId(Long.valueOf(addressID));	
		}*/
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("customerId", customerId);
		List<CAddress> list = addressService.queryListByPage(map);
		if(list != null && list.size()>0){
			record.setId(list.get(0).getId());
		}
		record.setCustomerId(Long.valueOf(customerId));
		record.setReceiverName(receiverName);
		record.setCellPhone(cellPhone);
		record.setPostalCode(postalCode);
		record.setStreetInfo(streetInfo);
		record.setDefaultAddress(1);
		record.setEnabled(1);
		int m = addressService.saveOrUpdate(record);
		if(m>0){
			rMap.put("state",true);
			rMap.put("addressID", record.getId());//收货地址id
			rMap.put("msg", "收货地址保存成功！");
		}else{
			rMap.put("state",false);
			rMap.put("msg", "收货地址保存失败！");
		}
		str = gson.toJson(rMap);
		
		return str;
	}
	
	
	/**
	 * 查询会员客户收货地址维护
	 */
	@RequestMapping(value = "findCAddressByCusId.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String findCAddressByCusId(HttpServletRequest request, 
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

		Map<String, Object> map = new HashMap<String , Object>();
		map.put("customerId", customerId);
		List<CAddress> list = addressService.queryListByPage(map);
		
		if(list != null && list.size()>=0){
			rMap.put("state",true);
			rMap.put("total",list.size());
			if(list.size()>0){
				rMap.put("record",list.get(0));
			}
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		str = gson.toJson(rMap);
		
		return str;
	}
	
	

}
