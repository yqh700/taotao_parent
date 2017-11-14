package com.taotao.sso.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manager.domain.User;
import com.taotao.manager.mapper.UserMapper;
import com.taotao.sso.redis.RedisUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${SSO_TAOTAO_TICKET_KEY}")
    private String SSO_TAOTAO_TICKET_KEY;//用户登录信息前缀

    @Value("{SSO_TAOTAO_TICKET_INCR}")
    private String SSO_TAOTAO_TICKET_INCR;//用户登录ticket自增长编号

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public Boolean check(String param, Integer type) {

        User user = new User();

        switch (type) {
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
        }

        int count = userMapper.selectCount(user);
        return count == 0;
    }

    @Override
    public User queryUserByTicket(String ticket) {
        String json = redisUtils.get(SSO_TAOTAO_TICKET_KEY + ticket);

        if(StringUtils.isNotBlank(json)) {
            try {
                User user = OM.readValue(json, User.class);
                redisUtils.expire(SSO_TAOTAO_TICKET_KEY + ticket, 60 * 30);
                return user;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void doRegister(User user) {
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userMapper.insert(user);
    }

    @Override
    public String doLogin(User user) {
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        User dbUser = userMapper.selectOne(user);

        if(dbUser != null) {
            //使用redis中的唯一数加上用户id生成一个ticket
            String ticket = "" + redisUtils.incr(SSO_TAOTAO_TICKET_INCR) + dbUser.getId();
            try {
                //使用固定字符串前缀加上ticket作为redis中的key，用户的json数据作为redis中的value进行存储
                redisUtils.set(SSO_TAOTAO_TICKET_KEY + ticket, OM.writeValueAsString(dbUser), 60 * 30);
                return ticket;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
