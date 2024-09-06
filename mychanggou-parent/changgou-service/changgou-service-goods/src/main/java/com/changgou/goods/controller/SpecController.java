package com.changgou.goods.controller;

import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SpecController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:19
 * @Version 1.0
 **/


@RestController
@RequestMapping("/spec")
@CrossOrigin
public class SpecController {

    private final SpecService specService;

    @Autowired
    public SpecController(SpecService specService) {
        this.specService = specService;
    }


    @PostMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "分页条件搜索实现", httpMethod = "POST")
    public Result<PageInfo> findPage(@ApiParam(value = "条件搜索参数") @RequestBody(required = false) Spec spec, @ApiParam(value = "当前页") @PathVariable int page, @ApiParam("每页记录数量") @PathVariable int size) {
        //执行搜索
        PageInfo<Spec> pageInfo = specService.findPage(spec, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }


    @GetMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "分页搜索实现", httpMethod = "GET")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //分页查询
        PageInfo<Spec> pageInfo = specService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }


    @PostMapping(value = "/search")
    @ApiOperation(value = "多条件搜索品牌数据", httpMethod = "POST")
    public Result<List<Spec>> findList(@RequestBody(required = false) Spec spec) {
        List<Spec> list = specService.findList(spec);
        return new Result<List<Spec>>(true, StatusCode.OK, "查询成功", list);
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据ID删除品牌数据", httpMethod = "DELETE")
    public Result delete(@PathVariable Integer id) {
        specService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改Spec数据", httpMethod = "PUT")
    public Result update(@RequestBody Spec spec, @PathVariable Integer id) {
        //设置主键值
        spec.setId(id);
        //修改数据
        specService.update(spec);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    @PostMapping
    @ApiOperation(value = "新增Spec数据", httpMethod = "POST")
    public Result add(@RequestBody Spec spec) {
        specService.add(spec);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询Spec数据", httpMethod = "GET")
    public Result<Spec> findById(@PathVariable Integer id) {
        //根据ID查询
        Spec spec = specService.findById(id);
        return new Result<Spec>(true, StatusCode.OK, "查询成功", spec);
    }


    @GetMapping
    @ApiOperation(value = "查询Spec全部数据", httpMethod = "GET")
    public Result<Spec> findAll() {
        List<Spec> list = specService.findAll();
        return new Result<Spec>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping(path = "/findByCategoryId/{id}")
    @ApiOperation(value = "根据分类id查询得到模板，根据模板id得到所有规则数据", httpMethod = "GET")
    public Result findByCategoryId(@ApiParam(value = "所选分类的id", defaultValue = "42") @PathVariable(value = "id") int categoryId) {
        List<Spec> byCategoryId = specService.findByCategoryId(categoryId);
        return new Result(true, StatusCode.OK, "成功查询到规则数据", byCategoryId);
    }
}
