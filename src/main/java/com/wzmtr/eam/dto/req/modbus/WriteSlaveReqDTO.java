package com.wzmtr.eam.dto.req.modbus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * modbus协议写入参数请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteSlaveReqDTO extends SlaveReqDTO {

    /**
     * 写单个数值
     */
    private Integer value;

    /**
     * 写多个数值
     */
    private int[] values;
}
