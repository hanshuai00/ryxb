package com.zj.ryxb.controller;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.zj.ryxb.common.sms.CheckBuilder;
import com.zj.ryxb.common.sms.SmsCodeUtil;
import com.zj.ryxb.common.sms.SmsCodeVO;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TemplateEngine;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.dao.BSmsLogMapper;
import com.zj.ryxb.dao.MOrderDetailMapper;
import com.zj.ryxb.model.BSmsLog;
import com.zj.ryxb.model.CAddress;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.model.MOrderWin;
import com.zj.ryxb.service.BMailService;
import com.zj.ryxb.service.CAddressService;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.GoodsService;
import com.zj.ryxb.service.OrderWinService;

/**
 * 中奖控制类
 * @author zxf
 *
 */
@Controller
@RequestMapping("/")
public class OrderWinController extends BaseController {

	@Autowired
	private OrderWinService orderWinService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CAddressService addressService;
	
	@Autowired
	private MOrderDetailMapper orderDetailMapper;
	
	@Autowired
	private BSmsLogMapper smsLogMapper;
	@Autowired
	private  BMailService mailService;
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 商品往期揭晓、个人中奖记录、宝物榜（手机端）
	 */
	@RequestMapping(value = "getOrderWinList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getOrderWinList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();

		String strToken = request.getParameter("token");
		String searchType = request.getParameter("searchType");//查询类型，1商品的往期开奖记录 2会员客户的历史中奖记录 3宝物榜
		String page = request.getParameter("pageNo");//当前页码数
		String size = request.getParameter("pageSize");//每页显示条数
		String goodId = request.getParameter("goodId"); //商品id(searchType=1时，必填)
		
		String customerId ="0";
		if(StringUtils.isNotBlank(strToken)){ //判断token是否null或者""
			Key key= KeyUtil.getKey(GlobalConstants.context);
			if (!TokenUtil.isValid(strToken,key)){
				rMap.put("state", false);
				rMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
				return gson.toJson(rMap);
			}
			customerId = TokenUtil.getUserId(strToken,key);
			if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
				rMap.put("state",false);
				rMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
				return gson.toJson(rMap);
			}
		}
		pageNo=1;
		pageSize=10;
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);
		}
		if(StringUtils.isNotBlank(size)){
			pageSize =Integer.valueOf(size);
		}
		if(StringUtils.isBlank(searchType)){
			rMap.put("state",false);
			rMap.put("msg", "查询类型不能为空！");
			return gson.toJson(rMap);
		}
		
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");
		
		if("1".equals(searchType)){
			if(StringUtils.isBlank(goodId)){
				rMap.put("state",false);
				rMap.put("msg", "商品ID不能为空！");
				return gson.toJson(rMap);
			}
			map.put("goodsId",  Long.valueOf(goodId));
			map.put("winStatus", 1);//查询不是待开奖的所有记录

		}else if("2".equals(searchType)){
			map.put("customerId", Long.valueOf(customerId));
			map.put("winStatus", 1);
		}else if("3".equals(searchType)){
			
		}
		int total = orderWinService.findListMapTotalCount(map);
		List<Map<Object,Object>> winList =orderWinService.queryListMapByPage(map);
		
		List<Map<Object,Object>> listMap = new ArrayList<Map<Object,Object>>();
		if(winList!=null){
			if(winList.size()>0){
				for(Map<Object,Object> object : winList){
					Map<Object,Object> m = new HashMap<Object,Object>();
					m.put("id",object.get("id"));//中奖记录id
					m.put("orderNum", object.get("order_num")==null?"":object.get("order_num"));//订单号(中奖号码)
					m.put("goodsId",object.get("goods_id"));//商品id
					m.put("goodsName",object.get("goods_name")==null?"":object.get("goods_name"));//商品名称
					m.put("goodsImage",object.get("goods_image")==null?"":object.get("goods_image"));//商品图片
					m.put("issue",object.get("issue")==null?"":object.get("issue"));//当前开奖期数
					m.put("personCount",object.get("person_count")==null?"":object.get("person_count"));//本期夺宝人数
					m.put("drawTime",object.get("draw_time")==null?"":object.get("draw_time"));//开奖时间
					m.put("winStatus", object.get("win_status")==null?"":object.get("win_status"));//开奖状态,1待开奖 2已开奖、3已发货、4已收货9已完成
					if(object.get("win_status")!=null && "1".equals(object.get("win_status").toString())){//待开奖时，计算当前时间与开奖时间的时间差
						if(object.get("draw_time")!=null){
							long interval = CalendarUtil.getDistanceTimeForSec(object.get("draw_time").toString(), CalendarUtil.getLongFormatDate());
							m.put("distanceTime",String.valueOf(interval));
						}else{
							m.put("distanceTime","");
						}
					}else{
						m.put("distanceTime","");
					}
					
					m.put("readyComment",object.get("ready_comment")==null?"0":object.get("ready_comment"));//是否已晒单,0否1是
					m.put("customerId",object.get("customer_id")==null?"":object.get("customer_id"));//中奖会员id
					m.put("nickname",object.get("c_nikename")==null?"":object.get("c_nikename"));//中奖会员昵称
					m.put("userName",object.get("user_name")==null?"":object.get("user_name"));//中奖会员用户名
					m.put("userImage",object.get("userImage")==null?"":object.get("userImage"));//中奖会员头像图片名
					m.put("goodPrice",object.get("price")==null?"":object.get("price"));//中奖商品价格
					
					listMap.add(m);
				}
			}
			rMap.put("total", total);
			rMap.put("items", listMap);
			rMap.put("pageNo", pageNo);
			rMap.put("pageSize", pageSize);
			rMap.put("curTime", CalendarUtil.getLongFormatDate());//系统当前时间
			rMap.put("goodPath", GOODS_IMAGE_PATH);
			rMap.put("userPath", USER_IMAGE_PATH);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(rMap);
	}
	
	/**
	 * 单个商品中奖记录信息（手机端）
	 */
	@RequestMapping(value = "getOrderWinInfo.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getOrderWinInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		
		String goodId = request.getParameter("goodId"); //商品id
		String issue = request.getParameter("issue"); //商品开奖期数
		
		/**
		//去掉token验证   2017-05-15
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
		}*/
		
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("goodsId",  Long.valueOf(goodId));
		map.put("issue",  Long.valueOf(issue));
		MOrderWin orderWin = orderWinService.selectByGoodidIssue(map);
		
		if(orderWin!=null){
			if(orderWin.getCustomerId() !=null){
				CCustomer record = customerService.findById(orderWin.getCustomerId());
				if(record!=null && record.getUserImage()!=null){
					orderWin.userImage=record.getUserImage();
				}else{
					orderWin.userImage="";
				}
			}else{
				orderWin.userImage="";
			}
			if(orderWin.getGoodsId()!=null){
				GGoods record = goodsService.selectByPrimaryKey(orderWin.getGoodsId());
				if(record!=null && record.getPrice()!=null){
					orderWin.goodPrice=String.valueOf(record.getPrice());
				}else{
					orderWin.goodPrice="";
				}
			}else{
				orderWin.goodPrice="";
			}
			
			rMap.put("items", orderWin);
			rMap.put("curTime", CalendarUtil.getLongFormatDate());//系统当前时间
			rMap.put("goodPath", GOODS_IMAGE_PATH);
			rMap.put("userPath", USER_IMAGE_PATH);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(rMap);
	}
	
	
	
	/**
	 * 服务端——跳转进入中奖记录查询页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="pc/zjjlgl.act",method ={RequestMethod.POST,RequestMethod.GET})
	public String indexOrderWinPC(HttpServletRequest request, ModelMap model){
		
		return "/modules/orderWin/index";
	}
	/**
	 * 服务端——获取中奖记录信息
	 * 分页查询
	 */
	@RequestMapping(value = "pc/getOrderWinListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getOrderWinListPC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		String goodsName = request.getParameter("goodsName");
		String userName = request.getParameter("userName");
		pageNo=1;
		pageSize=10;
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		map.put("winStatus", 1);//查询不是待开奖的所有记录
		if(StringUtils.isNotBlank(goodsName)){
			map.put("goodsName", "%"+goodsName+"%");
		}
		if(StringUtils.isNotBlank(userName)){
			map.put("userName", userName);
		}
		int total = orderWinService.findListMapTotalCount(map);
		List<Map<Object,Object>> winList =orderWinService.queryListMapByPage(map);
		pageInfo = PaginationUtil.getPageInfo(total, pageNo, pageSize);
		
		List<Map<Object,Object>> listMap = new ArrayList<Map<Object,Object>>();
		if(winList!=null){
			if(winList.size()>0){
				for(Map<Object,Object> object : winList){
					Map<Object,Object> m = new HashMap<Object,Object>();
					m.put("id",object.get("id"));//中奖记录id
					m.put("goodsId",object.get("goods_id"));//商品id
					m.put("issue",object.get("issue")==null?"":object.get("issue"));//当前开奖期数
					m.put("orderNum", object.get("order_num")==null?"":object.get("order_num"));//订单号(中奖号码)
					m.put("goodsName",object.get("goods_name")==null?"":object.get("goods_name"));//商品名称
					m.put("goodsImage",object.get("goods_image")==null?"":object.get("goods_image"));//商品图片
					m.put("personCount",object.get("person_count")==null?"":object.get("person_count"));//本期夺宝人数
					m.put("drawTime",object.get("draw_time")==null?"":object.get("draw_time"));//开奖时间
					m.put("winStatus", object.get("win_status")==null?"":object.get("win_status"));//开奖状态,1待开奖 2已开奖、3已发货、4已收货9已完成
					if(object.get("win_status")!=null && "1".equals(object.get("win_status").toString())){//待开奖时，计算当前时间与开奖时间的时间差
						if(object.get("draw_time")!=null){
							long interval = CalendarUtil.getDistanceTimeForSec(object.get("draw_time").toString(), CalendarUtil.getLongFormatDate());
							m.put("distanceTime",String.valueOf(interval));
						}else{
							m.put("distanceTime","");
						}
					}else{
						m.put("distanceTime","");
					}
					
					m.put("customerId",object.get("customer_id")==null?"":object.get("customer_id"));//中奖会员id
					m.put("nickname",object.get("c_nikename")==null?"":object.get("c_nikename"));//中奖会员昵称
					m.put("userName",object.get("user_name")==null?"":object.get("user_name"));//中奖会员用户名
					m.put("userImage",object.get("userImage")==null?"":object.get("userImage"));//中奖会员头像图片名
					m.put("goodPrice",object.get("price")==null?"":object.get("price"));//中奖商品价格
					
					listMap.add(m);
				}
			}
		}
		
		model.put("resultList", listMap);
		model.put("pageInfo", pageInfo);
		
		return "/modules/orderWin/data";
	}
	
	/**
	 * 跳转进入给中奖人发货、发送短信通知页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="pc/indexSendTransSms.act",method ={RequestMethod.POST,RequestMethod.GET})
	public String indexSendTransSms(HttpServletRequest request, ModelMap model){
		String objectId = request.getParameter("objectId");//获取中奖记录id
		String customerId = request.getParameter("customerId");//获取中奖人id
		
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("customerId", Long.valueOf(customerId));
		List<CAddress> list = addressService.queryListByPage(map);
		if(list != null && list.size()>=0){
			if(list.size()>0){
				model.put("address",list.get(0));
			}
		}
		MOrderWin record = orderWinService.selectByPrimaryKey(Long.valueOf(objectId));
		model.put("record",record);
		
		return "/modules/orderWin/sendSms";
	}
	/**
	 * 发送发货通知短信
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/saveSendTransSms.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	@Transactional
	public String saveSendTransSms(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String winId = request.getParameter("winId");//获取中奖记录id
		String customerId = request.getParameter("customerId");//获取中奖人id
		String customerName= request.getParameter("nickname");//获取中奖人昵称	
		String cellPhone= request.getParameter("cellPhone");//收货手机号码
		String goodsName= request.getParameter("goodsName");//中奖商品名
		String param1= request.getParameter("param1");//物流公司
		String param2= request.getParameter("param2");//快递单号
		//发送发货通知短信
		String params = "['"+customerName+"','"+goodsName+"','"+param1+"','"+param2+"']";
		String result = SmsCodeUtil.sendNoticeTemplate(CheckBuilder.NOTICE_TEMPLATE,cellPhone,customerName,goodsName,param1,param2);
		//String result = SmsCodeUtil.sendNoticeTemplate(CheckBuilder.NOTICE_TEMPLATE,cellPhone,params);
		SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
		
		//保存发货通知站内信
		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("customerName", customerName);
		user1.put("goodsName", goodsName);
		user1.put("param1", param1);
		user1.put("param2", param2);
		String content1 = TemplateEngine.readReplaceTemplate(user1,"/template/trans.tpl");
		mailService.addBMail(3,"发货通知", content1, Long.valueOf(customerId),customerName);
		
		if(codeVO !=null &&"200".equals(codeVO.code)){
			//保存发货通知短信
			BSmsLog record = new BSmsLog();
			record.setMessage(content1);
			record.setRecipient(Long.valueOf(customerId));
			record.setRecipientName(customerName);
			record.setSendTime(CalendarUtil.getLongFormatDate());
			record.setVerificationCode(cellPhone);
			record.setStatus(1);
			record.setEnabled(1);
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			smsLogMapper.insert(record);
			
			//修改中奖记录状态为：已发货
			MOrderWin win = new MOrderWin();
			win.setId(Long.valueOf(winId));
			win.setWinStatus(3);//已发货
			orderWinService.updateByPrimaryKeySelective(win);
			
			returnMap.put("state", true);
			returnMap.put("msg", "短信发送成功！");
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "短信发送失败！");
		}
		return gson.toJson(returnMap);
	}
}
