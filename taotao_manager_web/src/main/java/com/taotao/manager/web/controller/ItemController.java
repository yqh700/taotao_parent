package com.taotao.manager.web.controller;

import com.taotao.manager.domain.Item;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品
     * @param item
     * @param desc
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void saveItem(Item item, String desc) {
        itemService.saveItem(item, desc);
    }
}
