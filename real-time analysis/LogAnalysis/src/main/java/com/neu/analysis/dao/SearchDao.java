package com.neu.analysis.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SearchDao {
    private final RestHighLevelClient client;
    private Connection connection;

    @Autowired
    public SearchDao(@Qualifier("restHighLevelClient") RestHighLevelClient client,Connection connection) {
        this.client = client;
        this.connection=connection;
    }

    public List<String> getHisSearch(Map<String,List<String>> map) {
        List<String> re= new ArrayList<>();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder[] filters=new QueryBuilder[map.size()];
        int i=0;
        for(Map.Entry<String,List<String>> entry:map.entrySet()){
            String key=entry.getKey();
            String temp;
            filters[i]=QueryBuilders.termsQuery(key,entry.getValue());
            //System.out.println(key+" "+entry.getValue());
            if(key.startsWith("param")){
                int n=Integer.parseInt(key.substring(5,6));
                if(n==1){
                    n=10;
                    temp = key.substring(8, key.length());
                }else {
                    temp = key.substring(7, key.length());
                }
                QueryBuilder tmpFilter=QueryBuilders.termsQuery("params.param"+n+"."+temp,entry.getValue());
                NestedQueryBuilder nestedQuery=QueryBuilders.nestedQuery("params",
                        QueryBuilders.nestedQuery("params.param"+n,tmpFilter,ScoreMode.None),
                        ScoreMode.None);
                boolQueryBuilder.must(nestedQuery);
            }else {
                boolQueryBuilder.must(filters[i]);
            }
            i++;
        }
        sourceBuilder.query(boolQueryBuilder).size(100);
        try {
            SearchResponse searchResponse = client.search(request.source(sourceBuilder));
            SearchHits hits=searchResponse.getHits();
            //System.out.println(hits.getTotalHits());
            for (SearchHit hit:hits) {
                String id=hit.getId();
                if(id!=null) {
                    re.add(id);
                    System.out.println(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re;
    }


    public Map<String,String>[] getData(List<String> rowkeys) {
        List<Get> getList = new ArrayList<>();
        Table table = null;
        Result[] results=null;

        try {
            table = connection.getTable(TableName.valueOf("webLog"));
            for (String rowkey : rowkeys) {
                Get get = new Get(Bytes.toBytes(rowkey));
                getList.add(get);
            }
            results = new Result[0];
            results = table.get(getList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String,String>[] re=new Map[results.length];
        int i=0;
        for (Result result : results){
            Map<String,String> subMap=new HashMap<>();
            for (Cell kv : result.rawCells()) {
                String key=Bytes.toString(CellUtil.cloneQualifier(kv));
                String value = Bytes.toString(CellUtil.cloneValue(kv));
                subMap.put(key,value);
            }
            re[i]=subMap;
            i++;
        }
        System.out.println(re.length);
        return re;
    }
}
