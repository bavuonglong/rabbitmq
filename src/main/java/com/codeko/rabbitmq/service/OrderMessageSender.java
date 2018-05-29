package com.codeko.rabbitmq.service;

import com.codeko.rabbitmq.config.RabbitConfig;
import com.codeko.rabbitmq.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OrderMessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrder(Order order) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_ORDERS, order);
    }

    public void sendOrderAsJson(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            Message message = MessageBuilder
                    .withBody(orderJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_ORDERS, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

