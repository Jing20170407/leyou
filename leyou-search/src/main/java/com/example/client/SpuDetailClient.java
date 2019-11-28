package com.example.client;

import com.example.api.SpuDetailApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SpuDetailClient extends SpuDetailApi {
}
