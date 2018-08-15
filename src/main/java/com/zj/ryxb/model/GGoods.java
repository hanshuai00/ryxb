package com.zj.ryxb.model;

import java.io.Serializable;

public class GGoods implements Serializable {
	
    private Long id;
    /**
     * 商品编码
     */
    private String goodsNum;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品图片名称
     */
    private String goodsImage;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 搜索关键字
     */
    private String searchFlag;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品分类id
     */
    private Long catalogueId;
    /**
     * 商品分类名称
     */
    private String catalogue;
    /**
     * 商品价格 （单位：元）
     */
    private Double price;
    /**
     * 最小购买单元
     */
    private Double minLimit;
    /**
     * 单位
     */
    private String unit;
    /**
     * 剩余数量
     */
    private Integer restQuantity;
    /**
     * 开奖进度（百分制）
     */
    private Double progress;
    /**
     * 默认开奖周期
     */
    private Integer turnaround;
    /**
     * 当前开奖期数
     */
    private Integer issue;
    /**
     * 总开奖期数
     */
    private Integer totalIssue;
    /**
     * 商品状态 0下架1上架2待开奖
     */
    private Integer status;
    /**
     * 开奖时间
     */
    private String saleTime;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否有效，0无效1有效
     */
    private Integer enabled;
    /**
     * 开奖规则，1随机中奖2设定中奖金额3指定中奖
     */
    private Integer rule;
    /**
     * 中奖金额下限
     */
    private Double amountLimit;

    private Long winnerId;
    /**
     * 是否置顶(首页轮播图),0否1是,默认0，最多6个
     */
    private Integer isTop;
    /**
     * 首页轮播图片名
     */
    private String homeImage;

    private String createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum == null ? null : goodsNum.trim();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag == null ? null : searchFlag.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(Long catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue == null ? null : catalogue.trim();
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getRestQuantity() {
        return restQuantity;
    }

    public void setRestQuantity(Integer restQuantity) {
        this.restQuantity = restQuantity;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Integer getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(Integer turnaround) {
        this.turnaround = turnaround;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime == null ? null : saleTime.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getRule() {
        return rule;
    }

    public void setRule(Integer rule) {
        this.rule = rule;
    }

    public Double getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(Double amountLimit) {
        this.amountLimit = amountLimit;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public String getHomeImage() {
		return homeImage;
	}

	public void setHomeImage(String homeImage) {
		this.homeImage = homeImage;
	}

	public Integer getTotalIssue() {
		return totalIssue;
	}

	public void setTotalIssue(Integer totalIssue) {
		this.totalIssue = totalIssue;
	}
	
}