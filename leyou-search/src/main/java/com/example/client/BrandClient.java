package com.example.client;

import com.example.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = BrandClientFallback.class)
public interface BrandClient extends BrandApi {
}
