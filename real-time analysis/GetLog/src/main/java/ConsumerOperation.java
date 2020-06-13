import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.spark.streaming.api.java.JavaInputDStream;

import java.time.Duration;
import java.util.Arrays;

public class ConsumerOperation {
    private static final String TOPIC="test0";
    public static void main(String[] args){
        org.apache.kafka.clients.consumer.Consumer consumer= KafkaTools.getConsumer();
        consumer.subscribe(Arrays.asList(TOPIC));
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s, value = %s",record.offset(), record.key(), record.value());
                System.out.println();
            }
        }


    }



}
