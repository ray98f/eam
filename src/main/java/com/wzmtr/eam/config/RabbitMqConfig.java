package com.wzmtr.eam.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * @author  Ray
 * @version 1.0
 * @date 2024/02/19
 */
@Configuration
public class RabbitMqConfig {
    public static final String FAULT_QUEUE = "EAM_FAULT_QUEUE";

    @Bean
    public Queue alarmQueue() {
        return QueueBuilder.durable(FAULT_QUEUE).build();
    }

}
