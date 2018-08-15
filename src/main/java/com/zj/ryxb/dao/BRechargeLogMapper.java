package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.BRechargeLog;
/**
 * 会员充值记录服务
 * @author zxf
 *
 */
public interface BRechargeLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BRechargeLog record);

    int insertSelective(BRechargeLog record);

    BRechargeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BRechargeLog record);

    int updateByPrimaryKey(BRechargeLog record);
    /**
     * 根据充值流水号查询充值记录
     * @param rechargeNum
     * @return
     */
    public BRechargeLog findByRechargeNum(String rechargeNum);
    
    /**
     * 根据map条件分页查询数据列表
     * @param map
     * @return
     */
    List<BRechargeLog> queryListByPage(Map<String,Object> map);
    /**
     * 根据map条件获取数据总条数
     * @param map
     * @return
     */
    int findTotalCount(Map<String,Object> map);
}