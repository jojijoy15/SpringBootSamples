package com.spring.learning.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

  @RabbitListener(queues = "#{@rabbitMQConfig.exchanges[0].queue[0].name}")
  public void listener(String message) {
    System.out.println("Received message: " + message);
  }

  @RabbitListener(queues = "#{@rabbitMQConfig.exchanges[0].queue[1].name}")
  public void listenerTwo(
      Message message, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) long tag,
      @Header(AmqpHeaders.RETRY_COUNT) long retry) throws IOException {
    log.info("Received message: {}", message);
    log.info("Tag : {}, Retry count is {}", tag, retry);
//    channel.basicNack(tag,  false, true);
    throw new RuntimeException("Some error");
  }


}
