package com.example.api;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface SpuApi {

    //@GetMapping(value = "/spu/page",produces = "application/x-www-form-urlencoded;charset=utf-8")
    @GetMapping("spu/page")
    PageResult<SpuBo> getSpuPage(
            @RequestParam(value = "key", required = false) String search,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

}
