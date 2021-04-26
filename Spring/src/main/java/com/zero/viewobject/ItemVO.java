package com.zero.viewobject;

/**
 * @author: Zero
 * @Date: 2021/4/4 - 14:52
 * @since: jdk 1.8
 */
public class ItemVO {
    private Integer id;

    private String title;
    private double price;

    private Integer stock;//商品的库存

    private Integer sales;//商品的销量

    private String description;

    private String picture;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ItemVO{" +
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
