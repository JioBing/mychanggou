package com.changgou.goods.dao;


import com.changgou.goods.pojo.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TemplateMapper extends tk.mybatis.mapper.common.Mapper<Template> {

    @Select("SELECT tb_template.* FROM tb_template, tb_category WHERE tb_template.id = tb_category.template_id AND tb_category.id = #{categoryId}")
    Template findByCategoryId(long categoryId);

}
