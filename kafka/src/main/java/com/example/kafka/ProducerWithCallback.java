package com.example.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerWithCallback.class);


    public static void main(String[] args) throws InterruptedException {
        log.info("Producer Callback");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("batch.size", "400");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        for (int j = 0; j < 10; j++) {

            for (int i = 0; i < 30; i++) {

                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world" + i);

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            log.info("Received new metadata  Topic : {}", metadata.topic());
                            log.info("Received new metadata  Partition : {}", metadata.partition());
                            log.info("Received new metadata  Offset : {}", metadata.offset());
                            log.info("Received new metadata  Timestamp : {}", metadata.timestamp());
                        } else {
                            log.error("Error while producing", exception);
                        }
                    }
                });

            }

            Thread.sleep(500);

        }

        producer.flush();

        producer.close();

    }
}