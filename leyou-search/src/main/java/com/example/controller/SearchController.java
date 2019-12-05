package com.example.controller;

import com.example.common.PageResult;
import com.example.pojo.Goods;
import com.example.pojo.SearchRequest;
import com.example.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping
    public ResponseEntity<PageResult<Goods>> searchGoods(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> pageResult = searchService.searchGoods(searchRequest);
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }
}
