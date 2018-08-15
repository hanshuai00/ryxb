package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.MOrderComment;

public interface MOrderCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MOrderComment record);

    int insertSelective(MOrderComment record);

    MOrderComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MOrderComment record);

    int updateByPrimaryKey(MOrderComment record);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<MOrderComment> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    
    /**
     * 根据map条件分页查询数据列表List<map>(多表级联查询)
     * @param map
     * @return
     */
    List<Map<Object,Object>> queryListMapByPage(Map<String,Object> map);
    /**
     * 根据map条件统计数据总条数 (多表级联查询统计)
     * @param map
     * @return
     */
    int findListMapTotalCount(Map<String,Object> map);
}