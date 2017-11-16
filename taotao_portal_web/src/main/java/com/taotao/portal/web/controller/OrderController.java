package com.taotao.portal.web.controller;

import com.taotao.cart.service.CartService;
import com.taotao.manager.domain.Cart;
import com.taotao.manager.domain.Order;
import com.taotao.manager.domain.User;
import com.taotao.order.service.OrderService;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.service.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 杨清华.
 * on 2017/11/16.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Value("${TT_TICKET}")
    private String TT_TICKET;


    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpServletRequest request) {
        //获取用户登录信息
        User user = (User) request.getAttribute("user");

        //从购物车系统中获得当前用户的购物车数据
        List<Cart> carts = cartService.queryCartByUserId(user.getId());
        model.addAttribute("carts", carts);

        return "order-cart";
    }


    /**
     * 提交订单
     * @param order
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveOrder(Order order, HttpServletRequest request) {
        //获得用户登录信息
        User user = (User) request.getAttribute("user");
        //为订单设置用户信息
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        //创建订单并返回订单号
        String orderId = orderService.saveOrder(order);

        Map<String, Object> map = new HashMap<>();
        map.put("status", 200);
        map.put("data", orderId);
        return map;
    }

    /**
     * 跳转到提交订单成功页面
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success(@RequestParam("id") String orderId, Model model) {
        //根据订单id查询订单信息
        Order order = orderService.queryOrderByOrderId(orderId);

        String date = new DateTime().plusDays(2).toString("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒");
        model.addAttribute("order", order);
        model.addAttribute("date", date);

        return "success";
    }
}
