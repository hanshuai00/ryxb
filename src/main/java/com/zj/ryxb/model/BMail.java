package com.zj.ryxb.model;

import java.io.Serializable;
/**
 * 站内信
 * @author zxf
 *
 */
public class BMail implements Serializable {
    
	private Long id;
    /**
     * 消息类型，1系统公告 2中奖提示 3发货通知 4夺宝动态5订单取消
     */
    private Integer messageType;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    private Long sender;

    private String senderName;
    /**
     * 接收人ID
     */
    private Long recipient;
    /**
     * 接受人昵称
     */
    private String recipientName;
    /**
     * 商品ID 当message_type=2时，该字段有值
     */
    private Long goodsId;
    /**
     * 当前开奖期数 当message_type=2时，该字段有值
     */
    private Integer issue;
    /**
     * 发送状态 0草稿1已发送，默认0
     */
    private Integer status;
    /**
     * 阅读状态 0未阅读1已阅读，默认0
     */
    private Integer readStatus;

    private Integer enabled;

    private String createTime;
    
    //--- 非数据库存储，用于移动端展示  begin
    public String messTypeName; //站内信类型名称
   //--- 非数据库存储，用于移动端展示  end
    
    

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName == null ? null : senderName.trim();
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName == null ? null : recipientName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
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
}