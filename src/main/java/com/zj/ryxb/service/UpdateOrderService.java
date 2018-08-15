package com.zj.ryxb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.zj.ryxb.common.quartz.QuartzManager2;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.TemplateEngine;
import com.zj.ryxb.dao.CBonusMapper;
import com.zj.ryxb.dao.MCartMapper;
import com.zj.ryxb.dao.MOrderDetailMapper;
import com.zj.ryxb.dao.MOrderMapper;
import com.zj.ryxb.model.CBonus;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.MCart;
import com.zj.ryxb.model.MOrder;
import com.zj.ryxb.model.MOrderDetail;



/**
 * 开奖服务类
 * @author han
 *
 */
@Service
public class UpdateOrderService extends SpringBeanAutowiringSupport implements Job {
	@Autowired
	QuartzManager2 quartzManager;
	@Autowired
	MOrderMapper orderMapper;
	@Autowired
	MCartMapper cartMapper;
	@Autowired
	CBonusMapper cBonusMapper;
	@Autowired
	MOrderDetailMapper orderDetailMapper;
	@Autowired
	private BMailService mailService;
	@Autowired
	private CustomerService customerService;
	
	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
        // The name is defined in the job definition
        String jobName = ((JobDetailImpl)jobDetail).getName();//任务名称
        //String jobName  = (String)jobDetail.getJobDataMap().get("jobName");
        System.out.println("获取订单取消任务参数jobName======"+jobName);
        if(jobName==null || jobName==""){
        	System.out.println("---------获取订单取消任务名称失败");
        }
        //订单取消
        MOrder order = orderMapper.selectByOrderNum(jobName);	
        order.setStatus(-1);
        orderMapper.updateByPrimaryKeySelective(order);
        
        //删除本次订单购物车中的商品
  		List<MOrderDetail> detailList = orderDetailMapper.queryByOrderNum(order.getOrderNum());
  		//遍历订单详情数据
  		for(int i=0;i<detailList.size();i++){
  			MOrderDetail orderDetail = detailList.get(i);
  			
  			Map<String,Object> map = new HashMap<String,Object>();
  			map.put("customerId", order.getCustomerId());
  			map.put("goodsId", orderDetail.getGoodsId());
  			List<MCart> cartList = cartMapper.selectByMap(map);
  			if(cartList !=null && cartList.size()>0){
  				MCart cart = cartList.get(0);
  				cartMapper.deleteByPrimaryKey(cart.getId());		
  			}
  		}
  		
        //修改取消订单关联的红包状态
		if(order.getBonusId()!=null){
			CBonus  record = new CBonus();
			record.setId(order.getBonusId());
			record.setStatus(0);//红包使用状态，0未使用1已使用2已失效
			cBonusMapper.updateByPrimaryKeySelective(record);//更新红包状态
		}
		
		//保存订单取消站内信
		CCustomer customer =customerService.findById(order.getCustomerId());
		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("customerName", customer.getNickname());
		user1.put("orderNum", order.getOrderNum());
		user1.put("time", CalendarUtil.getLongFormatDate());
		String content1 = TemplateEngine.readReplaceTemplate(user1,"/template/orderCancel.tpl");
		mailService.addBMail(5,"订单取消", content1, order.getCustomerId(), customer.getNickname());
		
        //移除定时任务
        try {
			Thread.sleep(1000);
			quartzManager.removeJob(jobName, "UPDATE_ORDER_JOB_GROUP", "OT_"+ jobName, "UPDATE_ORDER_TRIGGER_GROUP");   
			System.out.println("移除订单取消任务jobName======"+jobName);
        } catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
