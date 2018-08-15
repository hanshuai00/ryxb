package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.model.MOrderWin;

@Repository
public interface BDictionaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BDictionary record);

    int insertSelective(BDictionary record);

    BDictionary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BDictionary record);

    int updateByPrimaryKey(BDictionary record);
    
	/**
	 * (查询所有的数据字典)
	 * @Title: findAllDictionary 
	 */
	List<BDictionary> findAllDictionary();
	
	/**
	 * @Title: findByCode 
	 * (根据父类字典编码得到子类的List)
	 * @param dicCode
	 * @return
	 */
	List<BDictionary> findByParentCode(@Param(value="dicCode")String dicCode);
	
	/**
	 * 根据父类code，查询父类对象
	 * @param dicCode
	 * @return
	 */
	public BDictionary findParent(@Param(value="dicCode")String dicCode);
	
	/**
	 * (查询数据字典的一级数据（类型）)
	 * @Title: findParent
	 * @return
	 */
	List<BDictionary> findParentList();
	/**
	 * @Title: find 
	 * (根据父类ID查询数据字典)
	 * @param parentId
	 * @return
	 */
	List<BDictionary> findByParentId(@Param(value="pId")Integer parentId);
	
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    public List<BDictionary> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    public int findTotalCount(Map<String,Object> map);
    /**
	 * 根据父类id删除所有子信息
	 * @param parentId
	 * @return
	 */
    public int deleteByParentId(@Param(value="pId")Long parentId);
}