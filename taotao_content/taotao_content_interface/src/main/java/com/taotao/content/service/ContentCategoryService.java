package com.taotao.content.service;

import com.taotao.manager.domain.ContentCategory;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
public interface ContentCategoryService extends BaseService<ContentCategory> {

    /**
     * 根据父节点id查询内容分类
     * @param parentId
     * @return
     */
    List<ContentCategory> findContentCategoryByParentId(Long parentId);

    /**
     * 修改内容分类
     * @param contentCategory
     * @return
     */
    ContentCategory saveContentCategory(ContentCategory contentCategory);

    /**
     * 删除内容分类
     * @param parentId
     * @param id
     */
    void deleteContentCategory(Long parentId, Long id);
}
