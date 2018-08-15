package com.zj.ryxb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.CCustomerMapper;
import com.zj.ryxb.dao.SResourcesMapper;
import com.zj.ryxb.dao.SUsersMapper;
import com.zj.ryxb.model.BRechargeLog;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.SResources;
import com.zj.ryxb.model.SUsers;

@Service
public class UserService{

	@Autowired
	SUsersMapper userMapper;
	@Autowired
	SResourcesMapper resourcesMapper;
	@Autowired
	CCustomerMapper customerMapper;
	
	/**
	 * 用户登录验证
	 * @param map
	 * @return SUsers
	 * 2017-3-12下午2:06:04
	 * @author han
	 * @version 1.0
	 */
	public SUsers loginIn(Map<String,Object> map){
		return userMapper.loginIn(map);
	}
	
	/**
	 * 根据用户id获取菜单
	 * @param id
	 * @return Map<String,List<SResources>>
	 * 2017-3-12下午2:06:04
	 * @author han
	 * @version 1.0
	 */
	@Transactional
	public Map<String,List<SResources>>  getMenu(Long id){
		List<SResources> menulist = resourcesMapper.getMenu(id);
		Map<String,List<SResources>> map = new HashMap<String,List<SResources>>();
		Map<Long,String> first = new HashMap<Long,String>();
		
		for(int i=0;i<menulist.size();i++){
			if(menulist.get(i).getPid() == 0){
				first.put(menulist.get(i).getId(), menulist.get(i).getName());
				map.put(menulist.get(i).getName(), new ArrayList<SResources>());
			}
		}
		
		for(int i=0;i<menulist.size();i++){
			if(menulist.get(i).getPid() > 0){
				map.get(first.get(menulist.get(i).getPid())).add(menulist.get(i));
			}
		}

		return map;
	}
	
	/**
	 * 新增或更新
	 * @param record
	 * @return
	 */
	@Transactional
	public int  saveOrUpdate(SUsers  record){
		int m;
		if(record.getId() ==null){//新增
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			m = userMapper.insert(record);
		}else{
			m = userMapper.updateByPrimaryKeySelective(record);
		}
		
		return m;
	}
	
	/**
	 * 根据map条件分页查询列表
	 * @param map
	 */
	public List<SUsers> queryListByPage(Map<String,Object> map){
		return userMapper.queryListByPage(map);
	}
	/**
     * 根据map条件获取数据总条数
    * @Title: findTotalCount 
    * @param map
    * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return userMapper.findTotalCount(map);
    }
	
}
