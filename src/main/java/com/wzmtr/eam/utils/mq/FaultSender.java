package com.wzmtr.eam.utils.mq;

import com.wzmtr.eam.config.RabbitMqConfig;
import com.wzmtr.eam.dto.req.fault.FaultReportOpenReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 故障生产
 * @author  Ray
 * @version 1.0
 * @date 2024/02/19
 */
@Slf4j
@Component
public class FaultSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 故障消息推送
     * @param fault 故障信息
     */
    public void sendFault(FaultReportOpenReqDTO fault) {
        if (Objects.isNull(fault)) {
            return;
        }
        rabbitTemplate.convertAndSend(RabbitMqConfig.FAULT_QUEUE, fault);
    }

}
