package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.CAddressMapper;
import com.zj.ryxb.model.CAddress;

@Service
public class CAddressService {

	@Autowired
	CAddressMapper addressMapper;
	/**
	 * 新增或更新
	 * @param record
	 * @return
	 */
	@Transactional
	public int  saveOrUpdate(CAddress  record){
		int m;
		if(record.getId() == null){//新增
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			m = addressMapper.insert(record);
		}else{
			m = addressMapper.updateByPrimaryKeySelective(record);
		}
		
		return m;
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public  CAddress findCAddressByID(Long id){
		return addressMapper.selectByPrimaryKey(id);
	}
	
	public CAddress selectByCustomerId(Long customerId){
		return addressMapper.selectByCustomerId(customerId);
	}
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
	public List<CAddress> queryListByPage(Map<String,Object> map){
		return addressMapper.queryListByPage(map);
	}
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
	public int findTotalCount(Map<String,Object> map){
		return addressMapper.findTotalCount(map);
	}
}
