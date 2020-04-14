package com.jimbolix.april.rabbitmq.config;

import com.jimbolix.april.rabbitmq.peoperties.AprilRabbitMqConfigProperties;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AprilRabbitConfig {
    @Autowired
    private AprilRabbitMqConfigProperties configProperties;

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(configProperties.getAddress());
        connectionFactory.setUsername(configProperties.getUserName());
        connectionFactory.setPassword(configProperties.getPassword());
        connectionFactory.setUsername(configProperties.getUserName());
        connectionFactory.setVirtualHost("/");
        return  connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer smListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        smListenerContainer.addQueueNames("");//绑定哪些队列
        smListenerContainer.setMaxConcurrentConsumers(5);//最多多少消费者
        smListenerContainer.setConcurrentConsumers(1);//
        smListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue +"_"+ UUID.randomUUID().toString();
            }
        });

        smListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                //消息监听处理
            }
        });
        return smListenerContainer;
    }
}
