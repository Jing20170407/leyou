package com.example.client;

import com.example.pojo.Sku;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkuClientFallback implements SkuClient {
    @Override
    public List<Sku> getSkuBySpuId(Long skuId) {
        System.out.println("SkuClientFallback：请求结果为空，加载默认值！");
        return null;
    }
}
