package com.taotao.manager.service;

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
}
