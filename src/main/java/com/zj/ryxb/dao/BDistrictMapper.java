package com.zj.ryxb.dao;

import java.util.List;

import com.zj.ryxb.model.BDistrict;

public interface BDistrictMapper {
    int insert(BDistrict record);

    int insertSelective(BDistrict record);
    
    List<BDistrict> selectByParentId(Long parentId);
}