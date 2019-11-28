package com.example.api;

import com.example.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface BrandApi {

    @GetMapping("brand/{id}")
    Brand getBrand(@PathVariable("id") Long id);
}
