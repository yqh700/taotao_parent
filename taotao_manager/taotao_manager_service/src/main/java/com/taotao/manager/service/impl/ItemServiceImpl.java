package com.taotao.manager.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Item;
import com.taotao.manager.domain.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import com.taotao.manager.service.ItemService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 杨清华.
 * on 2017/11/7.
 */
@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    @Autowired
    private ItemDescService itemDescService;

    /*消息模板工具类*/
    @Autowired
    private JmsTemplate jmsTemplate;

    /*Destination消息目的地*/
    @Autowired
    private Destination destination;

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public void saveItem(Item item, String desc) {
        //保存商品
        item.setStatus(1);
        super.save(item);

        //保存商品描述
        ItemDesc param = new ItemDesc();
        param.setItemId(item.getId());
        param.setItemDesc(desc);

        itemDescService.save(param);

        //发送消息
        sendMQ("save", item.getId());
    }

    @Override
    public TaoResult<Item> queryItemList(Integer page, Integer rows) {
        List<Item> list = super.findByPage(page, rows);

        TaoResult<Item> result = new TaoResult<>();
        PageInfo<Item> info = new PageInfo<>(list);
        result.setTotal(info.getTotal());
        result.setRows(new ArrayList<>(list));
        return result;
    }

    /**
     * 用消息中间件发送消息
     * @param type
     * @param itemId
     */
    private void sendMQ(final String type, final Long itemId) {

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //构建消息
                TextMessage message = new ActiveMQTextMessage();
                Map<String, Object> map = new HashMap<>();
                map.put("type", type);
                map.put("itemId", itemId);

                try {
                    message.setText(OM.writeValueAsString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return message;
            }
        });
    }
}
