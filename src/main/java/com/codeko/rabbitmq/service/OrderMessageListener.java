package com.codeko.rabbitmq.service;

import com.codeko.rabbitmq.config.RabbitConfig;
import com.codeko.rabbitmq.dto.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageListener {

    @RabbitListener(queues = RabbitConfig.QUEUE_ORDERS)
    public void processOrder(Order order) {
        log.info("Order Received: " + order);
    }
}
