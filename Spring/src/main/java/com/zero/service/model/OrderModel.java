package com.zero.service.model;

import java.math.BigDecimal;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 20:58
 * @since: jdk 1.8
 */
//用户下单交易模型
public class OrderModel {

    private String id;//单号
    private Integer userId;//购买的用户id
    private Integer itemId;//购买的商品id
    private Integer amount;//购买的数量
    private BigDecimal itemPrice;//购买商品的单价
    private BigDecimal orderAmount;//购买的金额
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
