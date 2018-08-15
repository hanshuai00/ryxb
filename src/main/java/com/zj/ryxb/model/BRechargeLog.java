package com.zj.ryxb.model;

import java.io.Serializable;

public class BRechargeLog implements Serializable {
    private Long id;
    /**
     * 充值流水号,R+YYMMDDhhmissSSS+用户ID后6位
     */
    private String rechargeNum;
    /**
     * 充值客户id
     */
    private Long customerId;
    /**
     * 充值金额 单位：元
     */
    private Double amount;
    /**
     * 充值方式  1微信2支付宝3系统
     */
    private Integer payType;
    /**
     * 充值状态 0失败1成功
     */
    private Integer status;

    private Integer enabled;

    private String createTime;
    
   //--- 非数据库存储，用于页面展示  begin
    private String userName; //充值会员账号
    private String nickname; //充值会员昵称
    //--- 非数据库存储，用于页面展示  end

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(String rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}