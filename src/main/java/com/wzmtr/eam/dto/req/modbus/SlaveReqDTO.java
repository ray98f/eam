package com.wzmtr.eam.dto.req.modbus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * modbus协议读取参数请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SlaveReqDTO {

    /**
     * 设备id
     */
    private Integer slaveId;

    /**
     * 要读取的寄存器地址
     */
    private Integer address;

    /**
     * 要读取的寄存器数量
     */
    private Integer quantity;
}
