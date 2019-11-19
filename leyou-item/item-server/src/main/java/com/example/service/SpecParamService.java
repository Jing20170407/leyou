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
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return specParamMapper.select(specParam);
    }

    public void addSpecGroup(SpecParam specParam) {
        //处理字段numeric，unit，searching，segments
        Boolean numeric = specParam.getNumeric();
        if (numeric == null || numeric == false) {
            specParam.setNumeric(false);
            specParam.setUnit(null);
        }
        Boolean searching = specParam.getSearching();
        if (searching == null || searching == false) {
            specParam.setSearching(false);
            specParam.setSegments(null);
        }
        //添加
        specParamMapper.insertSelective(specParam);
    }

    public boolean delectSpecParam(Long id) {
        if (id == null) {
            return false;
        }
        specParamMapper.deleteByPrimaryKey(id);
        return true;
    }

    public void updateSpecParam(SpecParam specParam) {
        //处理字段numeric，unit，searching，segments
        Boolean numeric = specParam.getNumeric();
        if (numeric == null || numeric == false) {
            specParam.setNumeric(false);
            specParam.setUnit(null);
        }
        Boolean searching = specParam.getSearching();
        if (searching == null || searching == false) {
            specParam.setSearching(false);
            specParam.setSegments(null);
        }
        //更新
        specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    public List<SpecParam> getByCid(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        return specParamMapper.select(specParam);
    }
}
