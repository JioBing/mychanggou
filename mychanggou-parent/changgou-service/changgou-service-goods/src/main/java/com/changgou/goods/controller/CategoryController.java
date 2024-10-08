package com.changgou.goods.controller;

import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CategoryController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/5 6:43
 * @Version 1.0
 **/


@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /***
     * Category分页条件搜索实现
     * @param category
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "Category分页条件搜索实现", httpMethod = "")
    public Result<PageInfo> findPage(@RequestBody(required = false) Category category, @PathVariable int page, @PathVariable int size) {
        //执行搜索
        PageInfo<Category> pageInfo = categoryService.findPage(category, page, size);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * Category分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    @ApiOperation(value = "Category分页搜索实现", httpMethod = "")
    public Result<PageInfo> findPage(@ApiParam(value = "当前页") @PathVariable int page, @ApiParam(value = "每页显示多少条") @PathVariable int size) {
        //分页查询
        PageInfo<Category> pageInfo = categoryService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param category
     * @return
     */
    @PostMapping(value = "/search")
    @ApiOperation(value = "多条件搜索品牌数据", httpMethod = "")
    public Result<List<Category>> findList(@RequestBody(required = false) Category category) {
        List<Category> list = categoryService.findList(category);
        return new Result<List<Category>>(true, StatusCode.OK, "查询成功", list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据ID删除品牌数据", httpMethod = "")
    public Result delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 修改Category数据
     * @param category
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改Category数据", httpMethod = "")
    public Result update(@RequestBody Category category, @PathVariable Integer id) {
        //设置主键值
        category.setId(id);
        //修改数据
        categoryService.update(category);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 新增Category数据
     * @param category
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增Category数据", httpMethod = "")
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /***
     * 根据ID查询Category数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询Category数据", httpMethod = "")
    public Result<Category> findById(@PathVariable Integer id) {
        //根据ID查询
        Category category = categoryService.findById(id);
        return new Result<Category>(true, StatusCode.OK, "查询成功", category);
    }

    /***
     * 查询Category全部数据
     * @return
     */
    @GetMapping
    @ApiOperation(value = "询Category全部数据", httpMethod = "")
    public Result<Category> findAll() {
        List<Category> list = categoryService.findAll();
        return new Result<Category>(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 根据父ID查询
     */
    @GetMapping(value = "/list/{pid}")
    @ApiOperation(value = "根据父ID查询", httpMethod = "")
    public Result<Category> findByParentId(@PathVariable(value = "pid") Integer pid) {
        List<Category> list = categoryService.findByParentId(pid);
        return new Result<Category>(true, StatusCode.OK, "查询成功", list);
    }



}
