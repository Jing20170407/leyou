package com.example.mapper;

import com.example.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {


    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
    void addBrandAndCategory(@Param("cid") Long cid,@Param("bid") Long bid);

    @Select("select brand_id from tb_category_brand where category_id=#{cid}")
    List<Long> findByCid(@Param("cid") Long cid);
}
