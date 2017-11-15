package com.taotao.item.mq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manager.domain.Item;
import com.taotao.manager.domain.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import com.taotao.manager.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 杨清华.
 * on 2017/11/15.
 */
public class ItemMessageListener implements MessageListener {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    @Value("${TAOTAO_ITEM_HTML_PATH}")
    private String TAOTAO_ITEM_HTML_PATH;

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

                    //生成静态页面
                    getHtml(itemId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成静态页面
     * @param itemId
     */
    private void getHtml(long itemId) throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("item.ftl");

        //从数据库中查询商品和商品详情数据
        Item item = null;

        //商品
        while(true) {
            item = itemService.findById(itemId);
            if(item != null) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //商品详情
        ItemDesc itemDesc = itemDescService.findById(itemId);

        Map<String, Object> root = new HashMap<>();
        root.put("item", item);
        root.put("itemDesc", itemDesc);

        Writer out = new FileWriter(new File(TAOTAO_ITEM_HTML_PATH + itemId + ".html"));
        template.process(root, out);
        out.close();
    }
}
