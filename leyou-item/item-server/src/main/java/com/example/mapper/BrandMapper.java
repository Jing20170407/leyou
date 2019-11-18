package com.example.mapper;

import com.example.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {


    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
    void addBrandAndCategory(@Param("cid") Long cid,@Param("bid") Long bid);
}
