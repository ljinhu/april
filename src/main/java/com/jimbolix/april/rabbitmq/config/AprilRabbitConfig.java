package com.jimbolix.april.rabbitmq.config;

import com.jimbolix.april.config.AprilConfigProperties;
import com.jimbolix.april.rabbitmq.common.AprilMessageDelegate;
import com.jimbolix.april.rabbitmq.common.CustomMessageDelegate;
import com.jimbolix.april.rabbitmq.domain.Role;
import com.jimbolix.april.rabbitmq.domain.User;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class AprilRabbitConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(AprilRabbitConfig.class);
    @Autowired
    private AprilConfigProperties configProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        LOGGER.info("配置Rabbit连接工厂");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(configProperties.getRabbit().getAddress());
        connectionFactory.setUsername(configProperties.getRabbit().getUserName());
        connectionFactory.setPassword(configProperties.getRabbit().getPassword());
        connectionFactory.setUsername(configProperties.getRabbit().getUserName());
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue("topic_queue");
        return queue;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer smListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        smListenerContainer.addQueueNames("spring_topic_queue", "spring_direct_queue");//绑定哪些队列
        smListenerContainer.addQueueNames("spring_topic_queue");//绑定哪些队列
        smListenerContainer.setMaxConcurrentConsumers(5);//最多多少消费者
        smListenerContainer.setConcurrentConsumers(1);//
        smListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        smListenerContainer.addQueueNames("spring_topic_queue", "spring_direct_queue");//绑定哪些队列
        smListenerContainer.setMaxConcurrentConsumers(5);//最多多少消费者
        smListenerContainer.setConcurrentConsumers(1);//
        smListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
//
        smListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                //消息监听处理
                LOGGER.info("收到的消息是"+new String(message.getBody()));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            }
        });
//        smListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                //消息监听处理
//                LOGGER.info("收到的消息是"+new String(message.getBody()));
//            }
//        });
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new CustomMessageDelegate());
//        messageListenerAdapter.setDefaultListenerMethod("customStringListenerMethod");
//        messageListenerAdapter.setMessageConverter(new AprilRabbitMessageConvert());
//        Map<String,String> queueOrTagToMethodNameMap = new HashMap<>(2);
//        queueOrTagToMethodNameMap.put("spring_topic_queue","topicConsumer");
//        queueOrTagToMethodNameMap.put("spring_direct_queue","directConsumer");
//        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagToMethodNameMap);
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new AprilMessageDelegate());
//        messageListenerAdapter.setDefaultListenerMethod("consume");
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        Map<String,Class<?>> idClassMapping = new HashMap<>(2);
//        idClassMapping.put("user", User.class);
//        idClassMapping.put("role", Role.class);
//        defaultJackson2JavaTypeMapper.setIdClassMapping(idClassMapping);
//        converter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
//        messageListenerAdapter.setMessageConverter(converter);
//        smListenerContainer.setMessageListener(messageListenerAdapter);
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new AprilMessageDelegate());
        messageListenerAdapter.setDefaultListenerMethod("consume");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String,Class<?>> idClassMapping = new HashMap<>(2);
        idClassMapping.put("user", User.class);
        idClassMapping.put("role", Role.class);
        defaultJackson2JavaTypeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
        messageListenerAdapter.setMessageConverter(converter);
        smListenerContainer.setMessageListener(messageListenerAdapter);
        return smListenerContainer;
    }

    //    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(4);
        container.addQueueNames("spring_topic_queue", "spring_direct_queue");
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new CustomMessageDelegate());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }
}
