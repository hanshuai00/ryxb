package com.zj.ryxb.model;

import java.io.Serializable;

public class MOrder implements Serializable {
    private Long id;

    private String orderNum;

    private Long customerId;
    /**
     * 订单总金额
     */
    private Double totalAmount;
    /**
     * 订单总数量
     */
    private Integer totalQuantity;

    private String messageinfo;
    /**
     * 支付宝实际支付金额
     */
    private Double alipayAmount;
    /**
     * 关联客户红包id
     */
    private Long bonusId;
    /**
     * 订单状态：-1付款超时、0未付款、1已付款、2已开奖、3已发货、4已收货9已完成
     */
    private Integer status;

    private Integer enabled;

    private String createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getMessageinfo() {
        return messageinfo;
    }

    public void setMessageinfo(String messageinfo) {
        this.messageinfo = messageinfo == null ? null : messageinfo.trim();
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

	public Long getBonusId() {
		return bonusId;
	}

	public void setBonusId(Long bonusId) {
		this.bonusId = bonusId;
	}

	public Double getAlipayAmount() {
		return alipayAmount;
	}

	public void setAlipayAmount(Double alipayAmount) {
		this.alipayAmount = alipayAmount;
	}
    
    
}