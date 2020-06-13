package com.neu.analysis.dao;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class RealTimeDao {
    private final RestHighLevelClient client;
    @Autowired
    public RealTimeDao(@Qualifier("restHighLevelClient") RestHighLevelClient client) {
        this.client = client;
    }

    public Map<Long,Long> getRealTotal(long start,long end){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<Long,Long> map=new LinkedHashMap<>();
        RangeQueryBuilder range=QueryBuilders.rangeQuery("timestamp").from(start).to(end);
        //ValueCountAggregationBuilder countBuilder=AggregationBuilders.count("pv");
        sourceBuilder.query(range);
        try {
            SearchResponse searchResponse=client.search(request.source(sourceBuilder));
            long pv=searchResponse.getHits().totalHits;
            map.put(end,pv);
            //System.out.println(start+" "+end+" "+pv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
