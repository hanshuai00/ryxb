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
import com.zj.ryxb.model.BMail;
import com.zj.ryxb.service.BMailService;

@Controller
@RequestMapping("/")
public class BMailContoller extends BaseController {
	@Autowired
	private BMailService mailService;
	
	/**
	 * 查询站内信（手机端）
	 */
	@RequestMapping(value = "getMessageList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getMessageList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		
		String strToken = request.getParameter("token");
		String page = request.getParameter("pageNo");//当前页码数
		String size = request.getParameter("pageSize");//每页显示条数
		//String messageType = request.getParameter("messageType"); //消息类型
		
		
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
		
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);
		}
		if(StringUtils.isNotBlank(size)){
			pageSize =Integer.valueOf(size);
		}
		
		
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");
		
		map.put("customerId", Long.valueOf(customerId));
		map.put("messageType",4);//查询不等于4的
		
		int total = mailService.findTotalCount(map);
		List<BMail> list = mailService.queryListByPage(map);
		
		if(list!=null){
			/*if(list.size()>0){
				for(BMail record:list){
					if(record.getMessageType() !=null){
						record.messTypeName = CacheUtil.getValueByCode("",String.valueOf(record.getMessageType()));
					}else{
						record.messTypeName = "";
					}
				}
			}*/
			rMap.put("total", total);
			rMap.put("items", list);
			rMap.put("pageNo", pageNo);
			rMap.put("pageSize", pageSize);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);

		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(rMap);
	}
	

	/**
	 * 获取首页夺宝动态信息（手机端）
	 */
	@RequestMapping(value = "getWinNotice.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getWinNotice(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//去掉token验证   2017-05-15
		/*String strToken = request.getParameter("token");
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
		}*/
		//查询消息类型为：夺宝动态
		List<BMail> list = mailService.queryListByType(4,20);
		
		if(list!=null){
			rMap.put("total", list.size());
			rMap.put("items", list);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);

		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(rMap);
	}
}
