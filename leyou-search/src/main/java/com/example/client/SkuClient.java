package com.example.client;

import com.example.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SkuClient extends SkuApi {
}
