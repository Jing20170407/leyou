package com.example.test;

import com.example.service.CategoryService;
import com.exmaple.pojo.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCategoryService {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test1() {
        List<Category> list = categoryService.getList((long) 0);
        list.forEach(c -> {
            System.out.println(c.getIsParent());
        });

    }

}
