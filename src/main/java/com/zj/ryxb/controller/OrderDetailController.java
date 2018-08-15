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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.dao.MOrderDetailMapper;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.OrderDetailService;

/**
 * 
 * @Description:订单详情相关(寻宝记录)
 * @ClassName: OrderController 
 */
@Controller
@RequestMapping("/")
public class OrderDetailController extends BaseController {
	
	@Autowired
	private OrderDetailService detailService;
	
	@Autowired
	private MOrderDetailMapper orderDetailMapper;
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 寻宝记录（手机端）
	 */
	@RequestMapping(value = "getOrderDetailList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getOrderDetailList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();

		String strToken = request.getParameter("token");
		String page = request.getParameter("pageNo");//当前页码数
		String size = request.getParameter("pageSize");//每页显示条数
		
		
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
		pageNo=1;
		pageSize=10;
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);
		}
		if(StringUtils.isNotBlank(size)){
			pageSize =Integer.valueOf(size);
		}
		//获取该用户的所有订单流水号
		//List<String> orderNumList = orderService.queryOrderNumByCusId(Long.valueOf(customerId));
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");
		map.put("customerId", Long.valueOf(customerId));//会员id
		
		
		List<Map<Object,Object>> orderDetailList = orderDetailMapper.queryListMapByPage(map);
		int total = orderDetailMapper.findListMapTotalCount(map);
		
		List<Map<Object,Object>> listMap = new ArrayList<Map<Object,Object>>();
		
		if(orderDetailList !=null){
			if(orderDetailList.size()>0){
				for(Map<Object,Object> object : orderDetailList){
					Map<Object,Object> m = new HashMap<Object,Object>();
					m.put("id",object.get("id"));//订单详情id
					m.put("orderNum", object.get("order_num")==null?"":object.get("order_num"));//订单号
					m.put("customerId",object.get("customer_id"));//订单用户
					m.put("goodsId",object.get("goods_id"));//商品id
					m.put("goodsName",object.get("goods_name")==null?"":object.get("goods_name"));//商品名称
					m.put("goodsImage",object.get("goods_image")==null?"":object.get("goods_image"));//商品图片
					m.put("price",object.get("price")==null?"":object.get("price"));//商品价格
					m.put("minLimit",object.get("min_limit")==null?"":object.get("min_limit"));//最小购买单元
					m.put("quantity",object.get("quantity")==null?"":object.get("quantity"));//购买数量
					m.put("issue",object.get("issue")==null?"":object.get("issue"));//当前开奖期数
					m.put("status",object.get("status")==null?"":object.get("status"));//订单详情状态,1未开奖2待开奖 3未中奖4已中奖
					m.put("win_id",object.get("win_id")==null?"":object.get("win_id"));//中奖记录ID
					m.put("win_orderNum",object.get("win_orderNum")==null?"":object.get("win_orderNum"));//中奖订单号码
					m.put("win_customerID",object.get("win_customer")==null?"":object.get("win_customer"));//中奖会员ID
					m.put("win_nickname",object.get("win_nickname")==null?"":object.get("win_nickname"));//中奖会员昵称
					m.put("win_personCount",object.get("person_count")==null?"":object.get("person_count"));//本期夺宝人数
					m.put("win_drawTime",object.get("draw_time")==null?"":object.get("draw_time"));//开奖时间
					m.put("win_status", object.get("win_status")==null?"":object.get("win_status"));//开奖状态	
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
					//判断是否有中奖者
					if(object.get("win_customer")!=null && object.get("win_customer")!=""){
						CCustomer record = customerService.findById(Long.valueOf(object.get("win_customer").toString()));
						if(record!=null &&record.getUserImage()!=null){
							m.put("win_userImage",record.getUserImage());//中奖会员头像
						}else{
							m.put("win_userImage","");
						}
					}else{
						m.put("win_userImage","");
					}
					
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

}
