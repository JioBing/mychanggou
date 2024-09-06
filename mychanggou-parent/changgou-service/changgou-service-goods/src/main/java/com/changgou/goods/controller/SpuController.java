package com.changgou.goods.controller;

import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.changgou.goods.vo.GoodsVo;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @PutMapping(path = "/audit/{id}")
    @ApiOperation(value = "商品审核", httpMethod = "PUT")
    public Result audit(@ApiParam(value = "参数是spu商品id", defaultValue = "0") @PathVariable(value = "id") long spuId) {
        spuService.audit(spuId);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    @PutMapping(path = "/pull/{id}")
    @ApiOperation(value = "商品下架", httpMethod = "PUT")
    public Result pull(@ApiParam(value = "参数是spu商品id", defaultValue = "0") @PathVariable(value = "id") long spuId) {
        spuService.pull(spuId);
        return new Result(true, StatusCode.OK, "下架成功");
    }


    @PutMapping("/put/many")
    @ApiOperation(value = "批量上架", httpMethod = "PUT")
    public Result putMany(@ApiParam(value = "spu商品id集合", defaultValue = "[0]") @RequestBody Long[] ids) {
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, "上架" + count + "个商品");
    }

    @PutMapping("/pull/many")
    @ApiOperation(value = "批量下架", httpMethod = "PUT")
    public Result pullMany(@ApiParam(value = "spu商品id集合", defaultValue = "[0]") @RequestBody Long[] ids) {
        int count = spuService.pullMany(ids);
        return new Result(true, StatusCode.OK, "上架" + count + "个商品");
    }

    @DeleteMapping("/logic/delete/{id}")
    @ApiOperation(value = "数据删除", httpMethod = "DELETE")
    public Result logicDelete(@PathVariable Long id) {
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK, "逻辑删除成功！");
    }

    @PutMapping("/restore/{id}")
    @ApiOperation(value = "数据恢复", httpMethod = "PUT")
    public Result restore(@PathVariable Long id) {
        spuService.restore(id);
        return new Result(true, StatusCode.OK, "数据恢复成功！");
    }

    @PostMapping("/search/{page}/{size}")
    @ApiOperation(value = "分页、条件搜索查询", httpMethod = "POST")
    public PageInfo findPage(@ApiParam(value = "当前页", defaultValue = "1") @PathVariable int page,@ApiParam(value = "每页条数", defaultValue = "10") @PathVariable int size, @RequestBody Spu spu) {
        PageInfo page1 = spuService.findPage(page, size, spu);
        return page1;
    }


}
