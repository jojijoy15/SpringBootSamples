package com.spring.learning.rabbitmq.controller;


import com.spring.learning.rabbitmq.model.PostMessage;
import com.spring.learning.rabbitmq.model.Status;
import com.spring.learning.rabbitmq.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SimpleController {

  private final PublisherService service;

  @PostMapping("/produce/message")
  public Mono<Status> produceMessage(@RequestBody PostMessage request) {
    service.publishMessage(request);
    Status status = new Status();
    status.setStatus("Success");
    return Mono.just(status);
  }

}
