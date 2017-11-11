package com.taotao.sso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manager.domain.User;
import com.taotao.manager.mapper.UserMapper;
import com.taotao.sso.redis.RedisUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Value("${TAOTAO_TICKET_KEY}")
    private String TAOTAO_TICKET_KEY;

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
        String json = redisUtils.get(TAOTAO_TICKET_KEY + ticket);

        if(StringUtils.isNotBlank(json)) {
            try {
                User user = OM.readValue(json, User.class);
                redisUtils.expire(TAOTAO_TICKET_KEY + ticket, 60 * 30);
                return user;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
