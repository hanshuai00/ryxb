package com.zj.ryxb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.zj.ryxb.common.quartz.QuartzManager2;
import com.zj.ryxb.common.sms.CheckBuilder;
import com.zj.ryxb.common.sms.SmsCodeUtil;
import com.zj.ryxb.common.sms.SmsCodeVO;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.TemplateEngine;
import com.zj.ryxb.dao.BSmsLogMapper;
import com.zj.ryxb.dao.MCartMapper;
import com.zj.ryxb.model.BMail;
import com.zj.ryxb.model.BSmsLog;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.model.MOrderWin;



/**
 * 开奖服务类
 * @author han
 *
 */
@Service
public class DrawLotteryService extends SpringBeanAutowiringSupport implements Job {
	@Autowired
	QuartzManager2 quartzManager;
	@Autowired
	private OrderWinService orderWinService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BMailService mailService;
	@Autowired
	MCartMapper cartMapper;
	@Autowired
	private BSmsLogMapper smsLogMapper;

	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
        // The name is defined in the job definition
        String jobName = ((JobDetailImpl)jobDetail).getName();//任务名称
        //String jobName  = (String)jobDetail.getJobDataMap().get("jobName");
        System.out.println("获取开奖任务参数jobName======"+jobName);
        if(jobName==null || jobName==""){
        	System.out.println("---------获取开奖任务名称失败");
        }
        MOrderWin orderWin = orderWinService.selectByPrimaryKey(Long.valueOf(jobName));
        GGoods goods = goodsService.selectByPrimaryKey(orderWin.getGoodsId());
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("goodsId", orderWin.getGoodsId());
        map.put("issue", orderWin.getIssue());
        
        List<MOrderDetail> detailList = orderDetailService.getBuyList(map);
        
        if(detailList ==null || detailList.size()==0){
        	System.out.println("---------未查询到订单信息");
        }
        int rule=1;
        if(goods.getRule()==null){
        	System.out.println("------- 商品开奖规则未设定");
        }
        //开奖方式
        rule = goods.getRule();
        MOrderDetail winOrder = null;
        List<Integer> entry = new ArrayList<Integer>();
        //如果指定中奖人
        if(rule == 3){
        	long winnerId = goods.getWinnerId();
        	//获取指定中奖人参与的订单列表
        	List<MOrderDetail> winnerOrderList = detailList.stream()
        			.filter(d -> d.getCustomerId().equals(winnerId)).collect(Collectors.toList());
        	//如果数量>0取第一个订单中间，如果为0开奖方式改为随机抽奖
        	if(winnerOrderList.size() >0){
        		winOrder = winnerOrderList.get(0);
        	}else{
        		rule =1;
        	}
        }
        //如果设定最低中奖金额
        if(rule == 2){
        	Double amountLimit = goods.getAmountLimit();
        	//按用户进行购买人次汇总
        	Map<Long, Integer> amountLimitMap = detailList.stream().collect(Collectors.groupingBy(MOrderDetail::getCustomerId,Collectors.summingInt(MOrderDetail::getQuantity)));

        	//过滤掉购买人次小于最低中奖金额
        	Map<Long, Integer> amountLimitMap1 = amountLimitMap.entrySet().stream()
        			.filter(p -> p.getValue() >= amountLimit)
        			.collect(Collectors.toMap(p -> p.getKey() ,p -> p.getValue()));

        	//获取符合的用户的订单
        	List<MOrderDetail> amountLimitList = detailList.stream()
        			.filter(d -> amountLimitMap1.containsKey(d.getCustomerId()) ).collect(Collectors.toList());
        	
        	if(amountLimitList.size()>0){
        		Random randomizer = new Random();
        		for(int i=0;i<amountLimitList.size();i++){
            		MOrderDetail orderDetail =amountLimitList.get(i);
            		Integer renci = (int) (orderDetail.getQuantity()/orderDetail.getMinLimit());
            		for(int j=0;j<renci;j++){
            			entry.add(i);
            		}
            	}
        		
        		Integer a = entry.get(randomizer.nextInt(entry.size()));
        		System.out.println("奖池数量" + entry.size());
            	System.out.println("中奖序号" + a);

        		winOrder = amountLimitList.get(a);	
        	}else{
        		rule =1;
        	}
        }
        //随机中奖
        if(rule == 1){
        	Random randomizer = new Random();
        	for(int i=0;i<detailList.size();i++){
        		MOrderDetail orderDetail =detailList.get(i);
        		Integer renci = (int) (orderDetail.getQuantity()/orderDetail.getMinLimit());
        		for(int j=0;j<renci;j++){
        			entry.add(i);
        		}
        	}
        	
        	Integer a = entry.get(randomizer.nextInt(entry.size()));
        	System.out.println("奖池数量" + entry.size());
        	System.out.println("中奖序号" + a);

    		winOrder = detailList.get(a);	
        }
        
