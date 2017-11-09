package com.taotao.manager.service;

import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Item;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
public interface ItemService extends BaseService<Item> {

    /**
     * 保存商品
     * @param item 商品
     * @param desc 商品描述
     */
    void saveItem(Item item, String desc);

    /**
     * 查询商品列表
     * @param page 第几页
     * @param rows 每页显示几条
     * @return
     */
    TaoResult<Item> queryItemList(Integer page, Integer rows);
}
