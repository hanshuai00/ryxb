package com.zj.ryxb.model;

import java.io.Serializable;

public class BDictionary implements Serializable {
    private Long id;

    private Long pid;

    private String dicCode;

    private String dicValue;

    private String dicValueEn;

    private Integer orderIndex;

    private Integer enabled;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode == null ? null : dicCode.trim();
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue == null ? null : dicValue.trim();
    }

    public String getDicValueEn() {
        return dicValueEn;
    }

    public void setDicValueEn(String dicValueEn) {
        this.dicValueEn = dicValueEn == null ? null : dicValueEn.trim();
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}