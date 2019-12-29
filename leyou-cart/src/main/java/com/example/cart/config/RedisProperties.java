package com.example.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("leyou.redis")
public class RedisProperties {

    String cartName;

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }
}
