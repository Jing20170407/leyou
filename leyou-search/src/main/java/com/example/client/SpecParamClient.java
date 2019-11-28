package com.example.client;

import com.example.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface SpecParamClient extends SpecParamApi {
}
