package com.taotao.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.taotao.manager.domain.ItemCat;
import com.taotao.manager.mapper.ItemCatMapper;
import com.taotao.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
@Service
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    /**
     * 根据父节点 id 查询商品类目
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        ItemCat param = new ItemCat();
        param.setParentId(parentId);
        List<ItemCat> list = super.findListByWhere(param);
        return list;
    }
}
