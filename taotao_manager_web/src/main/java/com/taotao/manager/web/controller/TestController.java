package com.taotao.manager.web.controller;

import com.taotao.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YangQingHua on 2017/11/5.
 */
@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/queryDate")
    @ResponseBody
    public String queryDate() {
        return testService.queryDate();
    }
}
