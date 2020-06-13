package com.neu.analysis.dao;


import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class HisDao {
    private final RestHighLevelClient client;
    @Autowired
    public HisDao(@Qualifier("restHighLevelClient") RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * UV大于IP：这种情况就是在网吧、学校、公司等，公用相同IP的场所中不同的用户，或者多种不同浏览器访问您网站，那么UV数会大于IP数。
     * UV小于IP：在家庭中大多数电脑使用ADSL拨号上网，所以同一个用户在家里不同时间访问您网站时，IP可能会不同，因为它会根据时间变动IP，
     * 即动态的IP地址，但是实际访客数唯一，便会出现UV数小于IP数。
     * **/
    public Map<String,Long[]> getHisTotal(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long[]> map=new LinkedHashMap<>();

        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("pv").field("date").order(BucketOrder.key(true));
        CardinalityAggregationBuilder termsBuilder1 = AggregationBuilders.cardinality("uv").field("uid");
        CardinalityAggregationBuilder termsBuilder2 = AggregationBuilders.cardinality("ip").field("ip");
        CardinalityAggregationBuilder termsBuilder3 = AggregationBuilders.cardinality("vv").field("session_id");
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1)
                                              .subAggregation(termsBuilder2)
                                              .subAggregation(termsBuilder3));
        try {
            SearchResponse searchResponse=client.search(request.source(sourceBuilder));
            Terms terms=searchResponse.getAggregations().get("pv");
            for(Terms.Bucket bucket:terms.getBuckets()){
                Long[] data=new Long[4];
                String date=bucket.getKey().toString();
                Long pv=bucket.getDocCount();
                Cardinality uv=bucket.getAggregations().get("uv");
                Cardinality vv=bucket.getAggregations().get("vv");
                Cardinality ip=bucket.getAggregations().get("ip");
                data[0]=pv;
                data[1]=uv.getValue();
                data[2]=vv.getValue();
                data[3]=ip.getValue();
                map.put(date,Arrays.copyOf(data,data.length));
                //System.out.println("date: "+date+",pv: "+data[0]+",uv: "+data[1]+",vv: "+data[2]+",ip: "+data[3]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public Map<String,Long[]> getHisFlow(String start,String end) {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String, Long[]> map = new LinkedHashMap<>();
        TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("date").field("date").order(BucketOrder.key(false));
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("referrer").field("referrer");
        RangeQueryBuilder range=QueryBuilders.rangeQuery("date").from(start).to(end);
        sourceBuilder.query(range).aggregation(termsBuilder1.subAggregation(termsBuilder2));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client.search(request.source(sourceBuilder));
            Terms terms=searchResponse.getAggregations().get("date");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String title=bucket.getKey().toString();//date
                Terms terms1=bucket.getAggregations().get("referrer");
                Long[] source={0L,0L,0L,0L,0L};
                for(Terms.Bucket bucket1:terms1.getBuckets()){
                    String ref=bucket1.getKey().toString();
                    long num=bucket1.getDocCount();
                    if("main".equals(ref)){
                        source[0]=num;
                    }else if("search".equals(ref)){
                        source[1]=num;
                    }else if("suggestion".equals(ref)){
                        source[2]=num;
                    }else if("share".equals(ref)){
                        source[3]=num;
                    }else {
                        source[4]=num;
                    }
                }
                map.put(title,source);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
