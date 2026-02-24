package com.spring.learning.rabbitmq.service;

import com.spring.learning.rabbitmq.config.ExchangeDetails;
import com.spring.learning.rabbitmq.config.ExchangeDetails.QueueDetails;
import com.spring.learning.rabbitmq.config.RabbitMQConfig;
import com.spring.learning.rabbitmq.model.PostMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublisherService {

  private final RabbitTemplate template;
  private final RabbitMQConfig config;

  public void publishMessage(PostMessage requestMessage) {
    final Message message = createMessage(requestMessage);
    final ExchangeDetails exchangeDetails = config.getExchanges().get(0);
    final List<QueueDetails> queue = exchangeDetails.getQueue();
    template.send(exchangeDetails.getName(), queue.get(1).getRoutingKey(), message);
    log.info("Message published : {}", message);
  }

  private Message createMessage(PostMessage reqMessage) {
    return new Message(reqMessage.getMessage().getBytes(StandardCharsets.UTF_8));
  }

}
