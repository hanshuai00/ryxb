package com.zj.ryxb.dao;

import com.zj.ryxb.model.SRoles;

public interface SRolesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SRoles record);

    int insertSelective(SRoles record);

    SRoles selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SRoles record);

    int updateByPrimaryKey(SRoles record);
}