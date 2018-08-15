
 /**
* @Company： 
* @Title: BonusService.java 
* @Description：描述 
* @Author： zxf   
* @Date： 2017年3月23日 下午10:22:05 
* @Version： V1.0      
*/ 
package com.zj.ryxb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.BBonusMapper;
import com.zj.ryxb.dao.CBonusMapper;
import com.zj.ryxb.model.BBonus;
import com.zj.ryxb.model.CBonus;

/** 
 * 红包种类、客户红包服务类
 * @ClassName: BonusService 
 * @Author： zxf   
 * @Date： 2017年3月23日 下午10:22:05  
 */
@Service
public class BonusService {
	/**
	 * 红包种类数据服务
	 */
	@Autowired
	private  BBonusMapper bBonusMapper;
	
	/**
	 * 客户红包数据服务
	 */
	@Autowired
	private  CBonusMapper cBonusMapper;
	
	
	/**
	 * 新增、修改——客户红包数据
	 * @Title: saveOrUpdageCBonus 
	 * @param record
	 * @return
	 */
	@Transactional
	public int saveOrUpdateCBonus(CBonus  record){
		int m=0;
		if(record.getId() ==null){//新增
			record.setEnabled(1);//0无效1有效
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			
			m = cBonusMapper.insertSelective(record);
		}else{
			m = cBonusMapper.updateByPrimaryKeySelective(record);
		}
		
		return m ;
	}
	
	/**
	 * 新增、修改——基础红包数据
	* @Title: saveOrUpdageBBonus 
	* @param record
	* @return
	 */
	@Transactional
	public int saveOrUpdageBBonus(BBonus  record){
		int m=0;
		if(record.getId() ==null){//新增
			record.setEnabled(1);//0无效1有效
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			m = bBonusMapper.insertSelective(record);
		}else{
			m = bBonusMapper.updateByPrimaryKeySelective(record);
		}
		
		return m;
	}
	
	
	/**
     * 根据map条件查询红包种类集合
    * @Title: queryBBonusList 
    * @param map
    * @return
     */
    public List<BBonus> queryBBonusList(Map<String,Object> map){
    	return bBonusMapper.queryBBonusList(map);
    }

    /**
    * map条件查询客户红包集合
   * @Title: queryBBonusList 
   * @param map
   * @return
    */
	public List<CBonus> queryCBonusList(Map<String,Object> map){
		
		return cBonusMapper.queryCBonusList(map);
	}
	/**
     * 根据map条件获取——客户红包数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findCBonusTotalCount(Map<String,Object> map){
    	return cBonusMapper.findTotalCount(map);
    }
    
    /**
     * 根据map条件获取——红包种类数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findBBonusTotalCount(Map<String,Object> map){
    	return bBonusMapper.findTotalCount(map);
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public BBonus selectBBonusById(Long id){
    	return bBonusMapper.selectByPrimaryKey(id);
    }
    /**
     * 根据id删除红包种类——逻辑删除
     * @param id
     * @return
     */
    public int deleteBBonusById(String ids){
    	int m =0;
    	String[] str = ids.split(",");
    	for(int i =0;i<str.length;i++){
    		BBonus record = new BBonus();
    		record.setId(Long.valueOf(str[i]));
    		record.setEnabled(0);
    		m = bBonusMapper.updateByPrimaryKeySelective(record);
    	}
    	return m;
    }
	/**
	 * 更新客户红包失效状态
	 * @param map
	 * @return
	 */
    public int updateStatus(){
    	//System.out.println(CalendarUtil.getLongFormatDate());
    	Map<String,Object> updateMap = new HashMap<String,Object>();
    	updateMap.put("newStatus", 2);
		updateMap.put("oldStatus", 0);
		updateMap.put("expiryDate", CalendarUtil.getLongFormatDate());
    	return cBonusMapper.updateStatus(updateMap);
    }

}
