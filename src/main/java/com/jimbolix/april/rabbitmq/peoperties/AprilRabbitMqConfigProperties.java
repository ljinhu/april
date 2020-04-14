package com.jimbolix.april.rabbitmq.peoperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbit配置类
 */
@Component
@ConfigurationProperties(prefix = "april.rabbit.config")
@Data
public class AprilRabbitMqConfigProperties {

    private String address;

    private String userName;

    private String password;
}
