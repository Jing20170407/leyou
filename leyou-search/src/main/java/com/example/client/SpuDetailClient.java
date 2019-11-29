package com.example.client;

import com.example.api.SpuDetailApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = SpuDetailClientFallback.class)
public interface SpuDetailClient extends SpuDetailApi {
}
