package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zj.ryxb.model.BBonus;

@Repository
public interface BBonusMapper {
	
    int deleteByPrimaryKey(Long id);
    /**
     * 新增数据——必须传入所有的参数
    * @Title: insert 
    * @param record
    * @return
     */
    int insert(BBonus record);
    /**
     * 新增数据——可传入部分参数
     * @Title: insertSelective 
     * @param record
     * @return
     */
    int insertSelective(BBonus record);

    BBonus selectByPrimaryKey(Long id);
    /**
     * 修改数据——可传入部分参数
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BBonus record);
    /**
     * 修改数据——必须传入所有的参数
     * @Title: updateByPrimaryKey 
     * @param record
     * @return
     */
    int updateByPrimaryKey(BBonus record);
    
    /**
     * 根据map条件查询红包种类集合
    * @Title: queryBBonusList 
    * @param map
    * @return
     */
    public List<BBonus> queryBBonusList(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map);
    
    
}