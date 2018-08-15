package com.zj.ryxb.model;

import java.io.Serializable;

public class MOrderParticipant implements Serializable {
    private Long id;

    private Long goodsId;

    private Integer issue;

    private Long customerId;

    private String nickname;

    private String userimage;

    private String lastip;

    private Integer quantity;

    private String createTime;

    private String firstRegion;

    private String seconeRegion;
    
    private String isbought;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage == null ? null : userimage.trim();
    }

    public String getLastip() {
        return lastip;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip == null ? null : lastip.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getFirstRegion() {
        return firstRegion;
    }

    public void setFirstRegion(String firstRegion) {
        this.firstRegion = firstRegion == null ? null : firstRegion.trim();
    }

    public String getSeconeRegion() {
        return seconeRegion;
    }

    public void setSeconeRegion(String seconeRegion) {
        this.seconeRegion = seconeRegion == null ? null : seconeRegion.trim();
    }

	public String getIsbought() {
		return isbought;
	}

	public void setIsbought(String isbought) {
		this.isbought = isbought;
	}
    
    
}