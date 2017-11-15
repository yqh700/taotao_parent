package com.taotao.portal.web.controller;

import com.taotao.cart.service.CartService;
import com.taotao.manager.domain.Cart;
import com.taotao.manager.domain.User;
import com.taotao.portal.service.CartCookieService;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartCookieService cartCookieService;

    @Value("${TT_TICKET}")
    private String TT_TICKET;

    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String saveItemByCart(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        //获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, TT_TICKET);
        User user = userService.queryUserByTicket(ticket);

        if(user != null) {
            //已登录
            cartService.saveItemByCart(user.getId(), itemId, num);
        } else {
            //未登录
            cartCookieService.saveItemByCookie(itemId, num, request, response);
        }

        return "redirect:/cart/show.html";
    }

    /**
     * 显示购物车详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showCart(Model model, HttpServletRequest request) {
        //获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, TT_TICKET);
        User user = userService.queryUserByTicket(ticket);

        //构建一个集合存储购物车信息
        List<Cart> cartList = new ArrayList<>();

        if(user != null) {
            //已登录
            cartList = cartService.queryCartByUserId(user.getId());
        } else {
            //未登录
            cartList = cartCookieService.queryCartByCookie(request);
        }

        //保存购物车到模型中
        model.addAttribute("cartList", cartList);

        return "cart";
    }

    /**
     * 修改购物车数量
     * @param itemId 商品id
     * @param num 商品数量
     * @param request
     */
    @RequestMapping(value = "/update/num/{itemId}/{num}", method = RequestMethod.POST)
    public void updateNumByCart(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        //获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, TT_TICKET);
        User user = userService.queryUserByTicket(ticket);

        if(user != null) {
            //用户已登录
            cartService.updateNumByCart(user.getId(), itemId, num);
        }else {
            //用户未登录
            cartCookieService.updateNumByCookie(itemId, num, request, response);
        }
    }

    /**
     *
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.GET)
    public String deleteItemByCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //获取用户登录信息
        String ticket = CookieUtils.getCookieValue(request, TT_TICKET);
        User user = userService.queryUserByTicket(ticket);

        if(user != null) {
            //用户已登录
            cartService.deleteItemByCart(user.getId(), itemId);
        } else {
            //用户未登录
            cartCookieService.deleteItemByCart(itemId, request, response);
        }

        return "redirect:/cart/show.html";
    }
}
