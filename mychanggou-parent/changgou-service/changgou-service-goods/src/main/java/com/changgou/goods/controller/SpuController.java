package com.changgou.goods.controller;

import com.changgou.goods.service.SpuService;
import com.changgou.goods.vo.GoodsVo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SpuController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 17:06
 * @Version 1.0
 **/

@RestController
@CrossOrigin
@RequestMapping("/spu")
@ApiModel(description = "商品添加")
public class SpuController {

    private final SpuService spuService;

    @Autowired
    public SpuController(SpuService spuService) {
        this.spuService = spuService;
    }

    @PostMapping("/save")
    @ApiOperation(value = "商品保存、修改之spu+sku", httpMethod = "POST")
    public Result save(@RequestBody GoodsVo goodsVo) {
        spuService.save(goodsVo);
        return new Result(true, StatusCode.OK, "操作成功");
    }

    @GetMapping("/findBySpuId/{id}")
    @ApiOperation(value = "根据spuId查询商品详情页", httpMethod = "GET")
    public Result findBySpuId(@PathVariable(value = "id") long spuId) {
        GoodsVo bySpuId = spuService.findBySpuId(spuId);
        return new Result(true, StatusCode.OK, "商品详情页查询成功", bySpuId);
    }

}
