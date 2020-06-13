package Spark;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class KafkaTools {
    private String servers="192.168.17.128:9092,192.168.17.129:9092,192.168.17.130:9092";
    private String serializer="org.apache.kafka.common.serialization.StringSerializer";

    public static Producer<String,String> getProducer(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.17.128:9092,192.168.17.129:9092,192.168.17.130:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer;
        producer = new KafkaProducer<String, String>(properties);
        return producer;
    }

    public static Consumer<String,String> getConsumer(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.17.128:9092,192.168.17.129:9092,192.168.17.130:9092");
        properties.put("group.id","cu1");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.offset.reset","earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String,String> consumer;
        consumer=new KafkaConsumer<String, String>(properties);
        return consumer;
    }
}
