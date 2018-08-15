package com.zj.ryxb.model;

import java.io.Serializable;
/**
 * 订单评论
 * @author zxf
 *
 */
public class MOrderComment implements Serializable {
    private Long id;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 当前开奖期数
     */
    private Integer issue;
    /**
     * 订单流水号
     */
    private String orderNum;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 会员客户昵称
     */
    private String nickname;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 评论图片
     */
    private String commentImage;
    /**
     * 点赞数量
     */
    private Integer praiseCount;

    private Integer enabled;

    private String createTime;
    
    //--- 非数据库存储，用于移动端展示  begin
    public String userImage; //会员头像名称
    //--- 非数据库存储，用于移动端展示  end

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
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

	public String getCommentImage() {
		return commentImage;
	}

	public void setCommentImage(String commentImage) {
		this.commentImage = commentImage;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getIssue() {
		return issue;
	}

	public void setIssue(Integer issue) {
		this.issue = issue;
	}
}