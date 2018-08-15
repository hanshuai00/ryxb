package com.zj.ryxb.model;

import java.io.Serializable;
/**
 * 客户红包数据
 * @author zxf
 *
 */
public class CBonus implements Serializable {
    private Long id;

    private Long customerId;

    private Long bonusId;
    /**
     * 红包名称
     */
    private String bonusName;
    /**
     * 红包金额
     */
    private Double amount;
    /**
     * 红包使用状态，0未使用1已使用2已失效
     */
    private Integer status;
    /**
     * 截至日期
     */
    private String expiryDate;
    /**
     * 是否有效，0无效1有效
     */
    private Integer enabled;

    private String createTime;
    
    
    //java对象自有字段 非数据库存储   begin
    private String description; //红包描述
    private String useCondition; //红包使用条件
    private String useDate; //红包有效期
    
    //java对象自有字段 非数据库存储   end

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

    public Long getBonusId() {
        return bonusId;
    }

    public void setBonusId(Long bonusId) {
        this.bonusId = bonusId;
    }

    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName == null ? null : bonusName.trim();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUseCondition() {
		return useCondition;
	}

	public void setUseCondition(String useCondition) {
		this.useCondition = useCondition;
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}
}