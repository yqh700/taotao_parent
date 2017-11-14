package com.taotao.sso.service;

import com.taotao.manager.domain.User;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
public interface UserService {

    /**
     * 检查数据是否可用
     * @param param
     * @param type
     * @return
     */
    Boolean check(String param, Integer type);

    /**
     * 查询用户登录信息
     * @param ticket
     * @return
     */
    User queryUserByTicket(String ticket);

    /**
     * 用户注册
     * @param user
     */
    void doRegister(User user);

    /**
     * 用户登录
     * @param user
     * @return
     */
    String doLogin(User user);
}
