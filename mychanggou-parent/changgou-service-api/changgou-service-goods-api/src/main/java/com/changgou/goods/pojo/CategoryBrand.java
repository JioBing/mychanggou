package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:admin
 * @Description:CategoryBrand构建
 * @Date 2019/6/14 19:13
 *****/
@Data
@ApiModel(description = "CategoryBrand", value = "CategoryBrand")
@Table(name = "tb_category_brand")
public class CategoryBrand implements Serializable {

    @Id
    @Column(name = "category_id")
    @ApiModelProperty(value = "分类ID", required = false)
    private Integer categoryId;

    @Id
    @Column(name = "brand_id")
    @ApiModelProperty(value = "品牌ID", required = false)
    private Integer brandId;

}
