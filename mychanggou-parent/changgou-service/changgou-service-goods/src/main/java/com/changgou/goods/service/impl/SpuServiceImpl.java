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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
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

    @Override
    @Transactional
    public void audit(long spuId) {
        //查询商品
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断商品是否已经删除
        if (spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("该商品已经删除！");
        }
        //实现上架和审核
        spu.setStatus("1"); //审核通过
        spu.setIsMarketable("1"); //上架
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    @Transactional
    public void pull(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu.getIsDelete().equals("1")) {
            throw new RuntimeException("此商品已删除！");
        }
        spu.setIsMarketable("0");//下架状态
        spuMapper.updateByPrimaryKeySelective(spu);
//        int i = 10 / 0;
    }

    @Override
    @Transactional
    public int putMany(Long[] idList) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");//上架
        //批量修改
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(idList));//id
        //下架
        criteria.andEqualTo("isMarketable", "0");
        //审核通过的
        criteria.andEqualTo("status", "1");
        //非删除的
        criteria.andEqualTo("isDelete", "0");
        return spuMapper.updateByExampleSelective(spu, example);
    }

    @Override
    @Transactional
    public int pullMany(Long[] idList) {
        Spu spu = new Spu();
        spu.setIsMarketable("0");  // 改为已下架
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(idList));

        //下架
        criteria.andEqualTo("isMarketable", "1");
        //审核通过的
        criteria.andEqualTo("status", "1");
        //非删除的
        criteria.andEqualTo("isDelete", "0");
        int i = spuMapper.updateByExampleSelective(spu, example);
        return i;
    }


    @Override
    @Transactional
    public void logicDelete(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //检查是否下架的商品
        if (!spu.getIsMarketable().equals("0")) {
            throw new RuntimeException("必须先下架再删除！");
        }
        //删除
        spu.setIsDelete("1");
        //未审核
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    @Transactional
    public void restore(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //检查是否删除的商品
        if (!spu.getIsDelete().equals("1")) {
            throw new RuntimeException("此商品未删除！");
        }
        //未删除
        spu.setIsDelete("0");
        //未审核
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public PageInfo findPage(int page, int size, Spu spu) {

        Example example = createExample(spu);
        // 开启分页
        PageHelper.startPage(page, size);
        List<Spu> spuList = spuMapper.selectByExample(example);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spuList);
        return spuPageInfo;
    }


    /**
     * 拼接搜索条件
     *
     * @param spu
     * @return
     */
    public Example createExample(Spu spu) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete", 0);//只找 没有被删除的
        if (spu != null) {
            // 主键
            if (!StringUtils.isEmpty(spu.getId())) {
                criteria.andEqualTo("id", spu.getId());
            }
            // 货号
            if (!StringUtils.isEmpty(spu.getSn())) {
                criteria.andEqualTo("sn", spu.getSn());
            }
            // SPU名
            if (!StringUtils.isEmpty(spu.getName())) {
                criteria.andLike("name", "%" + spu.getName() + "%");
            }
            // 副标题
            if (!StringUtils.isEmpty(spu.getCaption())) {
                criteria.andEqualTo("caption", spu.getCaption());
            }
            // 品牌ID
            if (!StringUtils.isEmpty(spu.getBrandId())) {
                criteria.andEqualTo("brandId", spu.getBrandId());
            }
            // 一级分类
            if (!StringUtils.isEmpty(spu.getCategory1Id())) {
                criteria.andEqualTo("category1Id", spu.getCategory1Id());
            }
            // 二级分类
            if (!StringUtils.isEmpty(spu.getCategory2Id())) {
                criteria.andEqualTo("category2Id", spu.getCategory2Id());
            }
            // 三级分类
            if (!StringUtils.isEmpty(spu.getCategory3Id())) {
                criteria.andEqualTo("category3Id", spu.getCategory3Id());
            }
            // 模板ID
            if (!StringUtils.isEmpty(spu.getTemplateId())) {
                criteria.andEqualTo("templateId", spu.getTemplateId());
            }
            // 运费模板id
            if (!StringUtils.isEmpty(spu.getFreightId())) {
                criteria.andEqualTo("freightId", spu.getFreightId());
            }
            // 图片
            if (!StringUtils.isEmpty(spu.getImage())) {
                criteria.andEqualTo("image", spu.getImage());
            }
            // 图片列表
            if (!StringUtils.isEmpty(spu.getImages())) {
                criteria.andEqualTo("images", spu.getImages());
            }
            // 售后服务
            if (!StringUtils.isEmpty(spu.getSaleService())) {
                criteria.andEqualTo("saleService", spu.getSaleService());
            }
            // 介绍
            if (!StringUtils.isEmpty(spu.getIntroduction())) {
                criteria.andEqualTo("introduction", spu.getIntroduction());
            }
            // 规格列表
            if (!StringUtils.isEmpty(spu.getSpecItems())) {
                criteria.andEqualTo("specItems", spu.getSpecItems());
            }
            // 参数列表
            if (!StringUtils.isEmpty(spu.getParaItems())) {
                criteria.andEqualTo("paraItems", spu.getParaItems());
            }
            // 销量
            if (!StringUtils.isEmpty(spu.getSaleNum())) {
                criteria.andEqualTo("saleNum", spu.getSaleNum());
            }
            // 评论数
            if (!StringUtils.isEmpty(spu.getCommentNum())) {
                criteria.andEqualTo("commentNum", spu.getCommentNum());
            }
            // 是否上架
            if (!StringUtils.isEmpty(spu.getIsMarketable())) {
                criteria.andEqualTo("isMarketable", spu.getIsMarketable());
            }
            // 是否启用规格
            if (!StringUtils.isEmpty(spu.getIsEnableSpec())) {
                criteria.andEqualTo("isEnableSpec", spu.getIsEnableSpec());
            }
            // 是否删除
            if (!StringUtils.isEmpty(spu.getIsDelete())) {
                criteria.andEqualTo("isDelete", spu.getIsDelete());
            }
            // 审核状态
            if (!StringUtils.isEmpty(spu.getStatus())) {
                criteria.andEqualTo("status", spu.getStatus());
            }
        }
        return example;
    }

}
