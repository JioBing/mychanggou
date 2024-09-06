package com.changgou.goods.service;

import com.changgou.goods.vo.GoodsVo;

public interface SpuService {

    void save(GoodsVo goodsVo);

    GoodsVo findBySpuId(long id);

}
