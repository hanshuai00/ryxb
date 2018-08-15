package com.zj.ryxb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.dao.BMailMapper;
import com.zj.ryxb.model.BMail;
/**
 * 站内信
 * @author zxf
 *
 */
@Service
public class BMailService {
	@Autowired
	private BMailMapper mailMapper;
	
	/**
	 * 添加站内信  默认是系统管理员发送
	 * @param type 消息类型，1系统公告 2中奖提示 3发货通知 4夺宝动态
	 * @param title 标题
	 * @param content 内容
	 * @param recipient 接收人ID
	 * @param recipientName 接受人昵称
	 * @return
	 */
	public int addBMail(int type,String title,String content,
			Long recipient,String recipientName){
		BMail  record = new BMail();
		record.setMessageType(type);
		record.setTitle(title);
		record.setContent(content);
		record.setRecipient(recipient);
		record.setRecipientName(recipientName);
		record.setSender(Long.valueOf(1));//
		record.setSenderName("admin");   //
		record.setStatus(1);//发送状态 0草稿1已发送
		record.setReadStatus(0);//阅读状态 0未阅读1已阅读
		
		return this.saveOrUpdate(record);
	}
	
	/**
	 * 新增、修改——站内信数据
	 * @Title: saveOrUpdate 
	 * @param record
	 * @return
	 */
	@Transactional
	public int saveOrUpdate(BMail  record){
		int m=0;
		if(record.getId() ==null){//新增
			record.setEnabled(1);//0无效1有效
			record.setCreateTime(CalendarUtil.getLongFormatDate());
			
			m = mailMapper.insert(record);
		}else{
			m = mailMapper.updateByPrimaryKeySelective(record);
		}
		
		return m ;
	}
	
	
	/**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    public List<BMail> queryListByPage(Map<String,Object> map){
    	return mailMapper.queryListByPage(map);
    }
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    public int findTotalCount(Map<String,Object> map){
    	return mailMapper.findTotalCount(map);
    }
    /**
     * 根据消息类型查询数据
     * @param messageType 消息类型 1系统公告 2中奖提示 3发货通知 4夺宝动态
     * @param count 查询数量
     * @return
     */
    public List<BMail> queryListByType(Integer messageType, Integer count){
    	return mailMapper.queryListByType(messageType,count);
    }
    
}
