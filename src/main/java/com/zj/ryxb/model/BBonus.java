package com.zj.ryxb.model;

import java.io.Serializable;

/**
 * 红包种类表
 * @author zxf
 *
 */
public class BBonus implements Serializable {
	
    private Long id;

    /**
     * 红包名称
     */
    private String bonusName;
    /**
     * 红包描述
     */
    private String description;
    /**
     * 红包金额
     */
    private Double amount;
    /**
     * 使用条件
     */
    private String useCondition;
    /**
     * 有效期限,单位：天
     */
    private Integer  indate;
    /**
     * 红包类型,1:新手红包
     */
    private Integer bonusType;
    /**
     * 是否有效，0无效1有效,默认1
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

    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName == null ? null : bonusName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(String useCondition) {
        this.useCondition = useCondition == null ? null : useCondition.trim();
    }



    public Integer getBonusType() {
        return bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
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

	public Integer getIndate() {
		return indate;
	}

	public void setIndate(Integer indate) {
		this.indate = indate;
	}
}