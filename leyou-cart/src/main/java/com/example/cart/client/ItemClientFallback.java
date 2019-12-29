package com.example.cart.client;

import com.example.pojo.Sku;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemClientFallback implements ItemClient {
    @Override
    public List<Sku> getSkuBySpuId(Long spuId) {
        return null;
    }

    @Override
    public Sku getSkuById(Long id) {
        return null;
    }
}
