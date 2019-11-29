package com.example.client;

import com.example.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = SpecParamClientFallback.class)
public interface SpecParamClient extends SpecParamApi {
}
