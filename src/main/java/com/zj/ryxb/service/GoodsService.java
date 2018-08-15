package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.GCatalogueMapper;
import com.zj.ryxb.dao.GGoodsMapper;
import com.zj.ryxb.model.GGoods;

@Service
public class GoodsService{

	@Autowired
	GGoodsMapper gGoodsMapper;
	
	@Autowired
	GCatalogueMapper gCatalogueMapper;
	
	/**
	 * 根据map条件分页查询数据列表
	 * @param map
	 */
	public List<GGoods> queryListByPage(Map<String,Object> map){
		return gGoodsMapper.queryListByPage(map);
	}
	/**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return gGoodsMapper.findTotalCount(map);
    }
    
	/**
	 * 根据条件获取商品列表
	 * @author han
	 */
	public List<GGoods> queryGoodsFromApp(Map<String,Object> map){
		return gGoodsMapper.queryGoodsFromApp(map);
	}
	

	public List<GGoods> queryGoodsLB(Map<String,Object> map){
		return gGoodsMapper.queryGoodsLB(map);
	}

	public GGoods selectByPrimaryKey(Long id){
		return gGoodsMapper.selectByPrimaryKey(id);
	}
	
	public int updateByPrimaryKeySelective(GGoods record){
		return gGoodsMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
     * 获取置顶类型的 商品 （首页轮播图商品）,非下架、有效状态
     * @return
     */
	public List<GGoods> queryGoodsIsTop(){
		return gGoodsMapper.queryGoodsIsTop();
	}
	
	public int insert(GGoods record){
		return gGoodsMapper.insert(record);
	}
	
	public List<GGoods> queryDrawLotteryList(){
		return gGoodsMapper.queryDrawLotteryList();
	}
	
	public void updateLB(){
		gGoodsMapper.updateLB();
	}
	/**
	 * 获取有效商品当前最大排序号
	 * @return
	 */
	public Integer findCurrentMaxSort(){
		return gGoodsMapper.findCurrentMaxSort();
	}
}
