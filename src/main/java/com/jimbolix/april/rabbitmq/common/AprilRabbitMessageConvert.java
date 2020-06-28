package com.jimbolix.april.rabbitmq.common;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Optional;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/16 21:01
 * @Description: 自定义的消息转换器
 */
public class AprilRabbitMessageConvert implements MessageConverter {

    /**
     * 把Java对象和属性对象转换成message
     * @param object
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(),messageProperties);
    }

    /**
     * 把message对象转换成Java对象
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType = message.getMessageProperties().getContentType();
        contentType = Optional.ofNullable(contentType).orElse("1");
        if(contentType.contains("text")){
            return new String(message.getBody());
        }
        return message.getBody();
    }
}