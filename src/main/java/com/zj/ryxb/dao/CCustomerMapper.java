package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.GCatalogue;

public interface CCustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CCustomer record);

    int insertSelective(CCustomer record);

    CCustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CCustomer record);

    int updateByPrimaryKey(CCustomer record);
    
    CCustomer loginIn(Map<String,Object> map);
    
    CCustomer findCustomerByName(String userName);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<CCustomer> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    
    /**
     * 通过detail查询用户列表
     * @param map
     * @return
     */
    List<CCustomer> selectByOderDetail(Map<String,Object> map);
    
}