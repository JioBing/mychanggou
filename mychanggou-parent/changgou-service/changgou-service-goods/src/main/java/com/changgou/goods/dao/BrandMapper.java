package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BrandMapper extends Mapper<Brand> {

    @Select(value = {"SELECT * FROM tb_brand, tb_category_brand where tb_brand.id = tb_category_brand.brand_id AND category_id = #{categoryId}"})
    List<Brand> findByCategoryId(long categoryId);
}
