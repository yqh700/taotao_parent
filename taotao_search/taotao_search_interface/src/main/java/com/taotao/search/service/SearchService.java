package com.taotao.search.service;

import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Item;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
public interface SearchService {

    /**
     * 商品搜索，搜索Solr索引库
     * @param keyword 查询关键词
     * @param page 第几页
     * @param rows 每页显示数量
     * @return
     */
    TaoResult<Item> search(String keyword, Integer page, Integer rows);

    /**
     * 更新索引库
     * @param itemId
     */
    void saveItem(long itemId);
}
