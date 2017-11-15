package com.taotao.portal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manager.domain.Cart;
import com.taotao.manager.domain.Item;
import com.taotao.manager.service.ItemService;
import com.taotao.portal.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
@Service
public class CartCookieService {

    private static final ObjectMapper OM = new ObjectMapper();

    @Value("${TT_CART}")
    private String TT_CART;

    @Autowired
    private ItemService itemService;


    /**
     * 添加商品到Cookie购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     */
    public void saveItemByCookie(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = queryCartByCookie(request);

        Cart cart = null;
        for (Cart c : carts) {
            if(c.getItemId().longValue() == itemId) {
                cart = c;
                break;
            }
        }

        if(cart != null) {
            //要添加的商品已经在购物车中，只需要修改数量即可
            cart.setNum(cart.getNum() + num);
            cart.setUpdated(new Date());
        } else {
            //要添加的商品部在购物车中，需要添加一个商品进去
            Item item = itemService.findById(itemId);

            cart = new Cart();
            cart.setId(null);
            cart.setUserId(null);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            // 设置商品图片url
            if (item.getImages() != null) {
                cart.setItemImage(item.getImages()[0]);
            } else {
                cart.setItemImage(null);
            }
            cart.setItemPrice(item.getPrice());
            cart.setNum(num);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());

            // 把购物车放在list中
            carts.add(cart);
        }

        // 把修改后的购物车数据放到cookie中
        try {
            CookieUtils.setCookie(request, response, this.TT_CART, OM.writeValueAsString(carts),
                    60 * 60 * 24 * 3, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 从Cookie中查询购物车信息
     * @param request
     * @return
     */
    public List<Cart> queryCartByCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        List<Cart> carts = null;
        //判断Cookie中是否有购物车信息
        if(StringUtils.isNotBlank(json)) {
            //有
            try {
                carts = OM.readValue(json, OM.getTypeFactory().constructCollectionType(List.class, Cart.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return carts;
        }else {
            //没有
            return new ArrayList<>();
        }
    }

    /**
     * 修改Cookie购物车中的信息
     * @param itemId
     * @param num
     * @param request
     * @param response
     */
    public void updateNumByCookie(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = queryCartByCookie(request);

        //迭代购物车，如果购物车中有要修改的商品则直接修改，否则什么也不做
        for (Cart cart : carts) {
            if(cart.getItemId().longValue() == itemId) {
                cart.setNum(num);
                cart.setUpdated(new Date());
                try {
                    CookieUtils.setCookie(request, response, this.TT_CART, OM.writeValueAsString(carts),
                            60 * 60 * 24 * 3, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    /**
     * 从Cookie购物车中删除商品信息
     * @param itemId
     * @param request
     * @param response
     */
    public void deleteItemByCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = queryCartByCookie(request);
        //迭代购物车，如果购物车中有要修改的商品则直接删除，否则什么也不做
        for (Cart cart : carts) {
            if(cart.getItemId().longValue() == itemId) {
                carts.remove(cart);
                try {
                    CookieUtils.setCookie(request, response, this.TT_CART, OM.writeValueAsString(carts),
                            60 * 60 * 24 * 3, true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
