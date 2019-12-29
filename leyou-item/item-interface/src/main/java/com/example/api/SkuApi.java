package com.example.api;

import com.example.pojo.Sku;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SkuApi {

    @GetMapping("sku/list")
    List<Sku> getSkuBySpuId(@RequestParam("id") Long spuId);

    @GetMapping("sku/{id}")
    Sku getSkuById(@PathVariable("id") Long id);
}
