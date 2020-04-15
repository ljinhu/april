package com.jimbolix.april.rabbitmq.peoperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit配置类
 */
@Data
public class AprilRabbitMqConfigProperties {

    private String address;

    private String userName;

    private String password;
}
