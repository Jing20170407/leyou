package com.example.test;

import com.example.common.PageResult;
import com.example.service.BrandService;
import com.exmaple.pojo.Brand;
import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestExample {

    @Autowired
    private BrandService brandService;

    @Test
    public void testExample() {
            PageResult<Brand> page = brandService.getPage(null, 1, 5, "id", true);
        System.out.println(page.getItems());
    }
}
