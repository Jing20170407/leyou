package com.example.controller;

import com.example.common.PageResult;
import com.example.service.BrandService;
import com.example.pojo.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        PageResult<Brand> pageResult = brandService.getPage(key, page, rows, sortBy, desc);
        if (pageResult == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("insert")
    public ResponseEntity<Object> addBrand(@Validated Brand brand, BindingResult result, @RequestParam("cids") Long[] cids) {
        if (result.hasErrors()) {
            String msg = result.getFieldError().getField() + ":" + result.getFieldError().getDefaultMessage();
            LOGGER.info("参数验证失败: {}", msg);
            return ResponseEntity.badRequest().body(msg);
        }
        try {
            brandService.addBrand(brand, cids);
        } catch (Exception e) {
            LOGGER.error("添加数据库失败: {}", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.ok().body(brand);
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = brandService.getByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> getBrand(@PathVariable("id") Long id) {
        Brand brand = brandService.getBrand(id);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(brand);
    }
}
