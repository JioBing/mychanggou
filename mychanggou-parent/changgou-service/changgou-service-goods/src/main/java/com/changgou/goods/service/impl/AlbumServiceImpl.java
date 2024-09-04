package com.changgou.goods.service.impl;

import com.changgou.goods.dao.AlbumMapper;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName AlbumServiceImpl
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/4 23:30
 * @Version 1.0
 **/

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumMapper albumMapper;

    @Autowired
    public AlbumServiceImpl(AlbumMapper albumMapper) {
        this.albumMapper = albumMapper;
    }

    @Override
    public PageInfo<Album> findPage(Album album, int page, int size) {
        Example example = createExample(album);
        PageHelper.startPage(page, size);
        List<Album> albums = albumMapper.selectByExample(example);
        return new PageInfo<>(albums, albums.size());
    }

    @Override
    public PageInfo<Album> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Album> albums = albumMapper.selectAll();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(albums);
        pageInfo.setSize(albums.size());
        return pageInfo;
    }

    @Override
    public List<Album> findList(Album album) {

        Example example = createExample(album);
        List<Album> albums = albumMapper.selectByExample(example);
        return albums;
    }

    @Override
    public int delete(Long id) {
        int i = albumMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public int update(Album album) {
        int i = albumMapper.updateByPrimaryKeySelective(album);
        return i;
    }

    @Override
    public int add(Album album) {
        // NULL属性不会保存，会使用数据库默认值，跟insert()区别
        int i = albumMapper.insertSelective(album);
        return i;
    }

    @Override
    public Album findById(Long id) {
        Album album = new Album();
        album.setId(id);
        Album result = albumMapper.selectOne(album);
        return result;
    }


    @Override
    public List<Album> findAll() {
        List<Album> albums = albumMapper.selectAll();
        return albums;
    }


    /**
     * 注意property写的是映射数据表的pojo对象的属性
     * 查询条件封装
     *
     * @param album pojo对象
     * @return
     */
    private Example createExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album != null) {
            // 编号
            if (!StringUtils.isEmpty(album.getId())) {
                criteria.andEqualTo("id", album.getId());
            }
            // 相册名称
            if (!StringUtils.isEmpty(album.getTitle())) {
                criteria.andLike("title", "%" + album.getTitle() + "%");
            }
            // 相册封面
            if (!StringUtils.isEmpty(album.getImage())) {
                criteria.andEqualTo("image", album.getImage());
            }
            // 图片列表
            if (!StringUtils.isEmpty(album.getImageItems())) {
                criteria.andEqualTo("imageItems", album.getImageItems());
            }
        }
        return example;
    }
}
