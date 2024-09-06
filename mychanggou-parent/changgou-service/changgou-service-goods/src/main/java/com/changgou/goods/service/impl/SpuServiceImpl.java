package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.changgou.goods.vo.GoodsVo;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SpuService
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 17:11
 * @Version 1.0
 **/

@Service
public class SpuServiceImpl implements SpuService {

    private final SpuMapper spuMapper;
    private final SkuMapper skuMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper, SkuMapper skuMapper, CategoryMapper categoryMapper, BrandMapper brandMapper) {
        this.spuMapper = spuMapper;
        this.skuMapper = skuMapper;
        this.categoryMapper = categoryMapper;
        this.brandMapper = brandMapper;
    }

    @Override
    @Transactional
    public void save(GoodsVo goodsVo) {

        /**
         * todo 这里先使用创建实例对象的方式去调用工具类中的方法，因为IdWorker在一个单独的模块中（不能算是一个单独的服务）
         * todo 因此也不能用跨服务调用的方式，教程中直接用@Autowired注入了，应该是比较久的视频教程，我这里不支持，后续知识储备粮上来再考虑解决方案
         */
        IdWorker idWorker = new IdWorker();
        long l = idWorker.nextId();

        // 支持修改操作，根据传入的spuId判断，如果部位null，需要先删除关联数据，再进行重新插入
        Spu spu = goodsVo.getSpu();
        if (!StringUtils.isEmpty(spu.getId())) {
            // 将关联此id的spu、sku数据删除
            spuMapper.deleteByPrimaryKey(spu.getId());
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            List<Sku> skuList = skuMapper.select(sku);
            for (Sku sku1 : skuList) {
                skuMapper.deleteByPrimaryKey(sku1.getId());
            }
        }
        // 初始化spu默认值，然后调用insert方法
        spu.setId(l);
        spu.setIsMarketable("0"); // 是否下架，默认已下架0
        spu.setIsEnableSpec("1"); // 是否启用，默认启用规格
        spu.setIsDelete("0"); // 是否删除，默认未删除
        spu.setStatus("0"); // 审核状态，默认未审核
        // 保存商品通用属性
        spuMapper.insertSelective(spu);

        //增加Sku
        Date date = new Date();
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        List<Sku> skuList = goodsVo.getSkuList();
        for (Sku sku : skuList) {
            //构建SKU名称，采用SPU+规格值组装
            if (StringUtils.isEmpty(sku.getSpec())) {
                sku.setSpec("{}");
            }
            //获取Spu的名字
            String name = sku.getName();
            //将规格转换成Map
            Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
            //循环组装Sku的名字
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                name += "  " + entry.getValue();
            }
            sku.setName(name);
            //ID
            sku.setId(idWorker.nextId());
            //SpuId
            sku.setSpuId(spu.getId());
            //创建日期
            sku.setCreateTime(date);
            //修改日期
            sku.setUpdateTime(date);
            //商品分类ID
            sku.setCategoryId(spu.getCategory3Id());
            //分类名字
            sku.setCategoryName(category.getName());
            //品牌名字
            sku.setBrandName(brand.getName());
            // 保存商品详细信息
            skuMapper.insertSelective(sku);
        }

    }

    @Override
    public GoodsVo findBySpuId(long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setSpu(spu);
        goodsVo.setSkuList(skuList);
        return goodsVo;
    }

}
