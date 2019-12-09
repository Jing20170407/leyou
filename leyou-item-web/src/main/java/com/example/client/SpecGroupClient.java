package com.example.client;

import com.example.api.SpecGroupApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = SpecGroupClientFallback.class)
public interface SpecGroupClient extends SpecGroupApi {
}
