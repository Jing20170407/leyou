package com.example.controller;

import com.example.service.CategoryService;
import com.example.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> getList(@RequestParam(value = "pid",defaultValue = "0") Long pid) {
        if (pid == null || pid < 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Category> list = categoryService.getList(pid);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }


    @PostMapping()
    public ResponseEntity<List<Category>> getCategoryByIds(@RequestParam("ids") List<Long> ids) {

        List<Category> categories = categoryService.getCategoryByIds(ids);
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }
}
