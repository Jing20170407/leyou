package com.example.service;

import com.example.client.*;
import com.example.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SpuDetailClient spuDetailClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecParamClient specParamClient;
    @Autowired
    private SpecGroupClient specGroupClient;

    @Autowired
    private TemplateEngine templateEngine;


    public Map<String, Object> getItemModel(Long spuId) {
        //Spu spu
        Spu spu = spuClient.getSpuById(spuId);
        //SpuDetail spuDetail
        SpuDetail spuDetail = spuDetailClient.getSpuDetail(spuId);
        //List<Category> Categorys
        List<Category> categorys = categoryClient.getCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //Brand brand
        Brand brand = brandClient.getBrand(spu.getBrandId());
        //List<Sku> skus
        List<Sku> skus = skuClient.getSkuBySpuId(spuId);
        //List<SpecParam> specParams
        List<SpecParam> specParams = specParamClient.getSpecParam(null, spu.getCid3());
        //List<SpecGroup> specGroups
        List<SpecGroup> specGroups = specGroupClient.getSpecGroup(spu.getCid3());

        HashMap<String, Object> map = new HashMap<>();
        map.put("spu", spu);
        map.put("spuDetail", spuDetail);
        map.put("categorys", categorys);
        map.put("brand", brand);
        map.put("skus", skus);
        map.put("specParams", specParams);
        map.put("specGroups", specGroups);

        return map;
    }


    /**
     * item.html静态化
     */
    public void savePage(Long spuId){
        Context context = new Context();

        Map<String, Object> itemModel = this.getItemModel(spuId);
        context.setVariables(itemModel);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("D:\\item\\"+spuId+".html"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        templateEngine.process("item", context, writer);
    }

    /**
     * item.html静态化
     */
    public void savePage(Map<String,Object> model){
        Context context = new Context();
        context.setVariables(model);

        //获取spuid
        Spu spu = (Spu)model.get("spu");
        Long spuId = spu.getId();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("D:\\item\\"+spuId+".html"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        templateEngine.process("item", context, writer);
    }
}
