package com.wzmtr.eam.controller.modbus;

import com.wzmtr.eam.dto.req.modbus.SlaveReqDTO;
import com.wzmtr.eam.dto.req.modbus.WriteSlaveReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.modbus.ModbusTcpService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * modbus协议接口
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@RestController
@RequestMapping("/modbus")
public class ModbusController {

    @Autowired
    private ModbusTcpService modbusTcpService;

    /**
     * 连接设备
     * @return 成功
     */
    @GetMapping(value = "/connect")
    public DataResponse<T> connectSlave(){
        modbusTcpService.connectSlave();
        return DataResponse.success();
    }

    /**
     * 读取保持寄存器
     * @param slaveReqDTO 保存寄存器读取参数
     * @return 设备状态
     * @throws ExecutionException 异常
     * @throws InterruptedException 异常
     */
    @PostMapping(value = "/holding/read")
    public DataResponse<int[]> readHoldingRegisters(@RequestBody SlaveReqDTO slaveReqDTO) throws ExecutionException, InterruptedException {
        return DataResponse.of(modbusTcpService.readHoldingRegisters(slaveReqDTO));
    }

    /**
     * 写单个寄存器
     * @param writeSlaveReqDTO 写入数据参数
     * @return 成功
     */
    @PostMapping(value = "/single/write")
    public DataResponse<T> writeSingleRegister(@RequestBody WriteSlaveReqDTO writeSlaveReqDTO) {
        modbusTcpService.writeSingleRegister(writeSlaveReqDTO);
        return DataResponse.success();
    }

    /**
     * 写多个寄存器
     * @param writeSlaveReqDTO 写入数据参数
     * @return 成功
     */
    @PostMapping(value = "/multiple/write")
    public DataResponse<T> writeMultipleRegisters(@RequestBody WriteSlaveReqDTO writeSlaveReqDTO){
        modbusTcpService.writeMultipleRegisters(writeSlaveReqDTO);
        return DataResponse.success();
    }

}

