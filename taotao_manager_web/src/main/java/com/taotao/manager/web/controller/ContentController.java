package com.taotao.manager.web.controller;

import com.taotao.common.TaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.manager.domain.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;


    /**
     * 查询内容列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public TaoResult<Content> queryConentPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer rows,
            Long categoryId) {

        return contentService.queryContentPage(page, rows, categoryId);
    }


    /**
     * 保存内容
     * @param content
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void saveContent(Content content) {
        contentService.save(content);
    }
}
