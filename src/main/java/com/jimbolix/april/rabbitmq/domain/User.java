package com.jimbolix.april.rabbitmq.domain;

import lombok.Data;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/16 21:58
 * @Description: RabbitMq消息转换测试类
 */
@Data
public class User {

    private Integer id;

    private String name;

    private String address;
}