package com.zj.ryxb.model;

import java.io.Serializable;
/**
 * 订单详情
 * @author zxf
 *
 */
public class MOrderDetail implements Serializable {
    private Long id;
    /**
     * 订单流水号
     */
    private String orderNum;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 商品id
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
     * 商品价格
     */
    private Double price;
    /**
     * 最小购买单元
     */
    private Double minLimit;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 当前开奖期数
     */
    private Integer issue;
    /**
     * 订单详情状态,1未开奖2待开奖 3未中奖4已中奖 默认1
     */
    private Integer status;

    private Integer enabled;

    private String createTime;
    
    //--- 非数据库存储，用于移动端展示  begin
    private MOrderWin orderWin; //中奖记录
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public MOrderWin getOrderWin() {
		return orderWin;
	}

	public void setOrderWin(MOrderWin orderWin) {
		this.orderWin = orderWin;
	}
	
}