package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();

    Brand findById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Integer id);

    List<Brand> findList(Brand brand);

    PageInfo findPage(int page, int pageSize);

    PageInfo findPageList(Brand brand, int page, int pageSize);

    List<Brand> findByCategoryId(long categoryId);
}
