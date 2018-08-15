package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.GCatalogueMapper;
import com.zj.ryxb.model.GCatalogue;

@Service
public class CatalogueService {
	@Autowired
	GCatalogueMapper gCatalogueMapper;
	
	/**
	 * 获取商品分类根分类
	 * @author han
	 */
	public List<GCatalogue> getRootCatalogue(){
		return gCatalogueMapper.getRootCatalogue();
	}
	
	/**
	 * 根据map条件分页查询列表
	 * @param map
	 */
	public List<GCatalogue> queryListByPage(Map<String,Object> map){
		return gCatalogueMapper.queryListByPage(map);
	}
	/**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return gCatalogueMapper.findTotalCount(map);
    }
}
