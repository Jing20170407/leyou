package com.example.service;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import com.example.mapper.*;
import com.example.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuService skuService;

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

    public String validateNotId(Spu spu) {
        //验证字段
        if (CollectionUtils.isEmpty(spu.getSkus()) || spu.getSpuDetail() == null) {
            return "skus和spuDetail不能为空";
        }
        //验证spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        String description = spuDetail.getDescription();
        if (StringUtils.isBlank(description)) {
            return "spuDetail description不能为空";
        }
        String specialSpec = spuDetail.getSpecialSpec();
        if (StringUtils.isBlank(specialSpec)) {
            return "spuDetail specialSpec不能为空";
        }
        //验证skus
        boolean b = spu.getSkus().stream().anyMatch(sku -> StringUtils.isBlank(sku.getTitle()));
        if (b) {
            return "sku title 不能为空";
        }
        b = spu.getSkus().stream().anyMatch(sku -> sku.getStock() == null);
        if (b) {
            return "sku stock 不能为空";
        }
        return null;
    }

    @Transactional
    public String addSpu(Spu spu) {
        //验证
        String s = validateNotId(spu);
        if (s != null) {
            return s;
        }
        //保存spu
        LocalDateTime now = LocalDateTime.now();
        spu.setCreateTime(now.toString());
        spu.setLastUpdateTime(now.toString());
        spuMapper.insertSelective(spu);

        //获得spuId，保存spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuDetail);

        //获得spuId，保存sku和stock
        spu.getSkus().forEach(sku -> {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(now);
            sku.setLastUpdateTime(now);
            skuMapper.insertSelective(sku);
        });

        spu.getSkus().forEach(sku -> {
            Stock stock = new Stock();
            stock.setStock(sku.getStock());
            stock.setSkuId(sku.getId());
            stockMapper.insertSelective(stock);
        });

        return null;
    }

    @Transactional
    public String updateSpu(Spu spu) {
        //验证
        String s = validateNotId(spu);
        if (s != null) {
            return s;
        }
        //更新spu和spuDetail
        spu.setLastUpdateTime(LocalDateTime.now().toString());
        spuMapper.updateByPrimaryKeySelective(spu);
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        //删除旧sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        List<Long> skuIds = skus.stream().map(x -> x.getId()).collect(Collectors.toList());
        skuService.delectSkus(skuIds);
        //，保存新sku
        List<Sku> skux = spu.getSkus();
        Long spuId = spu.getId();
        skuService.addSkus(skux,spuId);

        return null;
    }

    public Spu getSpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        return spu;
    }
}
