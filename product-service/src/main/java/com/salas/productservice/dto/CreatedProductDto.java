package com.salas.productservice.dto;

import java.math.BigDecimal;

public class CreatedProductDto {
    private String title;
    private BigDecimal price;
    private Integer count;

    public CreatedProductDto() {
    }

    public CreatedProductDto(String title, BigDecimal price, Integer count) {
        this.title = title;
        this.price = price;
        this.count = count;
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
