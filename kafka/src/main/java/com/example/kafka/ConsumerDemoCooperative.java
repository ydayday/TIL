package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCooperative.class);


    public static void main(String[] args) {
        log.info("I am a Kafka Consumer");

        // new group id
        String groupId = "my-java-application";
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
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());


        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                super.run();
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()..");
                consumer.wakeup();

                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            // subscribe to a topic
            consumer.subscribe(Arrays.asList(topic));

            // poll for data
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000)); // 데이터를 받기 위해 기다릴 시간
                for (ConsumerRecord<String, String> record : records) {
                    log.info("key : {}, value : {}", record.key(), record.value());
                    log.info("Partition : {}", record.partition());
                }

            }
        } catch (WakeupException e) {
            log.info("Consumer is starting to shut down");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
            log.info("컨슈머 종료!");
        }


    }
}
