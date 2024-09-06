package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BrandController
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/8/22 14:53
 * @Version 1.0
 **/


@RestController
@RequestMapping(value = "/brand")
@CrossOrigin
public class BrandController {

    private final BrandService brandService;
    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping(path = "/list")
    public Result findAll() {
        List<Brand> brands = brandService.findAll();
        Result result = new Result(true, 200, "查询所有成功", brands);
        return result;
    }

    @GetMapping(path = "/{id}")
    public Result<Brand> findById(@PathVariable Integer id) {
        Brand byId = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据id查询成功", byId);
    }

    @PostMapping(path = "/add")
    public Result add(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result<Brand>(true, StatusCode.OK, "新增成功");
    }

    @PutMapping(path = "/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        brandService.update(brand);
        return new Result<>(true, StatusCode.OK, "更新成功");
    }

    @DeleteMapping(path = "/{id}")
    public Result delete(@PathVariable Integer id) {
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping(path = "/findList")
    public Result findList(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result(true, StatusCode.OK, "条件查询成功", list);
    }

    @GetMapping("/search/{page}/{size}")
    public Result findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo pageInfo = brandService.findPage(page, size);
        return new Result(true, StatusCode.OK, "分页查询成功", pageInfo);
    }


    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody(required = false) Brand brand, @PathVariable int page, @PathVariable int size) {

        PageInfo pageInfo = brandService.findPageList(brand, page, size);
        return new Result(true, StatusCode.OK, "分页+条件查询成功", pageInfo);
    }

    @GetMapping(path = "/findByCategoryId/{id}")
    @ApiOperation(value = "根据分类id查询所有品牌，", httpMethod = "GET")
    public Result<List<Brand>> findByCategoryId(@PathVariable(value = "id") long categoryId) {
        List<Brand> byCategoryId = brandService.findByCategoryId(categoryId);
        return new Result<>(true, StatusCode.OK, "根据分类id查询所有品牌", byCategoryId);
    }



}
