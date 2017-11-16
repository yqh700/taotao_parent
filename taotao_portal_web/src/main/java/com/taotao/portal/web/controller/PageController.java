package com.taotao.portal.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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
    public String toPage(@PathVariable String pageName,
                         @RequestParam(value = "redirectURL", defaultValue = "") String redirectURL,
                         Model model) {

        model.addAttribute("redirectURL", redirectURL);
        return pageName;
    }
}
