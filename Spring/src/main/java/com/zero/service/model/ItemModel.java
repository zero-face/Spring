package com.zero.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 13:32
 * @since: jdk 1.8
 */
public class ItemModel {
    private Integer id;
    @NotBlank(message = "商品名称不能为空")
    private String title;
    @NotNull(message = "价格不能为空")
    @Min(value = 0,message = "商品价格不能小于0")
    private double price;
    @NotNull(message = "商品库存不能为空")
    @Min(value = 0,message = "库存量不能小于0")
    private Integer stock;//商品的库存
    private Integer sales;//商品的销量
    @NotNull(message = "商品描述信息不能为空")
    private String description;
    private String picture;
    @NotNull(message = "商品地址图片URL地址不能为空")
    private String imgUrl;//商品描述图片的URL

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getimgUrl() {
        return imgUrl;
    }

    public void setimgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", sales=" + sales +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
