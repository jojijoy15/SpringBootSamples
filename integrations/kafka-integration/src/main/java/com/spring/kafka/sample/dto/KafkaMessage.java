package com.spring.kafka.sample.dto;

import lombok.Data;

@Data
public class KafkaMessage {

  private String key;
  private String value;

}
