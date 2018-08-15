package com.zj.ryxb.dao;

import java.util.List;
import com.zj.ryxb.model.SResources;

public interface SResourcesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SResources record);

    int insertSelective(SResources record);

    SResources selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SResources record);

    int updateByPrimaryKey(SResources record);
    
    List<SResources> getMenu(Long id);
}