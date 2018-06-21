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
