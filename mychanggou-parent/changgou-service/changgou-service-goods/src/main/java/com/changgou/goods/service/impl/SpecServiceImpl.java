package com.changgou.goods.service.impl;

import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.CategoryService;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName SpecServiceImpl
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:16
 * @Version 1.0
 **/

@Service
public class SpecServiceImpl implements SpecService {

    private final SpecMapper specMapper;
    private final TemplateMapper templateMapper;
    private final CategoryService categoryService;

    @Autowired
    public SpecServiceImpl(SpecMapper specMapper, TemplateMapper templateMapper, CategoryService categoryService) {
        this.specMapper = specMapper;
        this.templateMapper = templateMapper;
        this.categoryService = categoryService;
    }

    /**
     * Spec条件+分页查询
     *
     * @param spec 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spec> findPage(Spec spec, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(spec);
        //执行搜索
        return new PageInfo<Spec>(specMapper.selectByExample(example));
    }

    /**
     * Spec分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spec> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Spec>(specMapper.selectAll());
    }

    /**
     * Spec条件查询
     *
     * @param spec
     * @return
     */
    @Override
    public List<Spec> findList(Spec spec) {
        //构建查询条件
        Example example = createExample(spec);
        //根据构建的条件查询数据
        return specMapper.selectByExample(example);
    }


    /**
     * Spec构建查询对象
     *
     * @param spec
     * @return
     */
    public Example createExample(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (spec != null) {
            // ID
            if (!StringUtils.isEmpty(spec.getId())) {
                criteria.andEqualTo("id", spec.getId());
            }
            // 名称
            if (!StringUtils.isEmpty(spec.getName())) {
                criteria.andLike("name", "%" + spec.getName() + "%");
            }
            // 规格选项
            if (!StringUtils.isEmpty(spec.getOptions())) {
                criteria.andEqualTo("options", spec.getOptions());
            }
            // 排序
            if (!StringUtils.isEmpty(spec.getSeq())) {
                criteria.andEqualTo("seq", spec.getSeq());
            }
            // 模板ID
            if (!StringUtils.isEmpty(spec.getTemplateId())) {
                criteria.andEqualTo("templateId", spec.getTemplateId());
            }
        }
        return example;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //查询模板
        Spec spec = specMapper.selectByPrimaryKey(id);
        //变更模板数量
        updateSpecNum(spec, -1);

        //删除指定规格
        specMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spec
     *
     * @param spec
     */
    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKey(spec);
    }

    /**
     * 增加Spec
     *
     * @param spec
     */
    @Override
    public void add(Spec spec) {
        specMapper.insert(spec);
        //变更模板数量
        updateSpecNum(spec, 1);
    }

    /**
     * 根据ID查询Spec
     *
     * @param id
     * @return
     */
    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spec全部数据
     *
     * @return
     */
    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    @Override
    public List<Spec> findByCategoryId(int categoryId) {
        Category category = categoryService.findById(categoryId);
        Integer templateId = category.getTemplateId();
        Spec spec = new Spec();
        spec.setTemplateId(templateId);
        List<Spec> select = specMapper.select(spec);
        return select;
    }


    /**
     * 修改模板统计数据
     *
     * @param spec:操作的模板
     * @param count:变更的数量
     */
    public void updateSpecNum(Spec spec, int count) {
        //修改模板数量统计
        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }
}