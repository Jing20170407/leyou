package com.example.client;

import com.example.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-item")
public interface CategoryClient extends CategoryApi {
}
