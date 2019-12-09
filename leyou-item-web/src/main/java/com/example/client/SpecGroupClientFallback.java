package com.example.client;

import com.example.api.SpecGroupApi;
import com.example.pojo.SpecGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecGroupClientFallback implements SpecGroupClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecGroupClientFallback.class);

    @Override
    public List<SpecGroup> getSpecGroup(Long cid) {
        LOGGER.warn("访问远程服务leyou-Item异常");
        return null;
    }
}
