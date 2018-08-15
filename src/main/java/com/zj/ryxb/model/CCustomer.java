package com.zj.ryxb.model;

import java.io.Serializable;

public class CCustomer implements Serializable {
    private Long id;

    private String userName;

    private String password;

    private String payPassword;

    private String nickname;

    private String trueName;

    private Integer userGender;
    
    private String birthday;
    /**
     * 用户等级,1普通会员2铜牌会员
     */
    private Integer userLevel;
    /**
     * 用户头像
     */
    private String userImage;

    private Integer enabled;

    private String createTime;
    
    //--- 非数据库存储，用于移动端展示  begin
    public String levelName;
    //public Integer bonusCount;//红包个数(从红包表根据会员id统计查询个数)
    private Double  cusAmount;//会员当前夺宝币金额
    //--- 非数据库存储，用于移动端展示  end

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
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

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Double getCusAmount() {
		return cusAmount;
	}

	public void setCusAmount(Double cusAmount) {
		this.cusAmount = cusAmount;
	}

}