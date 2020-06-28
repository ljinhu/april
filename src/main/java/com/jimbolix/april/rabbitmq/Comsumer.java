package com.jimbolix.april.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description
 * @ClassName Comsumer
 * @Author liruihui
 * @date 2020.04.19 19:33
 */
@Component
@Slf4j
public class Comsumer {

    @RabbitListener(queues = "spring_topic_queue")
    public void process(Message message, Channel channel) throws IOException {
        // 采用手动应答模式, 手动确认应答更为安全稳定
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        log.info("receive: " + new String(message.getBody()));
    }
}
