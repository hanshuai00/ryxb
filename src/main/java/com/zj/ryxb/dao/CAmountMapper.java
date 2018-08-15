package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zj.ryxb.model.CAmount;
@Repository
public interface CAmountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CAmount record);

    int insertSelective(CAmount record);

    CAmount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CAmount record);

    int updateByPrimaryKey(CAmount record);
    
    /**
     * 根据map条件查询
    * @Title: queryCAmountList 
    * @param map
    * @return
     */
    public List<CAmount> queryCAmountList(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map);
    
    /**
     * 根据customerId获取最后一条记录
    * @Title: queryCAmountList 
    * @param map
    * @return
     */
    CAmount getAmountByCustomerId(Long customerId);
    /**
     * 统计用户总的充值、消费金额
     * @param customerId
     * @return
     */
    double getTotalAmount(Map<String,Object> map);
}