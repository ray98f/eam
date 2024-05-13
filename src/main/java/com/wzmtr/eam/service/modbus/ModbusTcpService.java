package com.wzmtr.eam.service.modbus;

import com.wzmtr.eam.dto.req.modbus.SlaveReqDTO;
import com.wzmtr.eam.dto.req.modbus.WriteSlaveReqDTO;

import java.util.concurrent.ExecutionException;

/**
 * modbus协议接口
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
public interface ModbusTcpService {

    /**
     * 连接设备
     */
    void connectSlave();

    /**
     * 读取保持寄存器
     * @param slaveReqDTO 保存寄存器读取参数
     * @return 设备状态
     * @throws ExecutionException 异常
     * @throws InterruptedException 异常
     */
    int[] readHoldingRegisters(SlaveReqDTO slaveReqDTO) throws ExecutionException, InterruptedException;

    /**
     * 写单个寄存器
     * @param writeSlaveReqDTO 写入数据参数
     */
    void writeSingleRegister(WriteSlaveReqDTO writeSlaveReqDTO);

    /**
     * 写多个寄存器
     * @param writeSlaveReqDTO 写入数据参数
     */
    void writeMultipleRegisters(WriteSlaveReqDTO writeSlaveReqDTO);
}
