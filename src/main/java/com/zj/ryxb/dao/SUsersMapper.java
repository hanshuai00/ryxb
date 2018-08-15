package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.SUsers;

public interface SUsersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SUsers record);

    int insertSelective(SUsers record);

    SUsers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SUsers record);

    int updateByPrimaryKey(SUsers record);

	SUsers loginIn(Map<String,Object> map);
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<SUsers> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
}