package com.codeko.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

    /*public static final String QUEUE_ORDERS = "orders-queue";
    public static final String QUEUE_DEAD_ORDERS = "orders-dead-queue";
    public static final String EXCHANGE_ORDERS = "orders-exchange";
    public static final String EXCHANGE_DEAD_ORDERS = "orders-dead-exchange";

    @Bean
    Queue ordersQueue() {
        return QueueBuilder.durable(QUEUE_ORDERS)
                .withArgument("x-dead-letter-exchange", orderDlExchange().getName())
//                .withArgument("x-dead-letter-routing-key", QUEUE_DEAD_ORDERS)
//                .withArgument("x-message-ttl", 5000) //if message is not consumed in 15 seconds send to DLQ
                .build();
    }

    @Bean
    Exchange ordersExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_ORDERS).build();
    }

    @Bean
    Binding binding(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(QUEUE_ORDERS);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder
                .durable(QUEUE_DEAD_ORDERS)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", orderDlExchange().getName())
                .build();
    }

    @Bean
    Exchange orderDlExchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_DEAD_ORDERS)
                .build();
    }

    @Bean
    Binding worksDlBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(orderDlExchange()).with(QUEUE_DEAD_ORDERS)
                .noargs();
    }*/

    @Bean
    Exchange worksExchange() {
        return ExchangeBuilder.topicExchange("work.exchange")
                .build();
    }
    @Bean
    public Queue worksQueue() {
        return QueueBuilder.durable("work.queue")
                .withArgument("x-dead-letter-exchange", worksDlExchange().getName())
                .build();
    }
    @Bean
    Binding worksBinding() {
        return BindingBuilder
                .bind(worksQueue())
                .to(worksExchange()).with("#").noargs();
    }
    // Dead letter exchange for holding rejected work units..
    @Bean
    Exchange worksDlExchange() {
        return ExchangeBuilder
                .topicExchange("work.exchange.dl")
                .build();
    }
    //Queue to hold Deadletter messages from worksQueue
    @Bean
    public Queue worksDLQueue() {
        return QueueBuilder
                .durable("works.queue.dl")
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", worksExchange().getName())
                .build();
    }
    @Bean
    Binding worksDlBinding() {
        return BindingBuilder
                .bind(worksDLQueue())
                .to(worksDlExchange()).with("#")
                .noargs();
    }

    /**
     * By setting message converter with Jackson2JsonMessageConverter instance, we can send a message as JSON payload
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * By setting message handler method factory with custom factory, we can receive message as JSON
     * @param registrar
     */
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

}
