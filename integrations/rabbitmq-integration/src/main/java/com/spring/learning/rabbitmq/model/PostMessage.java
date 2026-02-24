package com.spring.learning.rabbitmq.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMessage {

  private String key;
  private String message;

}
