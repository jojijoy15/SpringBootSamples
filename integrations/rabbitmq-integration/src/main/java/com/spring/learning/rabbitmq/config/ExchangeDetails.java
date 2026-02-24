package com.spring.learning.rabbitmq.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeDetails {

  private String name;
  private List<QueueDetails> queue;

  @Setter
  @Getter
  public static class QueueDetails {

    private String routingKey;
    private String name;
  }
}
