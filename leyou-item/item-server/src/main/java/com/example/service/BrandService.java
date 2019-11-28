package com.example.service;

import com.example.common.PageResult;
import com.example.mapper.BrandMapper;
import com.example.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> getPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        PageHelper.startPage(page, rows);

       /* Example example = new Example.Builder(Brand.class)
                .where(WeekendSqls.<Brand>custom()
                        .andLike(Brand::getName,"%"+key+"%")
                        .orEqualTo(Brand::getLetter,key))
                .orderByDesc(sortBy)
                .build();*/

        Example example = new Example(Brand.class);

        if (StringUtils.isNotBlank(key)) {
            example.createCriteria()
                    .andEqualTo("letter",key)
                    .orLike("name", "%" + key + "%");
        }

        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }


        List<Brand> list = brandMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        PageInfo<Brand> info = new PageInfo<>(list);

        return new PageResult<>(info.getList(), info.getTotal());
    }

    @Transactional
    public void addBrand(Brand brand,Long[] cids) {
        brandMapper.insertSelective(brand);
        Arrays.asList(cids).forEach(cid->brandMapper.addBrandAndCategory(cid,brand.getId()));
    }

    public List<Brand> getByCid(Long cid) {
        if (cid == null) {
            return null;
        }
        //cid查询bid
        ArrayList<Brand> brands = new ArrayList<>();
        List<Long> bids = brandMapper.findByCid(cid);
        bids.forEach(bid->{
            Brand brand = brandMapper.selectByPrimaryKey(bid);
            brands.add(brand);
        });
        return brands;
    }

    public Brand getBrand(Long id) {
        if (id == null) {
            return null;
        }
        Brand brand = brandMapper.selectByPrimaryKey(id);
        return brand;
    }
}
