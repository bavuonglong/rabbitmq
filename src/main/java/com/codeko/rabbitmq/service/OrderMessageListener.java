package com.codeko.rabbitmq.service;

import com.codeko.rabbitmq.config.RabbitConfig;
import com.codeko.rabbitmq.dto.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OrderMessageListener {

    @RabbitListener(queues = "work.queue")
    public void receiveMessage(Order order, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                               @Header(required = false, name = "x-death") List<Map<String, String>> xDeath) throws Exception {
        if (xDeath != null) {
            log.info("size:  " +xDeath.size());
            if (Long.parseLong(String.valueOf(xDeath.get(0).get("count"))) >= 3) {
                channel.basicAck(tag, false);
                return;
            }
        }
        log.info("Order Received: " + order);
        channel.basicNack(tag, false, false);
    }
}
