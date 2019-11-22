package com.example.service;

import com.example.mapper.SpuDetailMapper;
import com.example.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpuDetailService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    public SpuDetail getBySpuId(Long spuId) {
        if (spuId == null) {
            return null;
        }
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuId);
        List<SpuDetail> select = spuDetailMapper.select(spuDetail);
        if (CollectionUtils.isEmpty(select)) {
            return null;
        }

        return select.get(0);
    }
}