        if(winOrder != null){
        	CCustomer customer =customerService.findById(winOrder.getCustomerId());
        	//更新中奖表
    		orderWin.setCustomerId(customer.getId());
    		orderWin.setNickname(customer.getNickname());
    		orderWin.setOrderNum(winOrder.getOrderNum());
    		orderWin.setPersonCount(detailList.size());
    		orderWin.setWinStatus(2);
    		orderWinService.updateByPrimaryKeySelective(orderWin);
    		
    		//更新商品
    		if(goods.getIssue() >= goods.getTotalIssue()){//当商品开奖期数》=商品总期数时，设置商品下架
    			goods.setStatus(0);//商品状态 0下架1上架2待开奖
    		}else{
    			goods.setStatus(1);
    			goods.setIssue(goods.getIssue()+1); //当商品开奖期数<商品总期数时,开奖期数+1
    		}
    		//如果是指定中奖人，开奖后改为随机中奖
    		if(goods.getRule().equals(3)){
    			goods.setRule(1);//开奖规则，1随机中奖2设定中奖金额3指定中奖
    		}
    		goodsService.updateByPrimaryKeySelective(goods);
    		    		
    		//更新订单明细
    		Map<String,Object> updateMap = new HashMap<String,Object>();
    		updateMap.put("status", "3");
    		updateMap.put("goodsId", winOrder.getGoodsId());
    		updateMap.put("issue", winOrder.getIssue());
    		updateMap.put("orderNum", winOrder.getOrderNum());
    		orderDetailService.updateStatus(updateMap);
    		orderDetailService.updateWinner(winOrder.getOrderNum());
    		
    		//保存中奖提示
    		Map<String, String> user = new HashMap<String, String>();
    		user.put("customerName", customer.getNickname());
    		user.put("goodsName", winOrder.getGoodsName());
    		String content = TemplateEngine.readReplaceTemplate(user,"/template/win.tpl");
    		BMail mail = new BMail();
    		mail.setMessageType(2);
    		mail.setTitle("中奖提示");
    		mail.setContent(content);
    		mail.setRecipient(customer.getId());
    		mail.setRecipientName(customer.getNickname());
    		mail.setSender(Long.valueOf(1));//
    		mail.setSenderName("admin");   //
    		mail.setStatus(1);//发送状态 0草稿1已发送
    		mail.setReadStatus(0);//阅读状态 0未阅读1已阅读
    		mail.setGoodsId(winOrder.getGoodsId());//商品ID 当message_type=2时，该字段有值
    		mail.setIssue(winOrder.getIssue());//开奖期数   当message_type=2时，该字段有值
    		mail.setEnabled(1);
    		mail.setCreateTime(CalendarUtil.getLongFormatDate());
    		mailService.saveOrUpdate(mail);
    		
    		//保存中奖公告
    		Map<String, String> user1 = new HashMap<String, String>();
    		user1.put("customerName", customer.getNickname());
    		user1.put("quantity", String.valueOf(winOrder.getQuantity()));
    		user1.put("goodsName", winOrder.getGoodsName());
    		String content1 = TemplateEngine.readReplaceTemplate(user1,"/template/winNotice.tpl");
    		mailService.addBMail(4,"夺宝动态", content1, customer.getId(), customer.getNickname());
    		
    		//发送中奖短信通知
    		Gson gson = new Gson();
    		//String params = "['"+customer.getNickname()+"','"+winOrder.getGoodsName()+"']";
    		try {
    			String result = SmsCodeUtil.sendWinTemplate(CheckBuilder.WIN_NOTICE_TEMPLATE,customer.getUserName(),customer.getNickname(),winOrder.getGoodsName());
				SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
				if(codeVO !=null &&"200".equals(codeVO.code)){
					//保存中奖通知短信
					BSmsLog record = new BSmsLog();
					record.setMessage(content);
					record.setVerificationCode(customer.getUserName());
					record.setRecipient(customer.getId());
					record.setRecipientName(customer.getNickname());
					record.setSendTime(CalendarUtil.getLongFormatDate());
					record.setStatus(1);
					record.setEnabled(1);
					record.setCreateTime(CalendarUtil.getLongFormatDate());
					smsLogMapper.insert(record);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		//删除购物车
    		cartMapper.deleteByGoodsId( winOrder.getGoodsId());	
        }
        //移除定时任务
        try {
			Thread.sleep(1000);
			quartzManager.removeJob(orderWin.getId().toString(), "DRAW_LOTTERY_JOB_GROUP", "OT_"+ Long.valueOf(orderWin.getId()), "DRAW_LOTTERY_TRIGGER_GROUP");   
			System.out.println("移除开奖任务jobName======"+orderWin.getId().toString());
        } catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
