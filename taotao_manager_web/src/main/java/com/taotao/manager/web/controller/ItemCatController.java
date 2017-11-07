package com.taotao.manager.web.controller;

import com.taotao.manager.domain.ItemCat;
import com.taotao.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;


    /**
     * 根据父节点 id 查询商品类目
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ItemCat> findItemCatByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<ItemCat> list = itemCatService.findItemCatByParentId(parentId);
        return list;
    }
}
