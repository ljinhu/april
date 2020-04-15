package com.jimbolix.april.rabbitmq.config;

import com.jimbolix.april.config.AprilConfigProperties;
import com.jimbolix.april.rabbitmq.peoperties.AprilRabbitMqConfigProperties;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(AprilRabbitConfig.class);
    @Autowired
    private AprilConfigProperties configProperties;

    @Bean
    public ConnectionFactory connectionFactory(){
        LOGGER.info("配置Rabbit连接工厂");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(configProperties.getRabbit().getAddress());
        connectionFactory.setUsername(configProperties.getRabbit().getUserName());
        connectionFactory.setPassword(configProperties.getRabbit().getPassword());
        connectionFactory.setUsername(configProperties.getRabbit().getUserName());
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

//    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer smListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        smListenerContainer.addQueueNames("topic_queue");//绑定哪些队列
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
                LOGGER.info("收到的消息是"+message.getBody().toString());
            }
        });
        return smListenerContainer;
    }
}
