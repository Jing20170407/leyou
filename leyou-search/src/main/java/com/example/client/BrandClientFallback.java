package com.example.client;

import com.example.pojo.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandClientFallback implements BrandClient {
    @Override
    public Brand getBrand(Long id) {
        Brand brand = new Brand();
        brand.setName("异常");
        return brand;
    }
}
