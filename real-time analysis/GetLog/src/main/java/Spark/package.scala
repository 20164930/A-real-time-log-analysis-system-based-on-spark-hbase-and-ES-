import java.util

import org.apache.spark.sql.{Row, SparkSession, functions}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import java.util.{LinkedHashMap => JLinkedHashMap}

import com.alibaba.fastjson.JSON
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes

package object SparkTools{

  val broker="192.168.17.128:9092,192.168.17.129:9092,192.168.17.130:9092"
  val groupid="cu1"
  val serializer="org.apache.kafka.common.serialization.StringSerializer"
  val topic="test0"

  def main(args:Array[String]):Unit={
    val spark: SparkSession = SparkSession
      .builder().master("local[*]")
      .appName("KafkaDirect")
      .getOrCreate()

    val map = Map(
      "metadata.broker.list"->"192.168.17.128:9092,192.168.17.129:9092,192.168.17.130:9092",
      "bootstrap.servers"-> "192.168.17.128:9092",
      "group.id"-> "cu1",
      "key.serializer"-> "org.apache.kafka.common.serialization.StringSerializer",
      "key.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer"
    )
    val topic = Set("test0")
    val ssc=new StreamingContext(spark.sparkContext,Seconds(5))
    val data=KafkaUtils.createDirectStream[String,String](ssc,LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](topic,map))


    val schema = StructType(List(
      StructField("imei", StringType),
      StructField("uid", StringType),
      StructField("cookie", StringType),
      StructField("ip", StringType),
      StructField("local_list", StringType),
      StructField("date", StringType),
      StructField("timestamp", StringType),
      StructField("session_id", StringType),
      StructField("inner_id", StringType),
      StructField("referrer",StringType),
      StructField("params", StringType)
    )
    )
    val startTime: Long = System.currentTimeMillis
    val reverseUDF=functions.udf(reverseTimestamp _)
    data.map(record=>handlerMessage2Row(record.value())).foreachRDD(rdd=>{
      import org.apache.spark.sql.functions._
      val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
      val df=spark.createDataFrame(rdd,schema)
      val hbaseDf=df.withColumn("rowkey",concat_ws("",reverse(concat_ws("",col("session_id"), col("inner_id"))),
        reverseUDF(col("timestamp")))
        .cast(StringType))
      hbaseDf.show()

      hbaseDf.foreachPartition(iterator=>{
        val hbaseConnection=HbaseTools.getConnection
        val table = hbaseConnection.getTable(TableName.valueOf("webLog"))
        val list:util.ArrayList[Put]=new util.ArrayList[Put]
        iterator.foreach(record=>{
          val put=new Put(record.getAs[String]("rowkey").getBytes)
          put.addColumn("main".getBytes(), "imei".getBytes(), record.getAs[String]("imei").getBytes())
          put.addColumn("main".getBytes(), "uid".getBytes(), record.getAs[String]("uid").getBytes())
          put.addColumn("main".getBytes(), "cookie".getBytes(), record.getAs[String]("cookie").getBytes())
          put.addColumn("main".getBytes(), "ip".getBytes(), record.getAs[String]("ip").getBytes())
          put.addColumn("main".getBytes(), "local_list".getBytes(), record.getAs[String]("local_list").getBytes())
          put.addColumn("main".getBytes(), "date".getBytes(), record.getAs[String]("date").getBytes())
          put.addColumn("main".getBytes(), "timestamp".getBytes(), record.getAs[String]("timestamp").getBytes())
          put.addColumn("main".getBytes(), "session_id".getBytes(), record.getAs[String]("session_id").getBytes())
          put.addColumn("main".getBytes(), "inner_id".getBytes(), record.getAs[String]("inner_id").getBytes())
          put.addColumn("main".getBytes(), "referrer".getBytes(), record.getAs[String]("referrer").getBytes())
          put.addColumn("main".getBytes(), "params".getBytes(), record.getAs[String]("params").getBytes())
          list.add(put)
        })
        table.put(list)
      })
    })
    val endTime: Long = System.currentTimeMillis
    ssc.start()
    ssc.awaitTermination()
    System.out.println("start: "+startTime+"end: "+endTime+"程序运行时间： " + (endTime - startTime) + "ms")
  }

  def handlerMessage2Row(jsonStr: String): Row = {
    import scala.collection.JavaConverters._
    val array = JSON.parseObject(jsonStr, classOf[JLinkedHashMap[String, Object]]).asScala.values.map(x => String.valueOf(x)).toArray
    Row(array: _*)
  }

  def reverseTimestamp(timestamp:Long): Long={
    Long.MaxValue-timestamp
  }
}
