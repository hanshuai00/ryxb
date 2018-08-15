package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zj.ryxb.model.CBonus;

@Repository
public interface CBonusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CBonus record);

    int insertSelective(CBonus record);

    CBonus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CBonus record);

    int updateByPrimaryKey(CBonus record);
    
    /**
     * map条件查询客户红包集合
    * @Title: queryBBonusList 
    * @param map
    * @return
     */
    public List<CBonus> queryCBonusList(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map);
    /**
	 * 更新客户红包状态(失效状态)
	 * @param map
	 * @return
	 */
    public int updateStatus(Map<String,Object> map);
}