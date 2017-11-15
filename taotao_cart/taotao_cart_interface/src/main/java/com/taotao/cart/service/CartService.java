package com.taotao.cart.service;

import com.taotao.manager.domain.Cart;

import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
public interface CartService {

    /**
     * 保存商品到购物车
     * @param id 用户id
     * @param itemId 商品id
     * @param num 数量
     */
    void saveItemByCart(Long id, Long itemId, Integer num);

    /**
     * 查询用户购物车信息
     * @param id 用户id
     * @return
     */
    List<Cart> queryCartByUserId(Long id);

    /**
     * 修改购物车内商品数量
     * @param id
     * @param itemId
     * @param num
     */
    void updateNumByCart(Long id, Long itemId, Integer num);

    /**
     * 删除购物车中的商品
     * @param id
     * @param itemId
     */
    void deleteItemByCart(Long id, Long itemId);
}
