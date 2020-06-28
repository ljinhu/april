package com.jimbolix.april.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/16 13:56
 * @Description: 
 */
@Configuration
public class QueueExchangeConfig {

    @Bean
    public Queue topicQueue(){
        Queue queue = new Queue("spring_topic_queue",true);
        return queue;
    }
    @Bean
    public Queue directQueue(){
        Queue queue = new Queue("spring_direct_queue",true);
        return queue;
    }

    @Bean
    public TopicExchange topicExchange(){
        TopicExchange topicExchange = new TopicExchange("spring_topic_exchange", true, false);
        return topicExchange;
    }
    @Bean
    public DirectExchange directExchange(){
        DirectExchange exchange = new DirectExchange("spring_direct_ex", true, false);
        return exchange;
    }
    @Bean
    public Binding topicBinding(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("spring.*");
    }

    @Bean
    public Binding directBinding(){
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("spring_direct_key");
    }
}