package com.taotao.cart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.redis.RedisUtils;
import com.taotao.cart.service.CartService;
import com.taotao.manager.domain.Cart;
import com.taotao.manager.domain.Item;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
@Service
public class CartServiceImpl implements CartService {

    @Value("${TAOTAO_CART_KEY}")
    private String TAOTAO_CART_KEY;

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void saveItemByCart(Long id, Long itemId, Integer num) {
        //从Redis中查询出当前用户所有的购物车信息
        List<Cart> carts = queryCartByUserId(id);
        //在购车列表中找到当前要添加的商品
        Cart cart = null;

        for (Cart c : carts) {
            if(c.getItemId().longValue() == itemId) {
                cart = c;
                break;
            }
        }

        if(cart != null) {
            //如果购物车中存在当前商品，则在这基础之上进行数量添加
            cart.setNum(cart.getNum() + num);
            cart.setUpdated(new Date());
        } else {
            //如果购物车中不存在要添加的商品，则创建一个商品购物车，然后将其添加到列表中
            Item item = itemMapper.selectByPrimaryKey(itemId);
            cart = new Cart();
            cart.setUserId(id);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImage());
            cart.setNum(num);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());

            carts.add(cart);
        }

        try {
            redisUtils.set(TAOTAO_CART_KEY + id, OM.writeValueAsString(carts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cart> queryCartByUserId(Long id) {
        //从Redis中查询当前用户的购物车信息
        String json = redisUtils.get(TAOTAO_CART_KEY + id);

        //判断数据是否为空
        if(StringUtils.isNotBlank(json)) {
            //将数据反序列化为List<Cart>集合
            try {
                List<Cart> carts = OM.readValue(json, OM.getTypeFactory().constructCollectionType(List.class, Cart.class));
                return carts;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //如果为空返回一个空的集合
        return new ArrayList<>();
    }

    @Override
    public void updateNumByCart(Long id, Long itemId, Integer num) {
        //查询当前用户的购物车信息
        List<Cart> carts = queryCartByUserId(id);

        for (Cart cart : carts) {
            if(cart.getItemId().longValue() == itemId) {
                cart.setNum(num);
                cart.setUpdated(new Date());
                //修改完成后存储到Redis中
                try {
                    redisUtils.set(TAOTAO_CART_KEY + id, OM.writeValueAsString(carts));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void deleteItemByCart(Long id, Long itemId) {
        //查询当前用户的购物车信息
        List<Cart> carts = queryCartByUserId(id);

        for (Cart cart : carts) {
            if(cart.getItemId().longValue() == itemId) {
                carts.remove(cart);
                //删除完成后存储到Redis中
                try {
                    redisUtils.set(TAOTAO_CART_KEY + id, OM.writeValueAsString(carts));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
