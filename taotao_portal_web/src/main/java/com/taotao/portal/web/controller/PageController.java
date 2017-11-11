package com.taotao.portal.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
@Controller
@RequestMapping("/page")
public class PageController {

    /**
     * 通用页面跳转
     * @param pageName
     * @return
     */
    @RequestMapping("/{pageName}")
    public String toPage(@PathVariable String pageName) {
        return pageName;
    }
}
