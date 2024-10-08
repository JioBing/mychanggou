package com.changgou.goods.service;

import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AlbumService {

    /***
     * Album多条件分页查询
     * @param album
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(Album album, int page, int size);

    /***
     * Album分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(int page, int size);

    /***
     * Album多条件搜索方法
     * @param album
     * @return
     */
    List<Album> findList(Album album);

    /***
     * 删除Album
     * @param id
     */
    int delete(Long id);

    /***
     * 修改Album数据
     * @param album
     */
    int update(Album album);

    /***
     * 新增Album
     * @param album
     */
    int add(Album album);

    /**
     * 根据ID查询Album
     * @param id
     * @return
     */
    Album findById(Long id);

    /***
     * 查询所有Album
     * @return
     */
    List<Album> findAll();

}
