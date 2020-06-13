package Spark;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HbaseTools {
    private String zookeeperQuorm="47.92.249.70,39.99.188.48,39.100.237.186";
    private String zookeeperport="2181";
    private static final String TOPIC="test0";

    public static void main(String[] args){
        Connection conn=getConnection();
        getDatas(conn);
    }

    public static Connection getConnection(){
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "47.92.249.70,39.99.188.48,39.100.237.186");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createTable(Connection conn){
        TableName tableName=TableName.valueOf("webLog");
        String[] columnFamilys=new String[]{"main"};
        try {
            HBaseAdmin hAdmin = (HBaseAdmin) conn.getAdmin();
            if (hAdmin.tableExists(tableName)) {
                System.out.println(tableName + "表已存在");
                conn.close();
                System.exit(0);
            } else {
                // 新建一个表描述
                TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(tableName);
                ColumnFamilyDescriptorBuilder cdb;
                ColumnFamilyDescriptor cfd ;
                // 在表描述里添加列族
                for (String columnFamily : columnFamilys) {
                    cdb=ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                    cfd=cdb.build();
                    //添加列族
                    tdb.setColumnFamily(cfd);
                }
                TableDescriptor td = tdb.build();
                // 根据配置好的表描述建表
                hAdmin.createTable(td);
                System.out.println("创建" + tableName + "表成功");
            }
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getDatas(Connection conn){
        Set<String> set=new HashSet<String>();
        set.add("uid");
        set.add("params");
        try {
            Table table= conn.getTable(TableName.valueOf("webLog"));
            Scan scan = new Scan();
            ResultScanner resultScanner = table.getScanner(scan);
            for(Result result: resultScanner){
                Cell[] cells=result.rawCells();
                for(int i=0;i<cells.length;i++){
                    String key=Bytes.toString(CellUtil.cloneQualifier(cells[i]));
                    String value=Bytes.toString(CellUtil.cloneValue(cells[i]));
                    if(set.contains(key)){
                        System.out.println(key+": "+value);
                    }
                }
                //byte[] uid=result.getValue(Bytes.toBytes("main"),Bytes.toBytes("uid"));
                //byte[] params=result.getValue(Bytes.toBytes("main"),Bytes.toBytes("params"));
                //System.out.println(Bytes.toString(uid)+" "+Bytes.toString(params));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
