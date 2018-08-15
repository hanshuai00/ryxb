package com.zj.ryxb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.MOrderCommentMapper;
import com.zj.ryxb.model.MOrderComment;

@Service
public class OrderCommentService {
	@Autowired
	private MOrderCommentMapper commentMapper;

	/**
	 * 新增、修改——订单评论数据
	 * @Title: saveOrUpdageCBonus 
	 * @param record
	 * @return
	 */
	@Transactional
	public int saveOrUpdate(MOrderComment  record){
		int m=0;
		if(record.getId() ==null){//新增
			record.setEnabled(1);//0无效1有效
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			
			m = commentMapper.insert(record);
		}else{
			m = commentMapper.updateByPrimaryKeySelective(record);
		}
		
		return m ;
	}
	
	 /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
	public List<MOrderComment> queryListByPage(Map<String,Object> map){
		return commentMapper.queryListByPage(map);
	}
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
	public int findTotalCount(Map<String,Object> map){
		return commentMapper.findTotalCount(map);
	}
	
	/**
     * 根据map条件分页查询数据列表List<map>(多表级联查询)
     * @param map
     * @return
     */
    public List<Map<Object,Object>> queryListMapByPage(Map<String,Object> map){
    	return commentMapper.queryListMapByPage(map);
    }
    /**
     * 根据map条件统计数据总条数 (多表级联查询统计)
     * @param map
     * @return
     */
    public int findListMapTotalCount(Map<String,Object> map){
    	return commentMapper.findListMapTotalCount(map);
  
    }
    
	
}
