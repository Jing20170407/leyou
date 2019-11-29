package com.example.client;

import com.example.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = SpuClientFallback.class)
public interface SpuClient extends SpuApi {
}
