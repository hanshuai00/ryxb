package com.zj.ryxb.dao;

import java.util.List;
import java.util.Map;

import com.zj.ryxb.model.MOrderParticipant;

public interface MOrderParticipantMapper {
   /**
    *  查询 商品详情 ——订单详情数据参与人列表 
    * @param map
    * @return
    */
    List<MOrderParticipant> selectByGoodidIssue(Map<String,Object> map);
    /**
     * 
     * @param customerId
     * @return
     */
    MOrderParticipant selectLastIp(Long customerId);
}