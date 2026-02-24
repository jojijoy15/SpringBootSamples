package com.spring.kafka.sample.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaProducerTemplate;

  public void sendMessage(String topic, String key, String message) {
    kafkaProducerTemplate.send(topic, key, message); // returns Completable Future
  }

}
