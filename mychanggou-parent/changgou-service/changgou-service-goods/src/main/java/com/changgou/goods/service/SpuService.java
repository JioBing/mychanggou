package com.changgou.goods.service;

import com.changgou.goods.pojo.Spu;
import com.changgou.goods.vo.GoodsVo;
import com.github.pagehelper.PageInfo;

public interface SpuService {

    void save(GoodsVo goodsVo);

    GoodsVo findBySpuId(long id);

    void audit(long id);

    void pull(long id);

    int putMany(Long[] idList);

    int pullMany(Long[] idList);

    void logicDelete(Long spuId);

    void restore(Long spuId);

    PageInfo findPage(int page, int size, Spu spu);
}
