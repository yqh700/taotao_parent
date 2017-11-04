package com.taotao.manager.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YangQingHua on 2017/11/5.
 */
@Controller
public class PageController {

    /**
     * 通用页面跳转
     * @param pageName
     * @return
     */
    @RequestMapping("/page/{pageName}")
    public String toPage(@PathVariable String pageName) {
        return pageName;
    }
}
