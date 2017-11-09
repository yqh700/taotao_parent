package com.taotao.content.service.impl;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.manager.domain.ContentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {

    @Override
    public List<ContentCategory> findContentCategoryByParentId(Long parentId) {
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);

        return super.findListByWhere(param);
    }

    @Override
    public ContentCategory saveContentCategory(ContentCategory contentCategory) {
        //设置初始状态
        contentCategory.setIsParent(false);
        contentCategory.setStatus(1);

        //保存内容分类
        super.save(contentCategory);

        //检查父节点的isParent属性是否为true,如果不为true，将其改成true

        ContentCategory parentContentCategory = super.findById(contentCategory.getParentId());
        if(!parentContentCategory.getIsParent()) {
            parentContentCategory.setIsParent(true);
            super.updateByIdSelective(parentContentCategory);
        }

        return contentCategory;
    }

    @Override
    public void deleteContentCategory(Long parentId, Long id) {
        //定义一个集合，用于存储要删除的节点和其所有的子节点
        List<Object> ids = new ArrayList<>();

        //将要删除节点的id放入集合
        ids.add(id);

        //遍历查询所有子节点的id
        findChildrenId(id, ids);

        //删除所有找到的节点
        super.deleteByIds(ids);

        //检查当前节点是否还有兄弟节点
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);
        List<ContentCategory> list = super.findListByWhere(param);

        //如果没有兄弟节点，将父节点的isParent改为false
        if(list == null || list.size() == 0) {
            ContentCategory parentContentCategory = new ContentCategory();
            parentContentCategory.setId(parentId);
            parentContentCategory.setIsParent(false);
            super.updateByIdSelective(parentContentCategory);
        }
    }

    /**
     * 查询子节点id
     * @param id
     * @param ids
     */
    private void findChildrenId(Long id, List<Object> ids) {
        ContentCategory param = new ContentCategory();
        param.setParentId(id);
        List<ContentCategory> list = super.findListByWhere(param);

        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            findChildrenId(contentCategory.getId(), ids);
        }
    }
}
