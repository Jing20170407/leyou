package com.example.dto;

import com.example.pojo.Sku;

public class SkuDTO extends Sku {
    private Integer stock;

    @Override
    public Integer getStock() {
        return stock;
    }

    @Override
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "SkuDTO{" +
                "sku=" + super.toString() +
                "stock=" + stock +
                '}';
    }
}
