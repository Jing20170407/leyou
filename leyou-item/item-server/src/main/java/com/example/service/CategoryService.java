package com.example.service;

import com.example.mapper.CategoryMapper;
import com.example.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getList(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    public List<Category> getCategoryByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        List<Category> categories = categoryMapper.selectByIdList(ids);
        return categories;
    }
}
