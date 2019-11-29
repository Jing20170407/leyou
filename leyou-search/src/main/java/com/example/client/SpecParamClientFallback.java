package com.example.client;

import com.example.api.SpecParamApi;
import com.example.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecParamClientFallback implements SpecParamClient {
    @Override
    public List<SpecParam> getSpecParam(Long gid, Long cid) {
        System.out.println("SpecParamClientFallback：请求结果为空，加载默认值！");
        return null;
    }
}
