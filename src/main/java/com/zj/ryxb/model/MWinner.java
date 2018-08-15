package com.zj.ryxb.model;

import java.io.Serializable;

public class MWinner implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

    private String nickname;
    /**
     * 用户头像
     */
    private String userImage;
    
    /**
     *本期夺宝人数
     */
    private Integer personCount;
    /**
     * 开奖时间
     */
    private String drawTime;
    
    private String lastip;
    
    private String firstRegion;

    private String seconeRegion;
    
    /**
     * 订单流水号
     */
    private String orderNum;
    
    /**
     *购买次数
     */
    private Integer purchaseCount;

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

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public Integer getPersonCount() {
		return personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public String getFirstRegion() {
		return firstRegion;
	}

	public void setFirstRegion(String firstRegion) {
		this.firstRegion = firstRegion;
	}

	public String getSeconeRegion() {
		return seconeRegion;
	}

	public void setSeconeRegion(String seconeRegion) {
		this.seconeRegion = seconeRegion;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getPurchaseCount() {
		return purchaseCount;
	}

	public void setPurchaseCount(Integer purchaseCount) {
		this.purchaseCount = purchaseCount;
	}

  
}