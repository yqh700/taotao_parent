import com.github.pagehelper.PageHelper;
import com.taotao.manager.domain.Item;
import com.taotao.manager.mapper.ItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
public class ItemImport {

    private CloudSolrServer cloudSolrServer;

    private ItemMapper itemMapper;

    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        cloudSolrServer = context.getBean(CloudSolrServer.class);
        itemMapper = context.getBean(ItemMapper.class);
    }

    @Test
    public void test() throws IOException, SolrServerException {
        int start = 1, size = 500;
        do {
            PageHelper.startPage(start, size);
            List<Item> items = itemMapper.select(null);

            List<SolrInputDocument> docs = new ArrayList<>();
            for (Item item : items) {
                SolrInputDocument document = new SolrInputDocument();
                // 商品id
                document.setField("id", item.getId().toString());
                // 商品名称
                document.setField("item_title", item.getTitle());
                // 商品价格
                document.setField("item_price", item.getPrice());
                // 商品图片
                if (StringUtils.isNotBlank(item.getImage())) {
                    document.setField("item_image", StringUtils.split(item.getImage(), ",")[0]);
                } else {
                    document.setField("item_image", "");
                }
                // 商品类目id
                document.setField("item_cid", item.getCid());
                // 商品状态
                document.setField("item_status", item.getStatus());

                // 把Document放到集合中，统一提交
                docs.add(document);
            }

            cloudSolrServer.add(docs);
            cloudSolrServer.commit();

            start++;
            size = items.size();
        }while (size == 500);
    }
}
