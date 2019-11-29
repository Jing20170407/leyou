package com.example.client;

import com.example.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = SkuClientFallback.class)
public interface SkuClient extends SkuApi {
}
