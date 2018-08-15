package com.zj.ryxb.dao;

import com.zj.ryxb.model.BLoginLog;

public interface BLoginLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BLoginLog record);

    int insertSelective(BLoginLog record);

    BLoginLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BLoginLog record);

    int updateByPrimaryKey(BLoginLog record);
}