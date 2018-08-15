package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.ryxb.dao.MCartMapper;
import com.zj.ryxb.model.MCart;

@Service
public class CartService{

	@Autowired
	MCartMapper cartMapper;
	
	public int insert(MCart record){
		return cartMapper.insert(record);
	}
	
	public int delete(Long id){
		return cartMapper.deleteByPrimaryKey(id);
	}
	
	public List<MCart> selectByCustomerID(Long customerId){
		return cartMapper.selectByCustomerID(customerId);
	}

	public List<MCart> selectByMap(Map<String,Object> map){
		return cartMapper.selectByMap(map);
	}
	
	public int updateByPrimaryKey(MCart record){
		return cartMapper.updateByPrimaryKeySelective(record);
	}
	

	
}
