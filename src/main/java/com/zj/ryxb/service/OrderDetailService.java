package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.MOrderDetailMapper;
import com.zj.ryxb.dao.MOrderParticipantMapper;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.model.MOrderParticipant;


@Service
public class OrderDetailService {
	@Autowired
	MOrderDetailMapper orderDetailMapper;
	@Autowired
	MOrderParticipantMapper orderParticipantMapper;
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    public List<MOrderDetail> queryListByPage(Map<String,Object> map){
    	return orderDetailMapper.queryListByPage(map);
    }
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return orderDetailMapper.findTotalCount(map);
    }
    
    /**
     * 根据商品id和issue查询数据列表
     * @param map
     * @return
     */
    public List<MOrderDetail> getBuyList(Map<String,Object> map){
    	return orderDetailMapper.getBuyList(map);
    }
    /**
     * 修改订单详情状态
     * @param map
     * @return
     */
    public int updateStatus(Map<String,Object> map){
    	return orderDetailMapper.updateStatus(map);
    }
    /**
     * 根据订单号修改订单详情状态：4已中奖
     * @param orderNum
     * @return
     */
    public int updateWinner(String orderNum){
    	return orderDetailMapper.updateWinner(orderNum);
    }
    /**
     * 商品详情——订单详情数据参与人列表
     * @param map
     * @return
     */
    public List<MOrderParticipant> selectByGoodidIssue(Map<String,Object> map){
    	return orderParticipantMapper.selectByGoodidIssue(map);
    }
    
    public MOrderParticipant selectLastIp(Long customerId){
    	return orderParticipantMapper.selectLastIp(customerId);
    }
}
