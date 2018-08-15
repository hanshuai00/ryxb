package com.zj.ryxb.model;

import java.io.Serializable;
/**
 * 客户账户金额 操作表
 * @ClassName: CAmount 
 * @Author： zxf   
 * @String： 2017年3月28日 下午11:29:28
 */
public class CAmount implements Serializable {
	
    private Long id;
    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 当前额度
     */
    private Double curAmount;
    /**
     * 实际充值、消费额度
     */
    private Double actualAmount;
    /**
     * 充值消费类型 ， 1:充值2:消费
     */
    private Integer rechargeType;
    /**
     * 剩余额度，
     * 当消费类型是1时，剩余额度=当前额度+充值消费额度
     * 当消费类型是2时，剩余额度=当前额度-充值消费额度
     * 单位：元
     */
    private Double remainAmout;
    /**
     * 0无效1有效
     */
    private Integer enabled;

    private String createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(Double curAmount) {
        this.curAmount = curAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Double getRemainAmout() {
        return remainAmout;
    }

    public void setRemainAmout(Double remainAmout) {
        this.remainAmout = remainAmout;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}