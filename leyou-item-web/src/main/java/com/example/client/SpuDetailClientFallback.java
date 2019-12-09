package com.example.client;

import com.example.pojo.SpuDetail;
import org.springframework.stereotype.Component;

@Component
public class SpuDetailClientFallback implements SpuDetailClient {
    @Override
    public SpuDetail getSpuDetail(Long spuId) {
        System.out.println("SpuDetailClientFallback：请求结果为空，加载默认值！");
        return null;
    }
}
