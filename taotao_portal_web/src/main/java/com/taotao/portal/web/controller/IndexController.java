package com.taotao.portal.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    /**
     * 访问首页
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
