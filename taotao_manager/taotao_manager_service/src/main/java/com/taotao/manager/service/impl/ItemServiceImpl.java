package com.taotao.manager.service.impl;

import com.taotao.manager.domain.Item;
import com.taotao.manager.domain.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    @Autowired
    private ItemDescService itemDescService;

    @Override
    public void saveItem(Item item, String desc) {
        //保存商品
        item.setStatus(1);
        super.save(item);

        //保存商品描述
        ItemDesc param = new ItemDesc();
        param.setItemId(item.getId());
        param.setItemDesc(desc);

        itemDescService.save(param);
    }
}
