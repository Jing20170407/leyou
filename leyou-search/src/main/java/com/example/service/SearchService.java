package com.example.service;

import com.example.client.*;
import com.example.pojo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecParamClient specParamClient;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    @Autowired
    private SkuClient skuClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);



    public Goods buildGoods(Spu spu) throws IOException {
        //all
        List<Category> categorys = categoryClient.getCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String categoryNames = StringUtils.join(categorys, " ");

        Brand brand = brandClient.getBrand(spu.getBrandId());

        String all = spu.getTitle() + " " + categoryNames + " " + brand.getName();
        //prices
        List<Sku> skus = skuClient.getSkuBySpuId(spu.getId());
        List<Long> prices = skus.stream().map(sku -> sku.getPrice()).collect(Collectors.toList());

        //specs
        List<SpecParam> specParams = specParamClient.getSpecParam(null, spu.getCid3());

        SpuDetail spuDetail = spuDetailClient.getSpuDetail(spu.getId());

        Map<Long, String> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), MAPPER.getTypeFactory().constructMapType(Map.class, Long.class, String.class));
        Map<Long, List<String>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});

        Map<String,Object> specs = new HashMap<>();

        specParams.forEach(specParam -> {
            Object value = null;
            if (specParam.getGeneric()) {
                value = genericSpecMap.get(specParam.getId());
                if (specParam.getNumeric()) {
                    value = chooseSegment(value.toString(), specParam);
                }
            } else {
                value = specialSpecMap.get(specParam.getId());
            }

            if (value == null) {
                value = "其他";
            }

            String key = specParam.getName();
            specs.put(key, value);
        });

        //skus
        String skuJson = skuJson = MAPPER.writeValueAsString(skus);

        //goods
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(LocalDateTime.parse(spu.getCreateTime()));
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(all);
        goods.setPrice(prices);
        goods.setSpecs(specs);
        goods.setSkus(skuJson);
        return goods;
    }

    /**
     * 将规格参数为数值型的参数划分为段
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
