package com.example.service;

import com.example.mapper.SkuMapper;
import com.example.mapper.StockMapper;
import com.example.pojo.Sku;
import com.example.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuService {
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public List<Sku> getSkuBySpuId(Long skuId) {
        if (skuId == null) {
            return null;
        }
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(skuId);
        List<Sku> skus = skuMapper.select(sku);
        //查询stock并封装
        skus.forEach(s->{
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if (stock != null) {
                s.setStock(stock.getStock());
            }
        });

        return skus;
    }

    public void addSkus(List<Sku> skus, Long spuId) {
        //插入sku
        LocalDateTime now = LocalDateTime.now();
        skus.forEach(s->{
            s.setSpuId(spuId);
            s.setCreateTime(now);
            s.setLastUpdateTime(now);
        });
        skuMapper.insertList(skus);
        //插入stock
        List<Stock> stocks = skus.stream().map(s -> {
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            stock.setStock(s.getStock());
            return stock;
        }).collect(Collectors.toList());

        stockMapper.insertList(stocks);
    }

    public void delectSkus(List<Long> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return;
        }
        skuMapper.deleteByIdList(skuIds);
        stockMapper.deleteByIdList(skuIds);
    }

    public Sku getSkuById(Long id) {
        Sku sku = new Sku();
        sku.setId(id);
        return skuMapper.selectOne(sku);
    }
}
