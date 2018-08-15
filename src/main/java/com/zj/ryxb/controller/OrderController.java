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
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.model.MCart;
import com.zj.ryxb.service.CartService;
import com.zj.ryxb.service.GoodsService;
import com.zj.ryxb.service.OrderService;

/**
 * 
 * @Description:订单相关
 * @ClassName: OrderController 
 */
@Controller
@RequestMapping("/")
public class OrderController extends BaseController{
	
	@Autowired
	private CartService cartService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 获取购物车（手机端）
	 */
	@RequestMapping(value = "getCartList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getCartList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
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
		
		List<MCart> cartList = cartService.selectByCustomerID(Long.valueOf(customerId) );
		
		if(cartList!=null){
			returnMap.put("state", true);
			returnMap.put("total", cartList.size());
			returnMap.put("items", cartList);
			returnMap.put("goodPath", GOODS_IMAGE_PATH);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}

		return gson.toJson(returnMap);
	}
	
	/**
	 * 加入购物车（手机端）
	 */
	@RequestMapping(value = "addCart.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String addCart(HttpServletRequest request, HttpServletResponse response, ModelMap model){	
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
		
		String quantity = request.getParameter("quantity");
		String goodsId = request.getParameter("goodsId");
		String issue = request.getParameter("issue");
		
		//goodsId为空，返回失败
		if (StringUtils.isBlank(goodsId)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_NO_GOODSID);
			return gson.toJson(returnMap);
		}
		
		//根据goodsId无法查询到商品，返回失败
		Map<String,Object> map1 = new HashMap<>();
		map1.put("id", goodsId);
		map1.put("issue", issue);
		map1.put("status", 1);
		List<GGoods> list = goodsService.queryListByPage(map1);
		if(list ==null || list.size()<=0){
			returnMap.put("state", false);
			returnMap.put("msg", "该商品已集满，请购买下一期！");
			return gson.toJson(returnMap);
		}
		/*GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(goodsId));
		if (goods == null){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_NO_GOODSID);
			return gson.toJson(returnMap);
		}
		*/
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("customerId", Long.valueOf(customerId));
		map.put("goodsId", Long.valueOf(goodsId));
		
		List<MCart> MCartFromDatebase = cartService.selectByMap(map);
		int result;
		//如果商品已经存在则更新数量，如果不存在则插入
		if(MCartFromDatebase.size()>0){
			MCart cart = MCartFromDatebase.get(0);
			cart.setQuantity(Integer.valueOf(quantity) + cart.getQuantity());
			result = cartService.updateByPrimaryKey(cart);
		}else{
			
			MCart cart = new MCart();
			cart.setCustomerId(Long.valueOf(customerId));
			cart.setGoodsId(list.get(0).getId());
			cart.setGoodsName(list.get(0).getGoodsName());
			cart.setGoodsImage(list.get(0).getGoodsImage());
			cart.setPrice(list.get(0).getPrice());
			cart.setMinLimit(list.get(0).getMinLimit());
			cart.setIssue(Integer.valueOf(issue));
			cart.setQuantity(Integer.valueOf(quantity));
			cart.setCreateTime(CalendarUtil.getLongFormatDate());
			cart.setEnabled(1);

			result = cartService.insert(cart);
		}
		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", "添加购物车成功！");
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "添加购物车失败！");
		}

		return gson.toJson(returnMap);
	}
	
	/**
	 * 删除购物车（手机端）
	 */
	@RequestMapping(value = "removeCart.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String removeCart(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String goodsId = request.getParameter("goodsId");
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
		
		
		//goodsId为空，返回失败
		if (StringUtils.isBlank(goodsId)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_NO_GOODSID);
			return gson.toJson(returnMap);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("customerId", Long.valueOf(customerId));
		map.put("goodsId", Long.valueOf(goodsId));
		
		List<MCart> MCartFromDatebase = cartService.selectByMap(map);
		//未查询到记录，返回失败
		if(MCartFromDatebase.size() == 0){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_NO_GOODSID);
			return gson.toJson(returnMap);
		}
		
		MCart cart = MCartFromDatebase.get(0);
		int result = cartService.delete(cart.getId());
		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_DEL_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_DEL_FAILED);
		}

		return gson.toJson(returnMap);
	}

	
	/**
	 * 支付（手机端）
	 */
	@RequestMapping(value = "payOld.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public synchronized String payOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model){	
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

		String goodsIdStr = request.getParameter("goodsIdStr");//商品id
		String goodsNameStr = request.getParameter("goodsNameStr");//商品名称
		String quantityStr = request.getParameter("quantityStr");//商品数量
		String issueStr = request.getParameter("issueStr");//开奖期数
		String cbID = request.getParameter("cbID");//客户红包id
		//数据条数校验
		if(!(goodsIdStr.split(",").length == goodsNameStr.split(",").length && goodsIdStr.split(",").length == quantityStr.split(",").length
				&& goodsIdStr.split(",").length == issueStr.split(",").length)){
			returnMap.put("state", false);
			returnMap.put("msg", "商品数据不完整");
			return gson.toJson(returnMap);
		}

		returnMap = orderService.saveAndPay(Long.valueOf(customerId), goodsIdStr, goodsNameStr, quantityStr, issueStr,cbID);
		return gson.toJson(returnMap);
	}
	
	/**
	 * 支付（手机端）
	 */
	@RequestMapping(value = "saveOrder.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public synchronized String saveOrder(HttpServletRequest request, HttpServletResponse response, ModelMap model){	
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

		String goodsIdStr = request.getParameter("goodsIdStr");//商品id
		String goodsNameStr = request.getParameter("goodsNameStr");//商品名称
		String quantityStr = request.getParameter("quantityStr");//商品数量
		String issueStr = request.getParameter("issueStr");//开奖期数
		String cbID = request.getParameter("cbID");//客户红包id
		//数据条数校验
		if(!(goodsIdStr.split(",").length == goodsNameStr.split(",").length && goodsIdStr.split(",").length == quantityStr.split(",").length
				&& goodsIdStr.split(",").length == issueStr.split(",").length)){
			returnMap.put("state", false);
			returnMap.put("msg", "商品数据不完整");
			return gson.toJson(returnMap);
		}

		returnMap = orderService.saveOrder(Long.valueOf(customerId), goodsIdStr, goodsNameStr, quantityStr, issueStr,cbID);
		return gson.toJson(returnMap);
	}
	
	/**
	 * 余额支付（手机端）
	 */
	@RequestMapping(value = "yuePay.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public synchronized String yuePay(HttpServletRequest request, HttpServletResponse response, ModelMap model){	
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

		String orderNum = request.getParameter("orderNum");//订单号
		returnMap = orderService.yuePay(Long.valueOf(customerId),orderNum);		
		
		//触发开奖
		orderService.updateLottery(orderNum);
		
		return gson.toJson(returnMap);
	}
	
	
}
