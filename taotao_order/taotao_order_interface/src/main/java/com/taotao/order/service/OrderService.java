package com.taotao.order.service;

import com.taotao.manager.domain.Order;

/**
 * Created by 杨清华.
 * on 2017/11/16.
 */
public interface OrderService {

    /**
     * 创建订单
     * @param order
     * @return
     */
    String saveOrder(Order order);

    /**
     * 根据订单id查询订单信息
     * @param orderId
     * @return
     */
    Order queryOrderByOrderId(String orderId);
}
