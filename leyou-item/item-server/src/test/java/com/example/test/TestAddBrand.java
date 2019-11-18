package com.example.test;

import com.example.mapper.BrandMapper;
import com.example.pojo.Brand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAddBrand {

    @Autowired
    private BrandMapper brandMapper;

    @Test
    public void addBrandForbackId() {
        Brand brand = new Brand();
        brand.setName("TestBrand");

        brandMapper.insert(brand);

        System.out.println(brand.getId());
    }
}
