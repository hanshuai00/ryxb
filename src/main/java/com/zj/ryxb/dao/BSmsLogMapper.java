package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.BSmsLog;

public interface BSmsLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BSmsLog record);

    int insertSelective(BSmsLog record);

    BSmsLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BSmsLog record);

    int updateByPrimaryKey(BSmsLog record);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<BSmsLog> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
}