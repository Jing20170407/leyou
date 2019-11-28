package com.example.api;

import com.example.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface SpuDetailApi {
    @GetMapping("spu/detail/{spuId}")
    SpuDetail getSpuDetail(@PathVariable("spuId") Long spuId);
}
