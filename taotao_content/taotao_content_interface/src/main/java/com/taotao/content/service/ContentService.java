package com.taotao.content.service;

import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Content;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
public interface ContentService extends BaseService<Content> {

    /**
     * 查询内容分页数据
     * @param page
     * @param rows
     * @return
     */
    TaoResult<Content> queryContentPage(Integer page, Integer rows, Long categoryId);
}
