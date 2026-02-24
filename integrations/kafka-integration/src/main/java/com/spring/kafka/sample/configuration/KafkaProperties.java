package com.spring.kafka.sample.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.props")
@Setter
@Getter
public class KafkaProperties {

  private Common common;
  private Consumer consumer;

  @Getter
  @Setter
  public static class Common {
    private String bootStrapServers;
    private String topic;
  }

  @Getter
  @Setter
  public static class Consumer {
    private String groupId;
  }

}
