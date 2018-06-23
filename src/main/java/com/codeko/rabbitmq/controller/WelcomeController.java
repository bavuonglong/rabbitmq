package com.codeko.rabbitmq.controller;

import com.codeko.rabbitmq.dto.Order;
import com.codeko.rabbitmq.service.OrderMessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${greeting}")
    private String greeting;

    private final OrderMessageSender sender;

    public WelcomeController(OrderMessageSender sender) {
        this.sender = sender;
    }

    @GetMapping("/")
    public String index() {
        return "Welcome to Order Application, " + greeting;
    }

    @GetMapping("/order")
    public Order sendOrderToBrokerAndGetItBack() {
        Order originalOrder = Order.builder()
                .orderNumber("order-number-1")
                .productId("product-id-1")
                .amount(123)
                .build();

        sender.sendOrder(originalOrder);

        return null;
    }
}
