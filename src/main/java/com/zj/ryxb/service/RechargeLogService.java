package com.zj.ryxb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.alipay.AlipayConfig;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.BRechargeLogMapper;
import com.zj.ryxb.dao.CAmountMapper;
import com.zj.ryxb.model.BRechargeLog;
import com.zj.ryxb.model.CAmount;

/**
 * 会员充值记录服务
 * @author zxf
 *
 */
@Service
public class RechargeLogService {
	
	@Autowired
    BRechargeLogMapper  rechargeLogMapper;
	
	@Autowired
	CAmountMapper amountMapper;
	
	@Autowired
	CustomerService customerService;

	/**
	 * 新增或更新
	 * @param record
	 * @return
	 */
	@Transactional
	public int  saveOrUpdate(BRechargeLog  record){
		int m;
		if(record.getId() ==null){//新增
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			m = rechargeLogMapper.insert(record);
		}else{
			m = rechargeLogMapper.updateByPrimaryKeySelective(record);
		}
		
		return m;
	}
	
	/**
	 * 支付成功并验证通过，  进行相关业务操作
	 * 修改该充值记录信息状态、新增会员夺宝币当前操作金额记录、统计会员总充值金额修改会员等级
	 * @param recharge
	 * @return
	 */
	@Transactional
	public int  rechargeSuccee(BRechargeLog  recharge){
		int m =0;
		recharge.setStatus(1);//修改支付状态：成功
		int k = this.saveOrUpdate(recharge);//修改支付状态
		
		Long customerId = recharge.getCustomerId();
		//修改新增会员夺宝币金额
		if(k>0){
			//查询当前会员的最后一条记录
			CAmount camount = amountMapper.getAmountByCustomerId(customerId);
			CAmount consume = new CAmount();
			if(camount == null){
				consume.setCurAmount(0.0);
				consume.setRemainAmout(recharge.getAmount());	
			}else{
				consume.setCurAmount(camount.getRemainAmout());
				consume.setRemainAmout(camount.getRemainAmout() +  recharge.getAmount());			
			}
			consume.setCustomerId(customerId);
			consume.setActualAmount(recharge.getAmount());
			consume.setRechargeType(1);//1:充值2:消费
			consume.setEnabled(1);
			consume.setCreateTime(CalendarUtil.getLongFormatDate());	
			int n = amountMapper.insert(consume);
			
			if(n>0){
				//修改会员等级
				Map<String,Object> map = new HashMap<String,Object>();	
				map.put("customerId", customerId);
				map.put("rechargeType", 1);//1:充值2:消费
				map.put("enabled", 1);
				Double totalAmount = amountMapper.getTotalAmount(map);
				m = customerService.updateUserLevel(customerId, totalAmount);
			}
		}
		return m;
	}
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
	public List<BRechargeLog> queryListByPage(Map<String,Object> map){
		return rechargeLogMapper.queryListByPage(map);
	}
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
	public int findTotalCount(Map<String,Object> map){
		return rechargeLogMapper.findTotalCount(map);
    }
}
