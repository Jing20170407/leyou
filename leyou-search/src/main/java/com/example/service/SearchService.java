package com.example.service;

import com.example.client.*;
import com.example.common.PageResult;
import com.example.pojo.*;
import com.example.repository.GoodsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);



    public Goods buildGoods(Spu spu) throws IOException {
        //all
        List<Category> categorys = categoryClient.getCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> names = categorys.stream().map(category -> category.getName()).collect(Collectors.toList());
        String categoryNames = StringUtils.join(names, " ");

        Brand brand = brandClient.getBrand(spu.getBrandId());

        String all = spu.getTitle() + " " + categoryNames + " " + brand.getName();
        //prices
        List<Sku> skus = skuClient.getSkuBySpuId(spu.getId());
        List<Long> prices = null;
        if (!CollectionUtils.isEmpty(skus)) {
            prices = skus.stream().map(sku -> sku.getPrice()).collect(Collectors.toList());
        }

        //specs
        SpuDetail spuDetail = spuDetailClient.getSpuDetail(spu.getId());
        List<SpecParam> specParams = specParamClient.getSpecParam(null, spu.getCid3());
        Map<String, Object> specs = new HashMap<>();
        if (spuDetail != null&&!CollectionUtils.isEmpty(specParams)) {
            Map<Long, String> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), MAPPER.getTypeFactory().constructMapType(Map.class, Long.class, String.class));
            Map<Long, List<String>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});

            specParams.forEach(specParam -> {
                Object value = null;
                if (specParam.getGeneric()) {
                    value = genericSpecMap.get(specParam.getId());
                    if (specParam.getNumeric()) {
                        if (value != null) {
                            value = chooseSegment(value.toString(), specParam);
                        }
                    }
                } else {
                    value = specialSpecMap.get(specParam.getId());
                }

                if (value == null) {
                    value = "其它";
                }

                String key = specParam.getName();
                specs.put(key, value);
            });
        }

        //skus
        String skuJson = "";
        if (skus != null) {
            skuJson = MAPPER.writeValueAsString(skus);
        }

        //goods
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
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
            if (StringUtils.isBlank(segment)) {
                break;
            }
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

    public SearchResult searchGoods(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        Integer page = searchRequest.getPage();
        Map<String,Object> filter = searchRequest.getFilter();
        if (page == null || page < 1) {
            page = 1;
        }
        Integer rows = 20;
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder matchQuery = QueryBuilders.boolQuery();
        matchQuery.must(QueryBuilders.matchQuery("all", key).operator(Operator.AND));
        //搜索过滤
        if (!CollectionUtils.isEmpty(filter)) {
            filter.forEach((k,v)->{
                if (StringUtils.equals("分类", k)) {
                    k = "cid3";
                }else if (StringUtils.equals("品牌", k)) {
                    k = "brandId";
                } else {
                    k = "specs." + k + ".keyword";
                }
                matchQuery.filter(QueryBuilders.matchQuery(k, v));
            });
        }
        queryBuilder.withQuery(matchQuery);

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        //分页
        queryBuilder.withPageable(PageRequest.of(page-1, rows));

        // 分类聚合查询
        queryBuilder.addAggregation(AggregationBuilders.terms("分类").field("cid3"));
        // 品牌聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("品牌").field("brandId"));


        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        //分类
        List<Map<String,Object>> categoryAggs = this.categoryAggregatedHandle(search.getAggregation("分类"));
        //品牌
        List<Brand> brandAggs = this.brandAggregatedHandle(search.getAggregation("品牌"));
        // 规格参数聚合
        List<Map<String, Object>> specAggs = null;
        if (categoryAggs != null || categoryAggs.size() == 1) {
            specAggs = this.specParamAggregatedHandle((Long)categoryAggs.get(0).get("id"), matchQuery);
        }

        return new SearchResult(search.getContent(), search.getTotalElements(),
                search.getTotalPages(),page,categoryAggs,brandAggs,specAggs,filter);
    }

    private List<Map<String, Object>> specParamAggregatedHandle(Long cid, QueryBuilder matchQuery) {
        //准备查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(matchQuery);
        List<SpecParam> specParam = specParamClient.getSpecParam(null, cid);
        specParam.stream()
                .filter(s -> s.getSearching())
                .forEach(s -> {
                    queryBuilder.addAggregation(AggregationBuilders.terms(s.getName()).field("specs." + s.getName() + ".keyword"));
                });

        //查询聚合
        AggregatedPage<Goods> search = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());


        //封装数据
        List<Map<String, Object>> specs = new ArrayList<>();
        search.getAggregations().asMap().forEach((k,v)->{
            StringTerms stringTerms = (StringTerms) v;
            String name = k;

            List<Object> collect = stringTerms
                    .getBuckets()
                    .stream()
                    .map(bucket -> bucket.getKey())
                    .collect(Collectors.toList());

            HashMap<String, Object> map = new HashMap<>();
            map.put("key", name);
            map.put("options", collect);
            specs.add(map);
        });

        return specs;
    }

    private List<Brand> brandAggregatedHandle(Aggregation agg) {
        LongTerms longTerms = (LongTerms) agg;
        return longTerms.getBuckets().stream().map(bucket -> {
            return brandClient.getBrand((Long) bucket.getKey());
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> categoryAggregatedHandle(Aggregation agg) {
        LongTerms longTerms = (LongTerms) agg;
        return longTerms.getBuckets().stream().map(bucket -> {
            long cid = bucket.getKeyAsNumber().longValue();
            List<Category> category = categoryClient.getCategoryByIds(Arrays.asList(cid));

            HashMap<String, Object> map = new HashMap<>();
            map.put("id", cid);
            map.put("name", category.get(0).getName());
            return map;
        }).collect(Collectors.toList());
    }
}
