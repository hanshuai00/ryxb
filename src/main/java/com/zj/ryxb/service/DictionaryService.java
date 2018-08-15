package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.dao.BDictionaryMapper;
import com.zj.ryxb.model.BDictionary;

/**
 * 数据字典服务类
 * @author zxf
 *
 */
@Service
public class DictionaryService {

	@Autowired
	private BDictionaryMapper dictionaryMapper;
	
	/**
	 * 新增、修改数据
	* @param record
	* @return
	 */
	@Transactional
	public int saveOrUpdate(BDictionary  record){
		int m=0;
		if(record.getId() ==null){//新增
			record.setEnabled(1);//0无效1有效
			m = dictionaryMapper.insertSelective(record);
		}else{
			m = dictionaryMapper.updateByPrimaryKeySelective(record);
		}
		
		return m;
	}
	/**
	 * 查询数据字典所有数据
	 * @return
	 */
	public List<BDictionary> findAllDictionary(){
		List<BDictionary> list=dictionaryMapper.findAllDictionary();
		return list;
	}
	/**
	 * 查询数据字典的一级数据（类型）
	* @Title: findParentList 
	* @return
	 */
	public List<BDictionary> findParentList(){
		List<BDictionary> list=dictionaryMapper.findParentList();
		return list;
	}
	
	/**
	 * 根据父类code获取子类集合
	 * @Title: findByParentCode 
	 * @param parentCode 父类code
	 * @return
	 */
	public List<BDictionary> findByParentCode(String parentCode){
		List<BDictionary> results=null;
		if(!StringUtils.isEmpty(parentCode)){
			results = this.dictionaryMapper.findByParentCode(parentCode);
		}
		
		return results;
	}
	
	/**
	 * 根据父类code，查询父类对象
	 * @param dicCode
	 * @return
	 */
	public BDictionary findParent(String parentCode){
		BDictionary  result= this.dictionaryMapper.findParent(parentCode);	
		return result;
	}
	
	
	/**
	 * 根据父类code，及子类code获取子类value值
	 * @param parentCode  父类code
	 * @param dicCode   子类code
	 * @return
	 */
	public  String getDicValueByCode(String parentCode, String dicCode){
		String  dicValue ="";
		List<BDictionary>  list = this.dictionaryMapper.findByParentCode(parentCode);
		if(list !=null && list.size()>0){
			for(BDictionary dic :list){
				if(dicCode.equalsIgnoreCase(dic.getDicCode())){
					dicValue= dic.getDicValue();
					break;
				}
			}
		}
		return dicValue;
	}
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    public List<BDictionary> queryListByPage(Map<String,Object> map){
    	return dictionaryMapper.queryListByPage(map);
    }
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return dictionaryMapper.findTotalCount(map);
    }
    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Long id){
    	return dictionaryMapper.deleteByPrimaryKey(id);
    }
	/**
	 * 根据父类id删除所有子信息
	 * @param parentId
	 * @return
	 */
    public int deleteByParentId(Long parentId){
    	return dictionaryMapper.deleteByParentId(parentId);
    }
	
}
