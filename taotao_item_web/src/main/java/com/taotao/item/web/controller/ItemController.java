package com.taotao.item.web.controller;

import com.taotao.manager.domain.Item;
import com.taotao.manager.domain.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    /**
     * 商品详情
     * @param itemId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String toItem(@PathVariable Long itemId, Model model) {
        //商品
        Item item = itemService.findById(itemId);

        //商品详情
        ItemDesc itemDesc = itemDescService.findById(itemId);

        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);

        return "item";
    }
}
