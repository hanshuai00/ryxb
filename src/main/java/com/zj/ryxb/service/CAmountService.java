
package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.CAmountMapper;
import com.zj.ryxb.model.CAmount;

/** 
 * 会员金额操作服务类型
 * @ClassName: CAmountService 
 * @Author： zxf   
 * @Date： 2017年3月28日 下午11:34:37  
 */
@Service
public class CAmountService {
	
	@Autowired
	private CAmountMapper amountMapper;
	
	public void save(CAmount consume){
		/*consume.setCustomerId(customerId);
		consume.setCurAmount(camount.getRemainAmout());
		consume.setActualAmount(totalAmount);
		consume.setRechargeType(2);//1:充值2:消费
		consume.setRemainAmout(camount.getRemainAmout() - totalAmount);
		consume.setEnabled(1);
		consume.setCreateTime(dateTime);*/
	}
	
	/**
     * 根据map条件查询
    * @Title: queryCAmountList 
    * @param map
    * @return
     */
    public List<CAmount> queryCAmountList(Map<String,Object> map){
    	return amountMapper.queryCAmountList(map);
    }
    /**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return amountMapper.findTotalCount(map);
    }
    
    /**
     * 根据customerId获取最后一条记录
    * @Title: queryCAmountList 
    * @param map
    * @return
     */
    public CAmount getAmountByCustomerId(Long customerId){
    	return amountMapper.getAmountByCustomerId(customerId);
    }
}
