package com.zj.ryxb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.quartz.CronDateUtils;
import com.zj.ryxb.common.quartz.QuartzManager2;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.CAmountMapper;
import com.zj.ryxb.dao.CBonusMapper;
import com.zj.ryxb.dao.GGoodsMapper;
import com.zj.ryxb.dao.MCartMapper;
import com.zj.ryxb.dao.MOrderDetailMapper;
import com.zj.ryxb.dao.MOrderMapper;
import com.zj.ryxb.dao.MOrderWinMapper;
import com.zj.ryxb.model.CAmount;
import com.zj.ryxb.model.CBonus;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.model.MCart;
import com.zj.ryxb.model.MOrder;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.model.MOrderWin;

@Service
public class OrderService{
	@Autowired
	QuartzManager2 quartzManager;
	@Autowired
	MOrderMapper orderMapper;
	@Autowired
	MOrderDetailMapper orderDetailMapper;
	@Autowired
	GGoodsMapper gGoodsMapper;
	@Autowired
	MCartMapper cartMapper;
	@Autowired
	CAmountMapper amountMapper;
	@Autowired
	MOrderWinMapper orderWinMapper;
	@Autowired
	private OrderDetailService orderDetailService;
	/**
	 * 客户红包
	 */
	@Autowired
	CBonusMapper cBonusMapper;
	
	
	/**
     * 根据客户id查询订单流水号集合
     * @return
     */
   public  List<String> queryOrderNumByCusId(Long customId){
	   return orderMapper.queryOrderNumByCusId(customId);
   }
   /**
    * 根据map条件分页查询数据列表
    * @param map
    * @return
    */
   public List<MOrder> queryListByPage(Map<String,Object> map){
	   return orderMapper.queryListByPage(map);
   }
   /**
    * 根据map条件获取数据总条数
    * @param map
    * @return
    */
   public int findTotalCount(Map<String,Object> map){
	   return orderMapper.findTotalCount(map);
   }
    
