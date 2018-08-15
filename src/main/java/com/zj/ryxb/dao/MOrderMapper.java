package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.MOrder;
import com.zj.ryxb.model.MOrderComment;

public interface MOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MOrder record);

    int insertSelective(MOrder record);

    MOrder selectByPrimaryKey(Long id);
    
    MOrder selectByOrderNum(String orderNum);

    int updateByPrimaryKeySelective(MOrder record);

    int updateByPrimaryKey(MOrder record);
    
    /**
     * 根据客户id查询订单流水号集合
     * @return
     */
    List<String> queryOrderNumByCusId(Long customId);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<MOrder> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
}