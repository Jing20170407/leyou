package com.example.cart.client;

import com.example.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-item",fallback = ItemClientFallback.class)
public interface ItemClient extends SkuApi {

}
