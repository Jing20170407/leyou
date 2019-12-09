package com.example.client;

import com.example.pojo.SpecParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecParamClientFallback implements SpecParamClient {
    @Override
    public List<SpecParam> getSpecParam(Long gid, Long cid) {
        System.out.println("SpecParamClientFallback：请求结果为空，加载默认值！");
        return null;
    }
}
