package com.example.service;

import com.example.mapper.SpecParamMapper;
import com.example.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecParam> getSpecGroupByGid(Long gid) {
        if (gid == null) {
            return null;
        }
        SpecParam specParam = new SpecParam();
        specParam.setGid(gid);
        return specParamMapper.select(specParam);
    }
}