   /**
    * 商品付款保存
    * @param customerId
    * @param goodsIdStr 商品id 多个用”，“隔开
    * @param goodsNameStr 商品名称 多个用”，“隔开
    * @param quantityStr 商品数量 多个用”，“隔开
    * @param issueStr 开奖期数 多个用”，“隔开
    * @param cbID  客户红包id
    * @return
    */
	@Transactional
	public Map<String, Object> saveAndPay(Long customerId,String goodsIdStr,String goodsNameStr,
			String quantityStr,String issueStr,String cbID){	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String[] goodsIdArray = goodsIdStr.split(",");
		String[] goodsNameArray = goodsNameStr.split(",");
		String[] quantityArray = quantityStr.split(",");
		String[] issueArray = issueStr.split(",");
		
		//订单流水号,YYMMDDhhmissSSS+用户ID后4位
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String orderNum = "D" + formatter.format(now) + StringUtils.right("000000" + customerId, 6);
		
		List<MOrderDetail> detailList = new ArrayList<MOrderDetail>();
		int totalQuantity = 0;//总数量
		double totalAmount = 0.0;//总价格
		
		String msg = "";//异常信息
		List<GGoods> failedGoods =new ArrayList<GGoods>();//失败商品列表
		List<GGoods> sucessGoods =new ArrayList<GGoods>();//成功商品列表
		for(int i=0;i<goodsIdArray.length;i++){
			String goodsId = goodsIdArray[i];
			String goodsName = goodsNameArray[i];
			String quantity = quantityArray[i];
			String issue = issueArray[i];

			GGoods goods;
			Map<String,Object> goodsIdMap = new HashMap<String,Object>();
			goodsIdMap.put("id", goodsId);
			goodsIdMap.put("pageIndex", 0);
			goodsIdMap.put("pageSize", 100);
			
			List<GGoods> goodsList = gGoodsMapper.queryGoodsFromApp(goodsIdMap);

			GGoods nogood = new GGoods();
			GGoods yesgood = new GGoods();
			//根据goodsId无法查询到商品，跳出循环
			if (goodsList.size() ==0){
				msg += goodsName + "已下架！";
				nogood.setId(Long.valueOf(goodsId) );
				nogood.setGoodsName(goodsName);
				nogood.setIssue(Integer.valueOf(issue));
				failedGoods.add(nogood);
				continue;
			}else{
				goods = goodsList.get(0);
				yesgood.setId(goods.getId());
				yesgood.setGoodsName(goods.getGoodsName());
				yesgood.setIssue(goods.getIssue());
			}
			//如果商品待开奖，跳出循环
			if(goods.getStatus() ==2 ){
				msg += goods.getGoodsName() + "待开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			//当前剩余数量为0，跳出循环
			if(goods.getRestQuantity() == 0){
				msg += goods.getGoodsName() + "待开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			//如果商品的当前开奖期数与传入的不一致，跳出循环
			if(Integer.valueOf(issue) !=goods.getIssue() ){
				msg += goods.getGoodsName() + "已开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			sucessGoods.add(yesgood);
			//构建订单详情数据
			MOrderDetail orderDetail = new MOrderDetail();
			orderDetail.setCustomerId(customerId);
			orderDetail.setOrderNum(orderNum);
			orderDetail.setGoodsId(goods.getId());
			orderDetail.setGoodsName(goods.getGoodsName());
			orderDetail.setGoodsImage(goods.getGoodsImage());
			orderDetail.setPrice(goods.getPrice());
			orderDetail.setMinLimit(goods.getMinLimit());
			//如果购买数量大于剩余数量,包尾，其他钱退回
			if(Integer.valueOf(quantity) >= goods.getRestQuantity()){
				String msginfo = Integer.valueOf(quantity).equals(goods.getRestQuantity()) ?"购买成功！":"剩余人次不足，已包尾，多余钱已退回账号！";
				msg += goods.getGoodsName() + msginfo;
				orderDetail.setQuantity(goods.getRestQuantity());
				orderDetail.setStatus(2);//设置为待开奖状态
				
	    		//将商品状态更新为待开奖
				goods.setStatus(2);
				goods.setProgress(0.0);
				goods.setRestQuantity(null);
				gGoodsMapper.updateByPrimaryKeySelective(goods);
				
				//在中奖表中插入一条记录，开奖开始倒计时
				MOrderWin orderWin = new MOrderWin();
				orderWin.setGoodsId(goods.getId());
				orderWin.setGoodsName(goods.getGoodsName());
				orderWin.setGoodsImage(goods.getGoodsImage());
				orderWin.setIssue(goods.getIssue());
				orderWin.setCreateTime(CalendarUtil.getLongFormatDate());
				orderWin.setWinStatus(1);//开奖状态 1待开奖 
				Date drawTime = new Date( new Date().getTime() + goods.getTurnaround() * 1000);
				orderWin.setDrawTime(CalendarUtil.transLongFormatDate(drawTime));
				orderWin.setReadyComment(0);
				orderWin.setEnabled(1);
				orderWinMapper.insert(orderWin);
				
				//更新商品当前开奖期数——所有订单明细状态为：2待开奖
	    		Map<String,Object> updateMap = new HashMap<String,Object>();
	    		updateMap.put("status", "2");
	    		updateMap.put("goodsId", goods.getId());
	    		updateMap.put("issue",goods.getIssue());
	    		orderDetailService.updateStatus(updateMap);
	    		
				//触发定时器
	    		quartzManager.addJob(orderWin.getId().toString(), "ORDER_JOB_GROUP", "OT_"+ Long.valueOf(orderWin.getId()), "ORDER_TRIGGER_GROUP", DrawLotteryService.class, CronDateUtils.getCron(drawTime));
				//QuartzManager.addJob(orderWin.getId().toString(), "ORDER_JOB_GROUP", "OT_"+ Long.valueOf(orderWin.getId()), "ORDER_TRIGGER_GROUP", DrawLotteryService.class, CronDateUtils.getCron(drawTime));    

			}else{
				orderDetail.setQuantity(Integer.valueOf(quantity));
				orderDetail.setStatus(1);//开奖状态： 1未开奖
				msg += goods.getGoodsName() + "购买成功！";
				
			}
			orderDetail.setIssue(goods.getIssue());//商品当前开奖期数
			orderDetail.setEnabled(1);
			orderDetail.setCreateTime(CalendarUtil.getLongFormatDate());
			detailList.add(orderDetail);//放到list集合中

			totalQuantity += orderDetail.getQuantity();
			//totalAmount += orderDetail.getMinLimit() * orderDetail.getQuantity();
			totalAmount += orderDetail.getQuantity();//结算金额=购买数量
			
			//删除购物车
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("customerId", Long.valueOf(customerId));
			map.put("goodsId", Long.valueOf(goodsId));
			List<MCart> cartList = cartMapper.selectByMap(map);
			if(cartList !=null && cartList.size()>0){
				MCart cart = cartList.get(0);
				cartMapper.deleteByPrimaryKey(cart.getId());		
			}
		}
		
		MOrder order = new MOrder();
		order.setOrderNum(orderNum);
		order.setCustomerId(Long.valueOf(customerId));
		order.setTotalAmount(totalAmount);
		order.setTotalQuantity(totalQuantity);
		order.setMessageinfo("");
		order.setStatus(1);
		order.setCreateTime(CalendarUtil.getLongFormatDate());
		order.setEnabled(1);
		
		//全部商品都异常
		if(detailList.size()==0){
			returnMap.put("state", false);
			returnMap.put("msg", msg);
			returnMap.put("sucessGoods",sucessGoods);
			returnMap.put("failedGoods",failedGoods);
			return returnMap;
		}
		
		//金额不足
		CAmount camount = amountMapper.getAmountByCustomerId(customerId);
		if(camount == null){
			returnMap.put("state", false);
			returnMap.put("msg", "金额不足！");
			return returnMap;
		}
		
		Double bAmount=0.0;//红包金额
		CBonus cBonus =null;//客户红包对象
		//判断是否使用红包
		if(StringUtils.isNotBlank(cbID)){
			cBonus = cBonusMapper.selectByPrimaryKey(Long.valueOf(cbID));
			if(cBonus !=null && cBonus.getStatus()==0){ //判断是未使用的红包
				bAmount = cBonus.getAmount();//红包金额 = 当前使用的红包的额度
			}else{
				msg += "该红包已使用或已失效，本次不能使用。";
			}
		}
		//判断客户 (剩余额度+红包金额 )是否小于订单总金额
		if(camount.getRemainAmout() == null || (bAmount+camount.getRemainAmout()) < totalAmount){
			returnMap.put("state", false);
			returnMap.put("msg", "金额不足！");
			return returnMap;
		}
		Double actualAmount = totalAmount-bAmount;//实际充值、消费额度=订单总金额-红包金额
		//新增保存客户金额记录表
		CAmount consume = new CAmount();
		consume.setCustomerId(customerId);
		consume.setCurAmount(camount.getRemainAmout());
		consume.setActualAmount(actualAmount);//实际充值、消费额度
		consume.setRechargeType(2);//1:充值2:消费
		consume.setRemainAmout(camount.getRemainAmout() - actualAmount);//剩余额度 = 当前额度-实际消费额度
		consume.setEnabled(1);
		consume.setCreateTime(CalendarUtil.getLongFormatDate());

		orderDetailMapper.batchInsert(detailList);
		orderMapper.insert(order);		
		amountMapper.insert(consume);
		
		if(cBonus !=null && bAmount>0){//判断该红包是否被使用
			CBonus record = new CBonus();
			record.setId(cBonus.getId());
			record.setStatus(1);//红包使用状态，0未使用1已使用2已失效
			cBonusMapper.updateByPrimaryKeySelective(record);//更新红包状态
		}
		
		returnMap.put("state", true);
		returnMap.put("sucessGoods",sucessGoods);
		returnMap.put("failedGoods",failedGoods);
		returnMap.put("msg",msg);	
		
		return returnMap;
	}

	
	 /**
    * 商品订单生成
    * @param customerId
    * @param goodsIdStr 商品id 多个用”，“隔开
    * @param goodsNameStr 商品名称 多个用”，“隔开
    * @param quantityStr 商品数量 多个用”，“隔开
    * @param issueStr 开奖期数 多个用”，“隔开
    * @param cbID  客户红包id
    * @return
    */
	@Transactional
	public Map<String, Object> saveOrder(Long customerId,String goodsIdStr,String goodsNameStr,
		String quantityStr,String issueStr,String cbID){	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String[] goodsIdArray = goodsIdStr.split(",");
		String[] goodsNameArray = goodsNameStr.split(",");
		String[] quantityArray = quantityStr.split(",");
		String[] issueArray = issueStr.split(",");
		
		//订单流水号,YYMMDDhhmissSSS+用户ID后4位
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String orderNum = "D" + formatter.format(now) + StringUtils.right("000000" + customerId, 6);
		
		List<MOrderDetail> detailList = new ArrayList<MOrderDetail>();
		int totalQuantity = 0;//总数量
		double totalAmount = 0.0;//总价格
		
		String msg = "";//异常信息
		List<GGoods> failedGoods =new ArrayList<GGoods>();//失败商品列表
		List<GGoods> sucessGoods =new ArrayList<GGoods>();//成功商品列表
		for(int i=0;i<goodsIdArray.length;i++){
			String goodsId = goodsIdArray[i];
			String goodsName = goodsNameArray[i];
			String quantity = quantityArray[i];
			String issue = issueArray[i];

			GGoods goods;
			Map<String,Object> goodsIdMap = new HashMap<String,Object>();
			goodsIdMap.put("id", goodsId);
			goodsIdMap.put("pageIndex", 0);
			goodsIdMap.put("pageSize", 100);
			
			List<GGoods> goodsList = gGoodsMapper.queryGoodsFromAppNew(goodsIdMap);

			GGoods nogood = new GGoods();
			GGoods yesgood = new GGoods();
			//根据goodsId无法查询到商品，跳出循环
			if (goodsList.size() ==0){
				msg += goodsName + "已下架！";
				nogood.setId(Long.valueOf(goodsId) );
				nogood.setGoodsName(goodsName);
				nogood.setIssue(Integer.valueOf(issue));
				failedGoods.add(nogood);
				continue;
			}else{
				goods = goodsList.get(0);
				yesgood.setId(goods.getId());
				yesgood.setGoodsName(goods.getGoodsName());
				yesgood.setIssue(goods.getIssue());
			}
			//如果商品待开奖，跳出循环
			if(goods.getStatus() ==2 ){
				msg += goods.getGoodsName() + "待开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			//当前剩余数量为0，跳出循环
			if(goods.getRestQuantity() == 0){
				msg += goods.getGoodsName() + "待开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			//如果商品的当前开奖期数与传入的不一致，跳出循环
			if(Integer.valueOf(issue) !=goods.getIssue() ){
				msg += goods.getGoodsName() + "已开奖！";
				failedGoods.add(yesgood);
				continue;
			}
			sucessGoods.add(yesgood);
			//构建订单详情数据
			MOrderDetail orderDetail = new MOrderDetail();
			orderDetail.setCustomerId(customerId);
			orderDetail.setOrderNum(orderNum);
			orderDetail.setGoodsId(goods.getId());
			orderDetail.setGoodsName(goods.getGoodsName());
			orderDetail.setGoodsImage(goods.getGoodsImage());
			orderDetail.setPrice(goods.getPrice());
			orderDetail.setMinLimit(goods.getMinLimit());
			orderDetail.setStatus(1);//开奖状态： 1未开奖
			//如果购买数量大于剩余数量,包尾，其他钱退回；将商品状态更新为待开奖；在中奖表中插入一条记录，开奖开始倒计时
			if(Integer.valueOf(quantity) >= goods.getRestQuantity()){
				String msginfo = Integer.valueOf(quantity).equals(goods.getRestQuantity()) ?"生成订单成功！":"剩余数量不足，已包尾！";
				msg += goods.getGoodsName() + msginfo;
				orderDetail.setQuantity(goods.getRestQuantity());
			}else{
				orderDetail.setQuantity(Integer.valueOf(quantity));		
				msg += goods.getGoodsName() + "生成订单成功！";
				
			}
			orderDetail.setIssue(goods.getIssue());//商品当前开奖期数
			orderDetail.setEnabled(1);
			orderDetail.setCreateTime(CalendarUtil.getLongFormatDate());
			detailList.add(orderDetail);//放到list集合中

			totalQuantity += orderDetail.getQuantity();
			totalAmount += orderDetail.getQuantity();//结算金额=购买数量
			
			//删除购物车
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("customerId", Long.valueOf(customerId));
			map.put("goodsId", Long.valueOf(goodsId));
			List<MCart> cartList = cartMapper.selectByMap(map);
			if(cartList !=null && cartList.size()>0){
				MCart cart = cartList.get(0);
				cartMapper.deleteByPrimaryKey(cart.getId());		
			}
		}
		
		//全部商品都异常
		if(detailList.size()==0){
			returnMap.put("state", false);
			returnMap.put("msg", msg);
			returnMap.put("sucessGoods",sucessGoods);
			returnMap.put("failedGoods",failedGoods);
			return returnMap;
		}
		
		MOrder order = new MOrder();
		order.setOrderNum(orderNum);
		order.setCustomerId(Long.valueOf(customerId));
		order.setTotalQuantity(totalQuantity);
		order.setMessageinfo("");
		order.setStatus(0);
		
		Double bAmount=0.0;//红包金额
		CBonus cBonus =null;//客户红包对象
		//判断是否使用红包
		if(StringUtils.isNotBlank(cbID)){
			cBonus = cBonusMapper.selectByPrimaryKey(Long.valueOf(cbID));
			if(cBonus !=null && cBonus.getStatus()==0){ //判断是未使用的红包
				bAmount = cBonus.getAmount();//红包金额 = 当前使用的红包的额度
				order.setBonusId(cBonus.getId());
				
				CBonus record = new CBonus();
				record.setId(cBonus.getId());
				record.setStatus(1);//红包使用状态，0未使用1已使用2已失效
				cBonusMapper.updateByPrimaryKeySelective(record);//更新红包状态
				
			}else{
				msg += "该红包已使用或已失效，本次不能使用。";
			}
		}
		Double actualAmount = totalAmount-bAmount;//实际充值、消费额度=订单总金额-红包金额

		order.setTotalAmount(actualAmount);
		order.setCreateTime(CalendarUtil.getLongFormatDate());
		order.setEnabled(1);
		orderDetailMapper.batchInsert(detailList);
		orderMapper.insert(order);	
		
		Date updateTime = new Date( new Date().getTime() + 120 * 1000);
		//触发订单取消(未付款)定时器
		quartzManager.addJob(order.getOrderNum(), "UPDATE_ORDER_JOB_GROUP", "UO_"+ order.getOrderNum(), "UPDATE_ORDER_TRIGGER_GROUP", UpdateOrderService.class, CronDateUtils.getCron(updateTime));
		
		returnMap.put("state", true);
		returnMap.put("order", order);
		returnMap.put("sucessGoods",sucessGoods);
		returnMap.put("failedGoods",failedGoods);
		returnMap.put("msg",msg);	
		
		return returnMap;
	}
			
	 /**
    * 余额支付
    * @param customerId
    * @param orderNum 订单号
    * @return
    */
	@Transactional
	public Map<String, Object> yuePay(Long customerId,String orderNum){	
		Map<String, Object> returnMap = new HashMap<String , Object>();

		MOrder order = orderMapper.selectByOrderNum(orderNum);
		if(order.getStatus()<0){
			returnMap.put("state", false);
			returnMap.put("msg", "交易超时，订单已取消！");
			return returnMap;
		}
		//金额不足
		CAmount camount = amountMapper.getAmountByCustomerId(customerId);
		if(camount == null){
			returnMap.put("state", false);
			returnMap.put("msg", "金额不足！");
			return returnMap;
		}
		
		//判断客户 (剩余额度 )是否小于订单总金额
		if(camount.getRemainAmout() == null || (camount.getRemainAmout()) < order.getTotalAmount()){
			returnMap.put("state", false);
			returnMap.put("msg", "金额不足！");
			return returnMap;
		}

		//新增保存客户金额记录表
		CAmount consume = new CAmount();
		consume.setCustomerId(customerId);
		consume.setCurAmount(camount.getRemainAmout());
		consume.setActualAmount(order.getTotalAmount());//订单总金额
		consume.setRechargeType(2);//1:充值2:消费
		consume.setRemainAmout(camount.getRemainAmout() - order.getTotalAmount());//剩余额度 = 当前额度-订单总金额
		consume.setEnabled(1);
		consume.setCreateTime(CalendarUtil.getLongFormatDate());
		amountMapper.insert(consume);		
		
		//更新状态并删除订单取消(未付款)定时器
		order.setStatus(1);//1已付款
		orderMapper.updateByPrimaryKeySelective(order);
		quartzManager.removeJob(order.getOrderNum(), "UPDATE_ORDER_JOB_GROUP", "OT_"+ order.getOrderNum(), "UPDATE_ORDER_TRIGGER_GROUP");   
		System.out.println("余额支付后，移除订单取消任务jobName======"+order.getOrderNum());
		
		returnMap.put("state", true);
		returnMap.put("msg","支付成功");	

		return returnMap;
	}
	
	/**
    * 订单支付后判断是否开奖
    * @param orderNum 订单号
    * @return
    */
	@Transactional
	public void updateLottery(String orderNum){	
		//查询订单详情数据集合
		List<MOrderDetail> detailList = orderDetailMapper.queryByOrderNum(orderNum);
		//遍历订单详情数据
		for(int i=0;i<detailList.size();i++){
			MOrderDetail orderDetail = detailList.get(i);

			Map<String,Object> goodsIdMap = new HashMap<String,Object>();
			goodsIdMap.put("id", orderDetail.getGoodsId());
			goodsIdMap.put("pageIndex", 0);
			goodsIdMap.put("pageSize", 100);
			
			List<GGoods> goodsList = gGoodsMapper.queryGoodsFromApp(goodsIdMap);
			if (goodsList.size() ==0){
				continue;
			}
			
			GGoods goods = goodsList.get(0);
			if(goods.getRestQuantity() > 0 ){
				continue;
			}
			
    		//将商品状态更新为待开奖
			goods.setStatus(2);
			goods.setProgress(0.0);
			goods.setRestQuantity(0);
			gGoodsMapper.updateByPrimaryKeySelective(goods);
			
			//在中奖表中插入一条记录，开奖开始倒计时
			MOrderWin orderWin = new MOrderWin();
			orderWin.setGoodsId(goods.getId());
			orderWin.setGoodsName(goods.getGoodsName());
			orderWin.setGoodsImage(goods.getGoodsImage());
			orderWin.setIssue(goods.getIssue());
			orderWin.setCreateTime(CalendarUtil.getLongFormatDate());
			orderWin.setWinStatus(1);//开奖状态 1待开奖 
			Date drawTime = new Date( new Date().getTime() + goods.getTurnaround() * 1000);
			orderWin.setDrawTime(CalendarUtil.transLongFormatDate(drawTime));
			orderWin.setReadyComment(0);
			orderWin.setEnabled(1);
			orderWinMapper.insert(orderWin);
			
			//更新商品当前开奖期数——所有订单明细状态为：2待开奖
    		Map<String,Object> updateMap = new HashMap<String,Object>();
    		updateMap.put("status", "2");
    		updateMap.put("goodsId", goods.getId());
    		updateMap.put("issue",goods.getIssue());
    		orderDetailService.updateStatus(updateMap);
    		
			//触发开奖定时器
    		quartzManager.addJob(orderWin.getId().toString(), "DRAW_LOTTERY_JOB_GROUP", "DT_"+ Long.valueOf(orderWin.getId()), "DRAW_LOTTERY_TRIGGER_GROUP", DrawLotteryService.class, CronDateUtils.getCron(drawTime));
			//QuartzManager.addJob(orderWin.getId().toString(), "ORDER_JOB_GROUP", "OT_"+ Long.valueOf(orderWin.getId()), "ORDER_TRIGGER_GROUP", DrawLotteryService.class, CronDateUtils.getCron(drawTime));    
		}
		
	}
	
	/**
	 * 支付宝通知校验成功后处理方法
	 * 更新订单状态、删除订单取消定时器、判断是否购买满可开奖
	 * @param orderNum
	 */
	@Transactional
	public void receiveAlipayHandel(MOrder order){
		//更新状态并删除订单取消(未付款)定时器
		order.setStatus(1);//1已付款
		orderMapper.updateByPrimaryKeySelective(order);
		quartzManager.removeJob(order.getOrderNum(), "UPDATE_ORDER_JOB_GROUP", "OT_"+ order.getOrderNum(), "UPDATE_ORDER_TRIGGER_GROUP");   
		System.out.println("支付宝支付后，移除订单取消任务jobName======"+order.getOrderNum());
		
		this.updateLottery(order.getOrderNum());
		
	}
}
