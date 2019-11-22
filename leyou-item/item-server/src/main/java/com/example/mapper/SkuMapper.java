package com.example.mapper;

import com.example.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SkuMapper extends Mapper<Sku>, DeleteByIdListMapper<Sku, Long>, InsertListMapper<Sku> {
}
