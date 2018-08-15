package com.zj.ryxb.dao;

import com.zj.ryxb.model.MOrderStatus;

public interface MOrderStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MOrderStatus record);

    int insertSelective(MOrderStatus record);

    MOrderStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MOrderStatus record);

    int updateByPrimaryKey(MOrderStatus record);
}