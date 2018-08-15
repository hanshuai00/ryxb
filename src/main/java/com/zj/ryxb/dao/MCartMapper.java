package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.BSmsLog;
import com.zj.ryxb.model.MCart;

public interface MCartMapper {
    int deleteByPrimaryKey(Long id);
    
    int deleteByGoodsId(Long goodsId);

    int insert(MCart record);

    int insertSelective(MCart record);

    MCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MCart record);

    int updateByPrimaryKey(MCart record);
    
    List<MCart> selectByCustomerID(Long customerId);
    
    List<MCart> selectByMap(Map<String,Object> map);
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<MCart> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
}