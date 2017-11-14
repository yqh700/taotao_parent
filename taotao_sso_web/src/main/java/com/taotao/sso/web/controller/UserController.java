package com.taotao.sso.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manager.domain.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final ObjectMapper OM = new ObjectMapper();
    /**
     * 检查数据是否可用
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public ResponseEntity<String> check(
            @PathVariable String param,
            @PathVariable Integer type,
            HttpServletRequest request) {

        try {
            Boolean bool = userService.check(param, type);
            String callback = request.getParameter("callback");
            //判断是否为jsonp请求
            if(StringUtils.isNotBlank(callback)) {
                return ResponseEntity.ok(callback + "(" + bool + ")");
            } else {
                return ResponseEntity.ok(String.valueOf(bool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询用户登录信息
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/{ticket}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> queryUserByTicket(@PathVariable String ticket, HttpServletRequest request) {

        try {
            User user = userService.queryUserByTicket(ticket);
            if(user != null) {
                //判断是否是jsonp请求
                String callback = request.getParameter("callback");
                if(StringUtils.isNotBlank(callback)) {
                    return ResponseEntity.ok(callback + "(" + OM.writeValueAsString(user) + ")");
                } else {
                    return ResponseEntity.ok(OM.writeValueAsString(user));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
