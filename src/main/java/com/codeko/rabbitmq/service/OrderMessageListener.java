package com.codeko.rabbitmq.service;

import com.codeko.rabbitmq.config.RabbitConfig;
import com.codeko.rabbitmq.dto.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageListener {

    @RabbitListener(queues = RabbitConfig.QUEUE_ORDERS)
    public void receiveMessage(Order order, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        log.info("Order Received: " + order);
//        channel.basicAck(tag, false);
        channel.basicNack(tag, false, true);
    }
}
