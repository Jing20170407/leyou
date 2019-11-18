package com.example.service;

import com.example.mapper.SpecGroupMapper;
import com.example.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    public List<SpecGroup> getSpecGroupByCid(Long cid) {
        if (cid == null) {
            return null;
        }
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }

    public void addSpecGroup(SpecGroup specGroup) {
        specGroupMapper.insertSelective(specGroup);
    }

    public boolean delectSpecGroup(Long id) {
        if (id == null) {
            return false;
        }

        specGroupMapper.deleteByPrimaryKey(id);
        return true;
    }

    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }
}
