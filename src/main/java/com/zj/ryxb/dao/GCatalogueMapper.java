package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.GCatalogue;

public interface GCatalogueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GCatalogue record);

    int insertSelective(GCatalogue record);

    GCatalogue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GCatalogue record);

    int updateByPrimaryKey(GCatalogue record);
    
    List<GCatalogue> getRootCatalogue();
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<GCatalogue> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    
}