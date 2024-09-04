package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @Author:admin
 * @Description:Album构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name = "tb_album")
@Entity
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;//编号

    @Column(name = "title")
    @ApiModelProperty(name = "title", value = "相册名称", dataType = "String")
    private String title;

    @Column(name = "image")
    @ApiModelProperty(name = "image", value = "相册封面，表示为一个图片路径", dataType = "String")
    private String image;

    @Column(name = "image_items")
    @ApiModelProperty(name = "imageItems", value = "图片列表，包含url,status,uid;为json格式数据", dataType = "String")
    private String imageItems;


    //get方法
    public Long getId() {
        return id;
    }

    //set方法
    public void setId(Long id) {
        this.id = id;
    }

    //get方法
    public String getTitle() {
        return title;
    }

    //set方法
    public void setTitle(String title) {
        this.title = title;
    }

    //get方法
    public String getImage() {
        return image;
    }

    //set方法
    public void setImage(String image) {
        this.image = image;
    }

    //get方法
    public String getImageItems() {
        return imageItems;
    }

    //set方法
    public void setImageItems(String imageItems) {
        this.imageItems = imageItems;
    }


}
