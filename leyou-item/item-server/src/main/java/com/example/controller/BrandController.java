package com.example.controller;

import com.example.common.PageResult;
import com.example.service.BrandService;
import com.exmaple.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getPage(
        @RequestParam(value = "key",required = false) String key,
        @RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "rows",defaultValue = "5") Integer rows,
        @RequestParam(value = "sortBy",required = false) String sortBy,
        @RequestParam(value = "desc",required = false) Boolean desc
    ) {
        PageResult<Brand> pageResult = brandService.getPage(key, page, rows, sortBy, desc);
        if (pageResult == null) {
            return ResponseEntity.notFound().build();
        }
        //ResponseEntity e = ResponseEntity.ok();
        //ggggggg55555555556666666666666666gggggggg
        //ggggggg000001111111111111100000000gggggggg
        //????
        return ResponseEntity.ok(pageResult);
    }
}
