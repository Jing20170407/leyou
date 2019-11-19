package com.example.service;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import com.example.mapper.BrandMapper;
import com.example.mapper.CategoryMapper;
import com.example.mapper.SpuMapper;
import com.example.pojo.Brand;
import com.example.pojo.Category;
import com.example.pojo.Spu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public PageResult<SpuBo> findByPage(String search, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        //分页
        PageHelper.startPage(page, rows);
        //添加search条件
        if (StringUtils.isNotBlank(search)) {
            example.createCriteria().andLike("title","%"+search+"%");
        }
        //添加saleable条件
        if (saleable != null) {
            example.createCriteria().andEqualTo("saleable", saleable);
        }
        //查询spu
        List<Spu> spus = spuMapper.selectByExample(example);
        //封装spubo
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            //查询cname
            Category category = categoryMapper.selectByPrimaryKey(spu.getCid3());
            //查询bname
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            //封装spubo
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);

            List<Category> categories = categoryMapper.selectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            List<String> names = categories.stream().map(c -> c.getName()).collect(Collectors.toList());
            spuBo.setCname(StringUtils.join(names,"-"));

            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());
        //封装pageresult
        PageInfo<Spu> info = new PageInfo<>(spus);
        //返回
        return new PageResult<>(spuBos,info.getTotal());
    }
}
