package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);


    public static void main(String[] args) {
        //TODO 컨슈머를 종료하고 다시 시작할 경우 이전에 읽었던 offset을 알고 있기 때문에 처음부터 읽지 않고, 저장된 offset부터 읽는다.
        // [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator -
        // [Consumer clientId=consumer-my-java-application-1, groupId=my-java-application]
        // Setting offset for partition demo_java-2 to the committed offset FetchPosition{offset=294, offsetEpoch=Optional[0],
        // currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1 rack: null)], epoch=0}}


        log.info("I am a Kafka Consumer");

        // new group id
        String groupId = "my-java-application-2";
        String topic = "demo_java3";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);

        // none : 컨슈머그룹이 없을 경우 동작하지 않는다. (동작 전에 컨슈머 그룹을 만들어야 함)
        // earliest : 토픽을 처음부터 읽는다.
        // latest : 새 메세지만 읽는다.
        properties.setProperty("auto.offset.reset", "earliest");

        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe to a topic
        consumer.subscribe(Arrays.asList(topic));

        // poll for data
        while (true) {
            log.info("Polling");
            ConsumerRecords<String, String  > records = consumer.poll(Duration.ofMillis(1000)); // 데이터를 받기 위해 기다릴 시간
            for (ConsumerRecord<String, String> record : records) {
                log.info("key : {}, value : {}", record.key(), record.value());
                log.info("Partition : {}", record.partition());
            }

        }

    }
}
