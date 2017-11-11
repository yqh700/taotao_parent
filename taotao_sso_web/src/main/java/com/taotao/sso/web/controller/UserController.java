package com.taotao.sso.web.controller;

import com.github.abel533.entity.Example;
import com.taotao.manager.domain.User;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 检查数据是否可用
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public ResponseEntity<Boolean> check(@PathVariable String param, @PathVariable Integer type) {
        try {
            Boolean bool = userService.check(param, type);
            return ResponseEntity.ok(bool);
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
    public ResponseEntity<User> queryUserByTicket(String ticket) {

        try {
            User user = userService.queryUserByTicket(ticket);
            if(user != null) {
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
