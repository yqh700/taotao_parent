package com.taotao.order.service.impl;

import com.taotao.manager.domain.Order;
import com.taotao.manager.domain.OrderItem;
import com.taotao.manager.domain.OrderShipping;
import com.taotao.manager.mapper.OrderItemMapper;
import com.taotao.manager.mapper.OrderMapper;
import com.taotao.manager.mapper.OrderShippingMapper;
import com.taotao.order.redis.RedisUtils;
import com.taotao.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/16.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${ORDER_TAOTAO_ORDERID_INCR}")
    private String ORDER_TAOTAO_ORDERID_INCR;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Override
    public String saveOrder(Order order) {
        //创建订单id，用户Id + 唯一数
        String orderId = "" + order.getUserId() + redisUtils.incr(ORDER_TAOTAO_ORDERID_INCR);
        // 保存订单数据
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());

        orderMapper.insertSelective(order);

        //保存订单内的商品数据
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }

        // 保存订单物流数据
        OrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(order.getCreateTime());
        orderShipping.setUpdated(orderShipping.getCreated());

        orderShippingMapper.insertSelective(orderShipping);
        return orderId;
    }

    @Override
    public Order queryOrderByOrderId(String orderId) {
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);

        //查询订单商品信息
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        List<OrderItem> orderItems = orderItemMapper.select(orderItem);

        order.setOrderItems(orderItems);

        //查询订单物流信息
        OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
        order.setOrderShipping(orderShipping);

        return order;
    }
}
