package com.changgou.goods.controller;

import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName TemplateController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:00
 * @Version 1.0
 **/

@RestController
@RequestMapping("/template")
@CrossOrigin
@ApiModel(description = "模板管理")
public class TemplateController {

    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "分页条件搜索实现", httpMethod = "POST")
    public Result<PageInfo> findPage(@RequestBody(required = false) Template template, @PathVariable int page, @PathVariable int size) {
        PageInfo<Template> pageInfo = templateService.findPage(template, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @GetMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "分页实现", httpMethod = "GET")
    public Result<PageInfo> findPage(@ApiParam(value = "当前页", defaultValue = "1") @PathVariable int page, @ApiParam(value = "每页显示多少条", defaultValue = "10") @PathVariable int size) {
        PageInfo<Template> pageInfo = templateService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    @PostMapping(value = "/search")
    @ApiOperation(value = "多条件搜索品牌数据", httpMethod = "POST")
    public Result<List<Template>> findList(@RequestBody(required = false) Template template) {
        List<Template> list = templateService.findList(template);
        return new Result<List<Template>>(true, StatusCode.OK, "查询成功", list);
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据ID删除品牌数据", httpMethod = "DELETE")
    public Result delete(@PathVariable Integer id) {
        templateService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改Template数据", httpMethod = "PUT")
    public Result update(@RequestBody Template template, @PathVariable Integer id) {
        template.setId(id);
        templateService.update(template);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    @PostMapping
    @ApiOperation(value = "新增Template数据", httpMethod = "POST")
    public Result add(@RequestBody Template template) {
        templateService.add(template);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "据ID查询Template数据", httpMethod = "GET")
    public Result<Template> findById(@PathVariable Integer id) {
        //根据ID查询
        Template template = templateService.findById(id);
        return new Result<Template>(true, StatusCode.OK, "查询成功", template);
    }


    @GetMapping
    @ApiOperation(value = "查询Template全部数据", httpMethod = "GET")
    public Result<Template> findAll() {
        List<Template> list = templateService.findAll();
        return new Result<Template>(true, StatusCode.OK, "查询成功", list);
    }


    @ApiOperation(value = "根据分类id查询模板数据", httpMethod = "GET")
    @GetMapping(path = "/findByCategoryId/{id}")
    public Result findByCategoryId(@PathVariable(value = "id") long categoryId) {
        Template byCategoryId = templateService.findByCategoryId(categoryId);
        return new Result(true, StatusCode.OK, "根据分类id查询模板数据成功", byCategoryId);
    }

}
