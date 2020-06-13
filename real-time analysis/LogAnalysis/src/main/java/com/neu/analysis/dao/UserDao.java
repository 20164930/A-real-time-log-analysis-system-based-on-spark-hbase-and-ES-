package com.neu.analysis.dao;

import com.neu.analysis.configuration.MapLocal;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class UserDao {
    private final RestHighLevelClient client;

    private final RestHighLevelClient client1;

    private final RestHighLevelClient client2;

    private final RestHighLevelClient client3;

    @Autowired
    public UserDao(@Qualifier("restHighLevelClient") RestHighLevelClient client,
                   @Qualifier("restHighLevelClient") RestHighLevelClient client1,
                   @Qualifier("restHighLevelClient") RestHighLevelClient client2,
                   @Qualifier("restHighLevelClient") RestHighLevelClient client3) {
        this.client = client;
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = client3;
    }

    public Map<String,Long> getUserLocal(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("local").field("local_list").order(BucketOrder.count(false)).size(34);
        sourceBuilder.aggregation(termsBuilder);
        SearchResponse searchResponse=null;
        try {
            searchResponse = client.search(request.source(sourceBuilder));
            Terms terms=searchResponse.getAggregations().get("local");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String local=bucket.getKey().toString();
                long num=bucket.getDocCount();
                int ll=Integer.parseInt(local);
                if(ll>=1 && ll<=34){
                    //System.out.println("dao: "+MapLocal.getMap(local)+" "+local+" "+num);
                    map.put(MapLocal.getMap(local),num);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Long> getUserFavoriteSong(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        NestedAggregationBuilder termsBuilder = AggregationBuilders.nested("params","params");
        NestedAggregationBuilder termsBuilder1 = AggregationBuilders.nested("param9","params.param9");
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("title").field("params.param9.title").order(BucketOrder.count(false)).size(10);
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1.subAggregation(termsBuilder2)));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client.search(request.source(sourceBuilder));
            Nested nested=searchResponse.getAggregations().get("params");
            Nested nested1=nested.getAggregations().get("param9");
            Terms terms=nested1.getAggregations().get("title");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String title=bucket.getKey().toString();
                long num=bucket.getDocCount();
                map.put(title,num);
                //System.out.println(title+" "+num);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Long> getUserFavoriteSinger(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        NestedAggregationBuilder termsBuilder = AggregationBuilders.nested("params","params");
        NestedAggregationBuilder termsBuilder1 = AggregationBuilders.nested("param9","params.param9");
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("singer").field("params.param9.singer").order(BucketOrder.count(false)).size(10);
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1.subAggregation(termsBuilder2)));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client1.search(request.source(sourceBuilder));
            Nested nested=searchResponse.getAggregations().get("params");
            Nested nested1=nested.getAggregations().get("param9");
            Terms terms=nested1.getAggregations().get("singer");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String title=bucket.getKey().toString();
                long num=bucket.getDocCount();
                map.put(title,num);
                //System.out.println(title+" "+num);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Long> getHisUserFavoriteRMSong(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        NestedAggregationBuilder termsBuilder = AggregationBuilders.nested("params","params");
        NestedAggregationBuilder termsBuilder1 = AggregationBuilders.nested("param10","params.param10");
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("title").field("params.param10.title").order(BucketOrder.count(false)).size(10);
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1.subAggregation(termsBuilder2)));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client2.search(request.source(sourceBuilder));
            Nested nested=searchResponse.getAggregations().get("params");
            Nested nested1=nested.getAggregations().get("param10");
            Terms terms=nested1.getAggregations().get("title");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String title=bucket.getKey().toString();
                long num=bucket.getDocCount();
                map.put(title,num);
                //System.out.println(title+" "+num);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Long> getUserFavoriteRMSinger(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        NestedAggregationBuilder termsBuilder = AggregationBuilders.nested("params","params");
        NestedAggregationBuilder termsBuilder1 = AggregationBuilders.nested("param10","params.param10");
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("singer").field("params.param10.singer").order(BucketOrder.count(false)).size(10);
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1.subAggregation(termsBuilder2)));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client3.search(request.source(sourceBuilder));
            Nested nested=searchResponse.getAggregations().get("params");
            Nested nested1=nested.getAggregations().get("param10");
            Terms terms=nested1.getAggregations().get("singer");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String title=bucket.getKey().toString();
                long num=bucket.getDocCount();
                map.put(title,num);
                //System.out.println(title+" "+num);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public Map<String,Long> getUserSearch(){
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Map<String,Long> map=new LinkedHashMap<>();
        NestedAggregationBuilder termsBuilder = AggregationBuilders.nested("params","params");
        NestedAggregationBuilder termsBuilder1 = AggregationBuilders.nested("param2","params.param2");
        TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("keyword").field("params.param2.keyword").order(BucketOrder.count(false)).size(10);
        sourceBuilder.aggregation(termsBuilder.subAggregation(termsBuilder1.subAggregation(termsBuilder2)));
        SearchResponse searchResponse=null;
        try {
            searchResponse = client.search(request.source(sourceBuilder));
            Nested nested=searchResponse.getAggregations().get("params");
            Nested nested1=nested.getAggregations().get("param2");
            Terms terms=nested1.getAggregations().get("keyword");
            for(Terms.Bucket bucket:terms.getBuckets()){
                String keyword=bucket.getKey().toString();
                long num=bucket.getDocCount();
                map.put(keyword,num);
                //System.out.println(keyword+" "+num);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
