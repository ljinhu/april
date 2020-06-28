package com.jimbolix.april.rabbitmq.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimbolix.april.rabbitmq.domain.Role;
import com.jimbolix.april.rabbitmq.domain.User;

/**
 * @Author: ruihui.li
 * @Date: 2020/4/16 22:01
 * @Description:
 */
public class AprilMessageDelegate {

    public void consume(User user) {
        System.err.println("消费的消息是user:【name=" + user.getName() + "address = " + user.getAddress());

    }

    public void consume(Role role) {
        System.err.println("消费的消息是user:【roleName=" + role.getRoleName() + "id = " + role.getId());
    }
}