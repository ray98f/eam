package com.wzmtr.eam.dto.req.modbus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * modbus协议连接请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ModbusNetworkAddressReqDTO {

    /**
     * ip
     */
    private String ipAddress;

    /**
     * 端口
     */
    private Integer port;
}
