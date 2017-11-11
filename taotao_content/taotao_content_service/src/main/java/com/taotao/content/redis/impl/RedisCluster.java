package com.taotao.content.redis.impl;

import com.taotao.content.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
public class RedisCluster implements RedisUtils {

    @Autowired
    private JedisCluster jedisCluster;


    @Override
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public void del(String key) {
        jedisCluster.del(key);
    }

    @Override
    public void expire(String key, Integer seconds) {
        jedisCluster.expire(key, seconds);
    }

    @Override
    public void set(String key, String value, Integer seconds) {
        set(key, value);
        expire(key, seconds);
    }

    @Override
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }
}
