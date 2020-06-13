package sync;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.elasticsearch.client.transport.TransportClient;
import com.alibaba.fastjson.JSON;
import sync.ESBulk;
import sync.ESTools;

import java.io.IOException;
import java.util.*;

public class HbaseSyncEs implements RegionObserver, RegionCoprocessor {

    static TransportClient client;
    static Set<String> set;
    static Map<Integer,String[]> map;
    public void start(CoprocessorEnvironment env) throws IOException {
        client=ESTools.getClient();
        set=new HashSet<>();
        set.add("uid");
        set.add("ip");
        set.add("local_list");
        set.add("date");
        set.add("timestamp");
        set.add("session_id");
        set.add("referrer");
        set.add("params");

        map=new HashMap<>();
        map.put(2,new String[]{"keyword","result_num"});
        map.put(5,new String[]{"comment_id","content"});
        map.put(6,new String[]{"comment_id","content"});
        map.put(7,new String[]{"comment_id","content","likes"});
        map.put(8,new String[]{"url"});
        map.put(9,new String[]{"id","title","singer"});
        map.put(10,new String[]{"id","title","singer"});
    }

    public void stop(CoprocessorEnvironment env) throws IOException {
        client.close();
        ESBulk.shutdownScheduEx();
    }

    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    public void postPut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
            String indexId = new String(put.getRow());
            NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
            Map<String, Object> json = new HashMap<String, Object>();

            for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
                for (Cell cell : entry.getValue()) {
                    String key = Bytes.toString(CellUtil.cloneQualifier(cell));
                    if(set.contains(key)) {
                        String value = Bytes.toString(CellUtil.cloneValue(cell));
                        if(("local_list").equals(key)){
                            String[] list=value.split("_");
                            json.put(key,list);
                        }else if(("params").equals(key)){
                            Map<String, Object> params = new HashMap<>();
                            Map<String, Object> subParam = new HashMap<>();
                            JSONObject paramObj=JSON.parseObject(value);
                            int type=paramObj.getInteger("action_num");
                            if(map.containsKey(type)) {
                                params.put("action_num", type);
                                params.put("action_type", paramObj.get("action_type"));

                                String[] attributes = map.get(type);
                                for (String attribute : attributes) {
                                    subParam.put(attribute, paramObj.get(attribute));
                                }
                                params.put("param" + type, subParam);
                                json.put(key, params);
                            }
                        }else{
                            json.put(key, value);
                        }
                    }
                }
            }
            // set hbase family to es
            ESBulk.addUpdateBuilderToBulk(ESTools.client.prepareUpdate("web_log","logs", indexId).setDocAsUpsert(true).setDoc(json));

    }
}
