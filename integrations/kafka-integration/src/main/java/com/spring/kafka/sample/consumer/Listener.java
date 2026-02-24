package com.spring.kafka.sample.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class Listener {

  private static final Logger logger = LoggerFactory.getLogger(Listener.class);

  /*@KafkaListener(topics = "#{@kafkaProperties.common.topic}",
      groupId = "#{@kafkaProperties.consumer.groupId}")
  public void listener(@Payload String data) {
    logger.info("Data received from consumer : {}", data);
  }*/

  /*@KafkaListener(topics = "#{@kafkaProperties.common.topic}",
      groupId = "#{@kafkaProperties.consumer.groupId}")
  public void listenerConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
    logger.info("Data received from consumer : {}", consumerRecord);
  }*/

  @KafkaListener(topics = "#{@kafkaProperties.common.topic}",
      groupId = "#{@kafkaProperties.consumer.groupId}")
  @RetryableTopic(
      autoCreateTopics = "false", dltStrategy = DltStrategy.FAIL_ON_ERROR, attempts = "4"
  )
  //Pre create necessary topics in kafka, disable auto create at admin level
  public void listenerConsumerRecordFailure(ConsumerRecord<String, String> consumerRecord) {
    logger.info("Data received from consumer : {}", consumerRecord);
    throw new RuntimeException("Failure Simulation");
  }

  @DltHandler
  public void handleDlt(
      ConsumerRecord<String, String> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    logger.info("Event on dlt topic: {}, payload: {}", topic, consumerRecord);
  }

}
