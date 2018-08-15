package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.MOrderWinMapper;
import com.zj.ryxb.model.MOrderWin;

@Service
public class OrderWinService {

	@Autowired
	private MOrderWinMapper orderWinMapper;
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    public List<MOrderWin> queryListByPage(Map<String,Object> map){
    	return orderWinMapper.queryListByPage(map);
    }
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return orderWinMapper.findTotalCount(map);
    }
    /**
     * 根据map条件分页查询数据列表List<map>(多表级联查询)
     * @param map
     * @return
     */
    public List<Map<Object,Object>> queryListMapByPage(Map<String,Object> map){
    	return orderWinMapper.queryListMapByPage(map);
    }
    /**
     * 根据map条件统计数据总条数 (多表级联查询统计)
     * @param map
     * @return
     */
    public int findListMapTotalCount(Map<String,Object> map){
    	return orderWinMapper.findListMapTotalCount(map);
  
    }
    
    public MOrderWin selectByPrimaryKey(Long id){
    	return orderWinMapper.selectByPrimaryKey(id);
    }
    
    public int updateByPrimaryKeySelective(MOrderWin record){
    	return orderWinMapper.updateByPrimaryKeySelective(record);
    }
    /**
     * 根据商品id和开奖期数查询中奖信息
     * @param map
     * @return
     */
    public MOrderWin selectByGoodidIssue(Map<String,Object> map){
    	return orderWinMapper.selectByGoodidIssue(map);
    }
    
}
