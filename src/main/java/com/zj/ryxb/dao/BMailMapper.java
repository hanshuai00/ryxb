package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zj.ryxb.model.BMail;

@Repository
public interface BMailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BMail record);

    int insertSelective(BMail record);

    BMail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BMail record);

    int updateByPrimaryKey(BMail record);
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<BMail> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
    /**
     * 根据消息类型查询数据
     * @param messageType 消息类型 1系统公告 2中奖提示 3发货通知 4夺宝动态
     * @param count 查询数量
     * @return
     */
    public List<BMail> queryListByType(@Param("messageType")Integer messageType,@Param("count")Integer count);
}