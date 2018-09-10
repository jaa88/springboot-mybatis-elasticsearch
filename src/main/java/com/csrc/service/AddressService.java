package com.csrc.service;

import com.csrc.model.AddressNode;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.DeleteIndex;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by jianan on 2018/9/10.
 */
@Service
public class AddressService {
    private static final Logger logger= LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private JestClient jestClient;

    public void saveEntity(List<AddressNode> entityList) {
        Bulk.Builder bulk = new Bulk.Builder();
        for(AddressNode entity : entityList) {
            Index index = new Index.Builder(entity).index(AddressNode.INDEX_NAME).type(AddressNode.TYPE).build();
            bulk.addAction(index);
        }
        try {
            jestClient.execute(bulk.build());
            logger.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 在ES中搜索内容
     */
    public List<AddressNode> searchEntity(String searchContent) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("fullAddressName",searchContent));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(AddressNode.INDEX_NAME).addType(AddressNode.TYPE).build();



        try {
            JestResult  result = jestClient.execute(search);
            return result.getSourceAsObjectList(AddressNode.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除es index
     */
    public String deleteIndex(String indexName) {
        JestResult jestResult= null;
        try {
            jestResult = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        } catch (IOException e) {
            e.printStackTrace();
            return "失败,原因是："+e.getMessage();
        }
        return jestResult.isSucceeded()?"成功":"失败";
    }

}
