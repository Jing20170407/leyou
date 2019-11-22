package com.example.mapper;

import com.example.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock>, DeleteByIdListMapper<Stock,Long> , InsertListMapper<Stock> {
}
