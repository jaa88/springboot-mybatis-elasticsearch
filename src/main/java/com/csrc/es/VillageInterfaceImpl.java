package com.csrc.es;

import com.csrc.model.VillageAddressNodeEs;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by jianan on 2018/8/23.
 */
@Service
public class VillageInterfaceImpl implements VillageInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillageInterfaceImpl.class);

    @Autowired
    private JestClient jestClient;

    @Override
    public void saveEntity(VillageAddressNodeEs entity) {
        Index index = new Index.Builder(entity).index(VillageAddressNodeEs.INDEX_NAME).type(VillageAddressNodeEs.TYPE).build();
        try {
            jestClient.execute(index);
            LOGGER.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * 批量保存内容到ES
     */
    @Override
    public void saveEntity(List<VillageAddressNodeEs> entityList) {
        Bulk.Builder bulk = new Bulk.Builder();
        for(VillageAddressNodeEs entity : entityList) {
            Index index = new Index.Builder(entity).index(VillageAddressNodeEs.INDEX_NAME).type(VillageAddressNodeEs.TYPE).build();
            bulk.addAction(index);
        }
        try {
            jestClient.execute(bulk.build());
            LOGGER.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 在ES中搜索内容
     */
    @Override
    public List<VillageAddressNodeEs> searchEntity(String searchContent){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("fullAddressName",searchContent));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(VillageAddressNodeEs.INDEX_NAME).addType(VillageAddressNodeEs.TYPE).build();
        try {
            JestResult result = jestClient.execute(search);
            return result.getSourceAsObjectList(VillageAddressNodeEs.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
