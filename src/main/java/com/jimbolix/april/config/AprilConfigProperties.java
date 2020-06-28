package com.jimbolix.april.config;

import com.jimbolix.april.rabbitmq.peoperties.AprilRabbitMqConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/15 19:09
 * @Description: april 配置类
 */
@Component
@ConfigurationProperties(prefix = "april")
@Data
public class AprilConfigProperties {

    private AprilRabbitMqConfigProperties rabbit = new AprilRabbitMqConfigProperties();
}