package com.example.api;

import com.example.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @PostMapping("category")
    List<Category> getCategoryByIds(@RequestParam("ids") List<Long> ids);
}
