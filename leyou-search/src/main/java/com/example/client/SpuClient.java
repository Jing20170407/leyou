package com.example.client;

import com.example.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SpuClient extends SpuApi {
}
