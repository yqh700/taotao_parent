package com.taotao.portal.web.controller;

import com.taotao.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 杨清华.
 * on 2017/11/9.
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value("${TAOTAO_AD_ID}")
    private Long TAOTAO_AD_ID;

    /**
     * 访问首页
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        String ad = contentService.findAD(TAOTAO_AD_ID);
        model.addAttribute("ad", ad);
        return "index";
    }
}
