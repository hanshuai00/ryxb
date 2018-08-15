package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.BRechargeLog;
import com.zj.ryxb.model.CAddress;

public interface CAddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CAddress record);

    int insertSelective(CAddress record);

    CAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CAddress record);

    int updateByPrimaryKey(CAddress record);
    /**
     * 根据customerId查询客户地址
     * @param customerId
     * @return
     */
    CAddress selectByCustomerId(Long customerId);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<CAddress> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    
}