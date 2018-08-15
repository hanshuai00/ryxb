package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.GGoods;

public interface GGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GGoods record);

    int insertSelective(GGoods record);

    GGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GGoods record);

    int updateByPrimaryKey(GGoods record);
    /**
     * pc 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<GGoods> queryListByPage(Map<String,Object> map);
    /**
     * app客户端 根据条件获取商品(不含未支付订单)
     * @param map
     * @return
     */
    List<GGoods> queryGoodsFromApp(Map<String,Object> map);
    /**
     * 根据条件获取商品(含未支付订单)
     * @param map
     * @return
     */
    List<GGoods> queryGoodsFromAppNew(Map<String,Object> map);
    /**
     * 轮播图设置查询列表 
     * @param map
     * @return
     */
    List<GGoods> queryGoodsLB(Map<String,Object> map);
    
    /**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map);
    /**
     * 获取置顶类型的 商品 （首页轮播图商品）,非下架、有效状态
     * @return
     */
    List<GGoods> queryGoodsIsTop();
    
    /**
     * 获取待开奖商品列表
     */
    List<GGoods> queryDrawLotteryList();
    
    void updateLB();
    
    /**
     * 获取有效商品当前最大排序号
     * @return
     */
    public Integer findCurrentMaxSort();
}