package com.taotao.manager.web.controller;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.manager.domain.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点id查询内容分类
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ContentCategory> findContentCategoryByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return contentCategoryService.findContentCategoryByParentId(parentId);
    }

    /**
     * 添加内容分类
     * @param contentCategory
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ContentCategory saveContentCatory(ContentCategory contentCategory) {
        return contentCategoryService.saveContentCategory(contentCategory);
    }

    /**
     * 修改内容分类
     * @param contentCategory
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateContentCategory(ContentCategory contentCategory) {
        contentCategoryService.updateByIdSelective(contentCategory);
        return "200";
    }

    /**
     * 删除内容分类
     * @param parentId
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteContentCategory(Long parentId, Long id) {
        contentCategoryService.deleteContentCategory(parentId, id);
        return "200";
    }
}
