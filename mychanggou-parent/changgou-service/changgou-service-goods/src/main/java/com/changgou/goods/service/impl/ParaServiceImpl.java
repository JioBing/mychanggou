package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName ParaServiceImpl
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:35
 * @Version 1.0
 **/


@Service
public class ParaServiceImpl implements ParaService {

    private final ParaMapper paraMapper;
    private final TemplateMapper templateMapper;
    private final CategoryMapper categoryMapper;
    @Autowired
    public ParaServiceImpl(ParaMapper paraMapper, TemplateMapper templateMapper, CategoryMapper categoryMapper) {
        this.paraMapper = paraMapper;
        this.templateMapper = templateMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Para条件+分页查询
     *
     * @param para 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Para> findPage(Para para, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(para);
        //执行搜索
        return new PageInfo<Para>(paraMapper.selectByExample(example));
    }

    /**
     * Para分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Para>(paraMapper.selectAll());
    }

    /**
     * Para条件查询
     *
     * @param para
     * @return
     */
    @Override
    public List<Para> findList(Para para) {
        //构建查询条件
        Example example = createExample(para);
        //根据构建的条件查询数据
        return paraMapper.selectByExample(example);
    }


    /**
     * Para构建查询对象
     *
     * @param para
     * @return
     */
    public Example createExample(Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if (para != null) {
            // id
            if (!StringUtils.isEmpty(para.getId())) {
                criteria.andEqualTo("id", para.getId());
            }
            // 名称
            if (!StringUtils.isEmpty(para.getName())) {
                criteria.andLike("name", "%" + para.getName() + "%");
            }
            // 选项
            if (!StringUtils.isEmpty(para.getOptions())) {
                criteria.andEqualTo("options", para.getOptions());
            }
            // 排序
            if (!StringUtils.isEmpty(para.getSeq())) {
                criteria.andEqualTo("seq", para.getSeq());
            }
            // 模板ID
            if (!StringUtils.isEmpty(para.getTemplateId())) {
                criteria.andEqualTo("templateId", para.getTemplateId());
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
        //根据ID查询
        Para para = paraMapper.selectByPrimaryKey(id);
        //修改模板统计数据
        updateParaNum(para, -1);

        paraMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Para
     *
     * @param para
     */
    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKey(para);
    }

    /**
     * 增加Para
     *
     * @param para
     */
    @Override
    public void add(Para para) {
        paraMapper.insert(para);

        //修改模板统计数据
        updateParaNum(para, 1);
    }

    /**
     * 根据ID查询Para
     *
     * @param id
     * @return
     */
    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Para全部数据
     *
     * @return
     */
    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    @Override
    public List<Para> findByCategoryId(int categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Integer templateId = category.getTemplateId();
        Para para = new Para();
        para.setTemplateId(templateId);
        List<Para> select = paraMapper.select(para);
        return select;
    }

    /**
     * 修改模板统计数据
     *
     * @param para:操作的参数
     * @param count:变更的数量
     */
    public void updateParaNum(Para para, int count) {
        //修改模板数量统计
        Template template = templateMapper.selectByPrimaryKey(para.getTemplateId());
        template.setParaNum(template.getParaNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }
}
