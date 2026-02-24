package com.spring.kafka.sample;

import com.spring.kafka.sample.configuration.KafkaProperties;
import com.spring.kafka.sample.dto.KafkaMessage;
import com.spring.kafka.sample.dto.Status;
import com.spring.kafka.sample.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final KafkaProducer producer;
  private final KafkaProperties properties;

  @PostMapping("/message")
  public ResponseEntity<Status> sendMessage(@RequestBody KafkaMessage message) {
    producer.sendMessage(properties.getCommon().getTopic(), message.getKey(), message.getValue());
    final Status success = new Status();
    success.setStatus("Success");
    return ResponseEntity.ok(success);
  }
}
