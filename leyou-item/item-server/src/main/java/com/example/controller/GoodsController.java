package com.example.controller;

import com.example.Interface.AddInterface;
import com.example.Interface.UpdateInterface;
import com.example.bo.SpuBo;
import com.example.common.ControllerUtils;
import com.example.common.PageResult;
import com.example.pojo.Sku;
import com.example.pojo.Spu;
import com.example.pojo.SpuDetail;
import com.example.service.SkuService;
import com.example.service.SpuDetailService;
import com.example.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private SkuService skuService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> getSpuPage(
            @RequestParam(value = "key", required = false) String search,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> pageResult = spuService.findByPage(search, saleable, page, rows);
        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("goods")
    public ResponseEntity<Object> addGoods(@Validated(AddInterface.class) @RequestBody Spu spu, BindingResult result) {
        ResponseEntity<Object> bindingResultEntity = ControllerUtils.getBindingResultEntity(result);
        if (bindingResultEntity != null) {
            return bindingResultEntity;
        }
        String msg = spuService.addSpu(spu);
        if (msg != null) {
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetail(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = spuDetailService.getBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> getSkuBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> skus = skuService.getSkuBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(skus);
    }

    @PutMapping("goods")
    public ResponseEntity updateGoods(@Validated(UpdateInterface.class) @RequestBody Spu spu, BindingResult result) {
        ResponseEntity<Object> bindingResultEntity = ControllerUtils.getBindingResultEntity(result);
        if (bindingResultEntity != null) {
            return bindingResultEntity;
        }

        String msg = spuService.updateSpu(spu);
        if (msg != null) {
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable("id") Long id) {
        Spu spu = spuService.getSpuById(id);
        if (spu == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(spu);
    }
}
