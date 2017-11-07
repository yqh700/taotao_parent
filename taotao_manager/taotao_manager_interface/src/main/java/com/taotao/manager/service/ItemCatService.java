package com.taotao.manager.service;

import com.taotao.manager.domain.ItemCat;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
public interface ItemCatService extends BaseService<ItemCat> {

    List<ItemCat> findItemCatByParentId(Long parentId);
}
