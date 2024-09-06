package com.changgou.goods.controller;

import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ParaController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:37
 * @Version 1.0
 **/

@RestController
@RequestMapping("/para")
@CrossOrigin
public class ParaController {

    private final ParaService paraService;
    @Autowired
    public ParaController(ParaService paraService) {
        this.paraService = paraService;
    }

    @PostMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "Para分页条件搜索实现", httpMethod = "")
    public Result<PageInfo> findPage(@RequestBody(required = false) Para para, @PathVariable int page, @PathVariable int size) {
        //执行搜索
        PageInfo<Para> pageInfo = paraService.findPage(para, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @GetMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "Para分页搜索实现", httpMethod = "")
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size) {
        //分页查询
        PageInfo<Para> pageInfo = paraService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @PostMapping(value = "/search")
    @ApiOperation(value = "多条件搜索品牌数据", httpMethod = "")
    public Result<List<Para>> findList(@RequestBody(required = false) Para para) {
        List<Para> list = paraService.findList(para);
        return new Result<List<Para>>(true, StatusCode.OK, "查询成功", list);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据ID删除品牌数据", httpMethod = "")
    public Result delete(@PathVariable Integer id) {
        paraService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改Para数据", httpMethod = "")
    public Result update(@RequestBody Para para, @PathVariable Integer id) {
        //设置主键值
        para.setId(id);
        //修改数据
        paraService.update(para);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @PostMapping
    @ApiOperation(value = "新增Para数据", httpMethod = "")
    public Result add(@RequestBody Para para) {
        paraService.add(para);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "据ID查询Para数据", httpMethod = "")
    public Result<Para> findById(@PathVariable Integer id) {
        //根据ID查询
        Para para = paraService.findById(id);
        return new Result<Para>(true, StatusCode.OK, "查询成功", para);
    }

    @GetMapping
    @ApiOperation(value = "查询Para全部数据", httpMethod = "")
    public Result<Para> findAll() {
        List<Para> list = paraService.findAll();
        return new Result<Para>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping(path = "/findByCategoryId/{id}")
    @ApiOperation(value = "根据分类id查询参数信息", httpMethod = "GET")
    public Result findByCategoryId(@PathVariable(value = "id") int categoryId) {
        List<Para> byCategoryId = paraService.findByCategoryId(categoryId);
        return new Result<Para>(true, StatusCode.OK, "查询成功", byCategoryId);
    }
}
