package com.zj.ryxb.model;

import java.io.Serializable;

public class MOrderWin implements Serializable {
    private Long id;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品当前开奖期数
     */
    private Integer issue;
    /**
     * 中奖客户id
     */
    private Long customerId;
    /**
     * 中奖会员客户昵称
     */
    private String nickname;
    /**
     * 订单流水号
     */
    private String orderNum;
    /**
     *本期夺宝人数
     */
    private Integer personCount;
    /**
     * 开奖时间
     */
    private String drawTime;
    /**
     * 开奖状态 1待开奖 2已开奖、3已发货、4已收货9已完成,默认1
     */
    private Integer winStatus;

    private String createTime;

    private Integer enabled;
    /**
     * 是否已晒单,0否1是
     */
    private Integer readyComment;//ready_comment
    
    //--- 非数据库存储，用于移动端展示  begin
    public String userImage; //会员头像名称
    public String goodPrice; //开奖商品价格
    //--- 非数据库存储，用于移动端展示  end
    
    

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage == null ? null : goodsImage.trim();
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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

	public Integer getWinStatus() {
		return winStatus;
	}

	public void setWinStatus(Integer winStatus) {
		this.winStatus = winStatus;
	}

	public Integer getReadyComment() {
		return readyComment;
	}

	public void setReadyComment(Integer readyComment) {
		this.readyComment = readyComment;
	}
}