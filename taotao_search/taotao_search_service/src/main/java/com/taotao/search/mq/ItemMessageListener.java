package com.taotao.search.mq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
public class ItemMessageListener implements MessageListener {

    @Autowired
    private SearchService searchService;

    private static final ObjectMapper OM = new ObjectMapper();

    /**
     * 接收消息推送
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            try {
                TextMessage textMessage = (TextMessage)message;
                String msg = textMessage.getText();

                if(StringUtils.isNotBlank(msg)) {
                    JsonNode node = OM.readTree(msg);
                    //获得类型
//                    String type = node.get("type").asText();

                    //获得商品id
                    long itemId = node.get("itemId").asLong();

                    //更新索引库
                    searchService.saveItem(itemId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
