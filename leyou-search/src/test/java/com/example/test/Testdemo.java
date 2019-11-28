package com.example.test;

import com.example.bo.SpuBo;
import com.example.client.BrandClient;
import com.example.client.SpuClient;
import com.example.common.PageResult;
import com.example.pojo.Goods;
import com.example.repository.GoodsRepository;
import com.example.service.SearchService;
import com.netflix.loadbalancer.ILoadBalancer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Testdemo {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private BrandClient brandClient;

    @Test
    public void test1(){
        /*NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.addAggregation()*/
    }

    @Test
    public void buildMapping(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void importGoods() {
        Integer page = 1;
        Integer rows = 100;
        do {
            PageResult<SpuBo> spuPage = spuClient.getSpuPage(null, null, page, rows);
            if (spuPage != null && !CollectionUtils.isEmpty(spuPage.getItems())) {
                /*List<Goods> goodsList = spuPage.getItems().stream().map(sb->{
                    Spu spu = sb;
                    try {
                        Goods goods = searchService.buildGoods(spu);
                        return goods;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("spu转goods异常");
                    }
                }).collect(Collectors.toList());
                goodsRepository.saveAll(goodsList);
                page++;*/
                System.out.println(page);
                page++;
            }else {
                return;
            }
        } while (true);
    }

    @Test
    public void test2() {
        System.out.println(brandClient.getBrand(2505L));
    }
}
