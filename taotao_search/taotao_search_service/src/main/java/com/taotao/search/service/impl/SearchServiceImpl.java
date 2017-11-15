package com.taotao.search.service.impl;

import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Item;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CloudSolrServer cloudSolrServer;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public TaoResult<Item> search(String keyword, Integer page, Integer rows) {
        //构建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询语句
        if(StringUtils.isNotBlank(keyword)) {
            //有关键词
            solrQuery.setQuery("item_title:" + keyword + " AND item_status:1");
        } else {
            //没有关键词，查询所有
            solrQuery.setQuery("item_status:1");
        }
        //设置查询分页
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);

        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //构建返回对象
        TaoResult<Item> taoResult = new TaoResult<>();

        try {
            //查询
            QueryResponse queryResponse = cloudSolrServer.query(solrQuery);
            SolrDocumentList results = queryResponse.getResults();

            //获得高亮数据
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

            //解析查询到的结果集
            //声明一个集合用户存放解析后的商品对象
            List<Item> list = new ArrayList<>();
            
            //迭代解析
            for (SolrDocument solrDocument : results) {
                Item item = new Item();
                //id
                item.setId(Long.parseLong(solrDocument.get("id").toString()));
                //高亮标题
                List<String> hlist = highlighting.get(solrDocument.get("id").toString()).get("item_title");
                if(hlist != null && hlist.size() > 0) {
                    item.setTitle(hlist.get(0));
                } else {
                    item.setTitle(solrDocument.get("item_title").toString());
                }
                item.setImage(solrDocument.get("item_image").toString());
                //价格
                item.setPrice(Long.parseLong(solrDocument.get("item_price").toString()));
                //商品分类id
                item.setCid(Long.parseLong(solrDocument.get("item_cid").toString()));

                //将封装好的商品对象放入集合中
                list.add(item);
            }

            taoResult.setRows(list);
            taoResult.setTotal(results.getNumFound());

            return taoResult;
        } catch (SolrServerException e) {
            e.printStackTrace();
        }


        return taoResult;
    }

    @Override
    public void saveItem(long itemId) {
        //查询要更新的商品
        Item item = null;
        while(true) {
            item = itemMapper.selectByPrimaryKey(itemId);
            if(item != null) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //构建文档进行更新
        SolrInputDocument document = new SolrInputDocument();
        // 商品id
        document.setField("id", item.getId().toString());
        // 商品标题
        document.setField("item_title", item.getTitle());
        // 商品价格
        document.setField("item_price", item.getPrice());
        // 商品图片
        document.setField("item_image", item.getImage());
        // 商品类目id
        document.setField("item_cid", item.getCid());
        // 商品状态
        document.setField("item_status", item.getStatus());

        try {
            cloudSolrServer.add(document);
            cloudSolrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
