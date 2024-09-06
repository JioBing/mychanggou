package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:admin
 * @Description:Category构建
 * @Date 2019/6/14 19:13
 *****/

@Data
@ApiModel(description = "Category", value = "Category")
@Table(name = "tb_category")
public class Category implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "分类ID", required = false)
    private Integer id;

    @Column(name = "name")
    @ApiModelProperty(value = "分类名称", required = false)
    private String name;

    @Column(name = "goods_num")
    @ApiModelProperty(value = "商品数量", required = false)
    private Integer goodsNum;

    @Column(name = "is_show")
    @ApiModelProperty(value = "是否显示", required = false)
    private String isShow;

    @Column(name = "is_menu")
    @ApiModelProperty(value = "是否导航", required = false)
    private String isMenu;

    @Column(name = "seq")
    @ApiModelProperty(value = "排序", required = false)
    private Integer seq;

    @Column(name = "parent_id")
    @ApiModelProperty(value = "上级ID", required = false)
    private Integer parentId;

    @Column(name = "template_id")
    @ApiModelProperty(value = "模板ID", required = false)
    private Integer templateId;


}
