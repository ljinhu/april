package com.jimbolix.april.rabbitmq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/16 20:32
 * @Description: 
 */
public class CustomMessageDelegate {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMessageDelegate.class);

    public void handleMessage(byte[] message){
        System.out.println("使用handleMessage接受到的消息是"+new String(message));
        LOGGER.info("使用handleMessage接受到的消息是"+new String(message));
    }

    public void customListenerMethod(byte[] message){
        LOGGER.info("使用customListenerMethod接受到的消息是"+new String(message));
    }

    public void customStringListenerMethod(String message){
        LOGGER.info("使用StringListenerMethod接受到的消息是"+message);
    }

    public void topicConsumer(byte[] message){
        LOGGER.info("topicConsumer 消费消息"+ new String(message));
    }
    public void directConsumer(byte[] message){
        LOGGER.info("directConsumer 消费消息"+ new String(message));
    }
}