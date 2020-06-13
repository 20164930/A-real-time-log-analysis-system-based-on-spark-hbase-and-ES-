package sync;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ESTools {

    private final static int port = 9300;
    private final static String host = "47.92.249.70";
    static TransportClient client;
    public static void main(String[] args){
        TransportClient client=getClient();
        //deleteIndex(client);
        //createMapping(client);
        //deleteData(client);
        insert(client);
    }
    public static TransportClient getClient(){
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es")//设置es集群名称
                .build();
        client = null;
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static void closeClient(){
        client.close();
    }

    public static void createIndex(TransportClient client){
        AdminClient admin = client.admin();
        IndicesAdminClient indices = admin.indices();
        indices.prepareCreate("web_log").get();
    }

    public static void createMapping(TransportClient client){
        XContentBuilder mapping=null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    .field("dynamic", "false").startObject("properties")
                        .startObject("uid")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("ip")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("local_list")
                            .field("type", "integer")
                        .endObject()
                        .startObject("date")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("timestamp")
                            .field("type", "long")
                        .endObject()
                        .startObject("session_id")
                            .field("type", "long")
                        .endObject()
                        .startObject("referrer")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("params").field("type","nested")
                            .startObject("properties")
                                .startObject("action_num")
                                    .field("type","keyword")
                                .endObject()
                                .startObject("action_type")
                                    .field("type","keyword")
                                .endObject()
                                .startObject("param2").field("type","nested")
                                    .startObject("properties")
                                        .startObject("keyword")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("result_num")
                                            .field("type","long")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param5").field("type","nested")
                                    .startObject("properties")
                                        .startObject("comment_id")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("content")
                                            .field("type","text")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param6").field("type","nested")
                                    .startObject("properties")
                                        .startObject("comment_id")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("content")
                                            .field("type","text")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param7").field("type","nested")
                                    .startObject("properties")
                                        .startObject("comment_id")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("content")
                                            .field("type","text")
                                        .endObject()
                                        .startObject("likes")
                                            .field("type","long")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param8").field("type","nested")
                                    .startObject("properties")
                                        .startObject("url")
                                            .field("type","text")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param10").field("type","nested")
                                    .startObject("properties")
                                        .startObject("id")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("title")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("singer")
                                            .field("type","keyword")
                                        .endObject()
                                    .endObject()
                                .endObject()
                                .startObject("param9").field("type","nested")
                                    .startObject("properties")
                                        .startObject("id")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("title")
                                            .field("type","keyword")
                                        .endObject()
                                        .startObject("singer")
                                            .field("type","keyword")
                                        .endObject()
                                    .endObject()
                                .endObject()

                            .endObject()
                        .endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutMappingRequest mappingRequest = Requests.putMappingRequest("web_log").type("logs").source(mapping);
        client.admin().indices().putMapping(mappingRequest).actionGet();
    }

    public static void insert(TransportClient client){
        Map<String, Object> source = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> subParam = new HashMap<String, Object>();
        String index_id="10281733749832649842";
        Integer[] local_list = new Integer[]{1,22,77};
        source.put("uid", "haonanren");
        source.put("ip", "123.123.123.123");
        source.put("local_list", local_list);
        source.put("date", "20200307");
        source.put("timestamp", "1588248200837");
        source.put("session_id", 72654);
        source.put("referrer", "main");

        params.put("action_num",2);
        params.put("action_type","search");

        subParam.put("keyword","左手指月");
        subParam.put("result_num","15");

        params.put("param2",subParam);
        source.put("params",params);
        IndexResponse response = client.prepareIndex("web_log", "logs",index_id)
                .setSource(source).execute().actionGet();
        System.out.println("status:"+response.status().getStatus()+" , response id : "+response.getId());
    }

    public static void search(TransportClient client){
        SearchResponse result = client.prepareSearch("books")
                .setTypes("book1")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("name", "action"))
                .get();
        SearchHits hits  = result.getHits();
        for(SearchHit hit:hits){
            System.out.println(hit.getId()+" "+hit.getSourceAsMap());
        }
    }

    public static void deleteData(TransportClient client) {
        DeleteResponse deleteResponse = client
                .prepareDelete("web_log", "logs", "10281783649832649832")
                .get();
    }

    public static void deleteIndex(TransportClient client){
        client.admin().indices().prepareDelete("web_log").execute().actionGet();
        createIndex(client);
    }


}
