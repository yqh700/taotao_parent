package com.taotao.cart.redis;

/**
 * Created by 杨清华.
 * on 2017/11/11.
 */
public interface RedisUtils {

    public void set(String key, String value);

    public String get(String key);

    public void del(String key);

    public void expire(String key, Integer seconds);

    public void set(String key, String value, Integer seconds);

    public Long incr(String key);

}