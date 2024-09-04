package com.changgou.goods.controller;

import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

/**
 * @ClassName AlbumController
 * @Description 图片库管理
 * @Author huangpengbing
 * @Date 2024/9/4 23:34
 * @Version 1.0
 **/

@RequestMapping("/album")
@RestController
@ApiModel(description = "图片库管理类")
@CrossOrigin
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "查询所有（无参无分页）", httpMethod = "GET")
    public Result<List<Album>> findAll() {
        List<Album> all = albumService.findAll();
        return new Result<>(true, StatusCode.OK, "查询所有成功", all);
    }


    /**
     * 使用注意：如果{id}中的id与参数Long id，与名称相同，@PathVariable(value = "id")中可不用指定路径变量，可直接写为@PathVariable Long id
     * 如果如果{id}中的id与参数名称不同，则需要指明路径变量@PathVariable(value = "id")，总之，value的值要与{}中的值保持一致
     * 另额外注意，如果请求路径中没有id这个参数的值，请求会404，因为@PathVariable注解中有个required = true(default)
     */
    // todo 有時間研究一下前端请求发到后端项目的原理，方便排查问题？
    @GetMapping(value = "/findById/{id}")
    @ApiOperation(value = "根据id查询相册", httpMethod = "GET")
    public Result<Album> findById(@PathVariable(value = "id") Long id) {
        Album byId = albumService.findById(id);
        return new Result<>(true, StatusCode.OK, "根据id查询成功", byId);
    }

    @PostMapping(path = "/add")
    @ApiOperation(value = "新增相册的方法", httpMethod = "POST")
    public Result add(@ApiParam(value = "相册的pojo对象，直接新增一个") @RequestBody Album album) {
        int add = albumService.add(album);
        return new Result<>(true, StatusCode.OK, "相册新增成功", add);
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "更新相册", httpMethod = "PUT")
    public Result update(@ApiParam(value = "被修改相册的id") @PathVariable(value = "id") long albumId, @ApiParam(value = "传要改的属性值") @RequestBody Album album) {
        album.setId(albumId);
        int update = albumService.update(album);
        return new Result<>(true, StatusCode.OK, "相册更新成功", update);
    }

    @DeleteMapping("/delete{id}")
    @ApiOperation(value = "删除相册", httpMethod = "DELETE")
    public Result delete(@ApiParam(value = "被修改相册的id") @PathVariable(value = "id") long albumId) {
        int delete = albumService.delete(albumId);
        return new Result<>(true, StatusCode.OK, "删除相册成功", delete);
    }

    @PostMapping("/findList")
    @ApiOperation(value = "多条件搜索相册", httpMethod = "POST")
    public Result findList(@RequestBody Album album) {
        List<Album> list = albumService.findList(album);
        return new Result<>(true, StatusCode.OK, "搜索相册成功", list);
    }

    @GetMapping("/findPage/{page}/{size}")
    @ApiOperation(value = "分页查询")
    public Result findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Album> page1 = albumService.findPage(page, size);
        return new Result<>(true, StatusCode.OK, "分页查询成功", page1);
    }


    @PostMapping("/findByPage/{page}/{size}")
    @ApiOperation(value = "搜索加分页查询", httpMethod = "POST")
    public Result findByPage(@PathVariable int page, @PathVariable int size, @RequestBody Album album) {
        PageInfo<Album> page1 = albumService.findPage(album, page, size);
        return new Result<>(true, StatusCode.OK, "搜索分页查询成功", page1);
    }

}
