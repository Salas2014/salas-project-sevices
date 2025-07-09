package com.salas.productservice.dto;

import java.math.BigDecimal;

public class CreateProductEvent {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer count;

    public CreateProductEvent(String productId, String title, BigDecimal price, Integer count) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.count = count;
    }

    public CreateProductEvent() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
