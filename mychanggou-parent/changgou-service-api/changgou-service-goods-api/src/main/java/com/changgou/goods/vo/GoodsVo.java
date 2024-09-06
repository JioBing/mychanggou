package com.changgou.goods.vo;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品组合实体类
 */
@Data
@ApiModel(description = "商品信息")
public class GoodsVo implements Serializable {

    @ApiModelProperty(value = "商品信息的Spu对象信息，即一些公共属性抽取", dataType = "Spu", example = "{\n" +
            "    \"brandId\": 8557,\n" +
            "    \"caption\": \"华为手机大促销\",\n" +
            "    \"category1Id\": 1,\n" +
            "    \"category2Id\": 59,\n" +
            "    \"category3Id\": 64,\n" +
            "    \"commentNum\": 0,\n" +
            "    \"freightId\": 0,\n" +
            "    \"images\": \"http://www.qingcheng.com/image/1.jpg,http://www.qingcheng.com/image/2.jpg\",\n" +
            "    \"introduction\": \"华为产品世界最强\",\n" +
            "    \"isEnableSpec\": \"1\",\n" +
            "    \"isMarketable\": \"1\",\n" +
            "    \"name\": \"string\",\n" +
            "    \"specItems\": \"{\\\"颜色\\\":[\\\"红\\\",\\\"绿\\\"],\\\"机身内存\\\":[\\\"64G\\\",\\\"8G\\\"]}\",\n" +
            "    \"paraItems\": \"{\\\"赠品\\\":\\\"充电器\\\",\\\"出厂年份\\\":\\\"2019\\\"}\",\n" +
            "    \"saleNum\": 0,\n" +
            "    \"saleService\": \"一年包换\",\n" +
            "    \"sn\": \"No10001\",\n" +
            "    \"status\": \"1\",\n" +
            "    \"templateId\": 42\n" +
            "}")
    private Spu spu;

    @ApiModelProperty(value = "商品信息Sku对象，即具体特有的一些属性", dataType = "List<Sku>", example = "[\n" +
            "    {\n" +
            "      \"alertNum\": 10,\n" +
            "      \"brandName\": \"华为\",\n" +
            "      \"categoryId\": 64,\n" +
            "      \"commentNum\": 0,\n" +
            "      \"image\": \"http://www.baidu.com\",\n" +
            "      \"images\": \"\",\n" +
            "      \"name\": \"华为P30手机\",\n" +
            "      \"num\": 5,\n" +
            "      \"price\": 1000,\n" +
            "      \"saleNum\": 0,\n" +
            "      \"sn\": \"No1001\",\n" +
            "      \"spec\": \"{\\\"颜色\\\":\\\"红色\\\",\\\"网络\\\":\\\"移动3G\\\"}\",\n" +
            "      \"weight\": 0\n" +
            "    }\n" +
            "  ]")
    private List<Sku> skuList;

}