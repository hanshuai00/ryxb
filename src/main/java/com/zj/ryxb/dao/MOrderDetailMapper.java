package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zj.ryxb.model.MOrderDetail;

public interface MOrderDetailMapper {
	int deleteByPrimaryKey(Long id);

    int insert(MOrderDetail record);

    int insertSelective(MOrderDetail record);

    MOrderDetail selectByPrimaryKey(Long id);
    
    List<MOrderDetail> queryByOrderNum(String orderNum);

    int updateByPrimaryKeySelective(MOrderDetail record);

    int updateByPrimaryKey(MOrderDetail record);
    
    int batchInsert(List<MOrderDetail> detailList);
    
    /**
     * 根据订单流水号和商品id查询订单 详细
     * @param orderNum
     * @param goodsId
     * @return
     */
    MOrderDetail selectByOrderNum(@Param("orderNum")String orderNum,@Param("goodsId")Long goodsId);
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<MOrderDetail> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    
    /**
     * 根据map条件分页查询List<map>数据列表(多表级联查询)
     * @param map
     * @return
     */
    List<Map<Object,Object>> queryListMapByPage(Map<String,Object> map);
    /**
     * 分页查询List<map>统计数据总条数 (多表级联查询统计)
     * @param map
     * @return
     */
    int findListMapTotalCount(Map<String,Object> map);
    
    
    List<MOrderDetail> getBuyList(Map<String,Object> map);
    
    int updateStatus(Map<String,Object> map);
    
    int updateWinner(@Param("orderNum")String orderNum);
}