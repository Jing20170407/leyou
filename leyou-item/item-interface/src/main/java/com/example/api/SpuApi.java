package com.example.api;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import com.example.pojo.Spu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface SpuApi {

    //@GetMapping(value = "/spu/page",produces = "application/x-www-form-urlencoded;charset=utf-8")
    @GetMapping("spu/page")
    PageResult<SpuBo> getSpuPage(
            @RequestParam(value = "key", required = false) String search,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    @GetMapping("/spu/{id}")
    Spu getSpuById(@PathVariable("id") Long id);

}
