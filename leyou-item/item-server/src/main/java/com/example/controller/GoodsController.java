package com.example.controller;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import com.example.pojo.Spu;
import com.example.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoodsController {
    @Autowired
    private SpuService spuService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> getSpuPage(
            @RequestParam(value = "key",required = false) String search,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> pageResult = spuService.findByPage(search, saleable, page, rows);
        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("goods")
    public ResponseEntity addGoods(@RequestBody Spu spu) {
        return null;//todo
    }

}
