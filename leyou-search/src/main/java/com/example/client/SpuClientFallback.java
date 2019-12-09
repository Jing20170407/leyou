package com.example.client;

import com.example.bo.SpuBo;
import com.example.common.PageResult;
import com.example.pojo.Spu;
import org.springframework.stereotype.Component;

@Component
public class SpuClientFallback implements SpuClient {
    @Override
    public PageResult<SpuBo> getSpuPage(String search, Boolean saleable, Integer page, Integer rows) {
        System.out.println("SpuClientFallback：getSpuPage//请求结果为空，加载默认值！");
        return null;
    }

    @Override
    public Spu getSpuById(Long id) {
        System.out.println("SpuClientFallback：getSpuById//请求结果为空，加载默认值！");
        return null;
    }
}
