package com.taotao.portal.web.controller;

import com.taotao.common.UserResult;
import com.taotao.manager.domain.User;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${TT_TICKET}")
    private String TT_TICKET;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public UserResult doRegister(User user) {
        userService.doRegister(user);
        return new UserResult("200");
    }

    /**
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public UserResult doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        String ticket = userService.doLogin(user);

        if(StringUtils.isNotBlank(ticket)) {
            CookieUtils.setCookie(request, response, TT_TICKET, ticket, 60 * 60 * 24, true);
            return new UserResult("200");
        }
        return new UserResult("-1");
    }
}
