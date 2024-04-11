package com.wzmtr.eam.impl.modbus;

import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.wzmtr.eam.constant.ModbusClientConstants;
import com.wzmtr.eam.dto.req.modbus.SlaveReqDTO;
import com.wzmtr.eam.dto.req.modbus.WriteSlaveReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.modbus.ModbusTcpService;
import com.wzmtr.eam.utils.ModbusMasterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * modbus协议接口
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@Service
@Slf4j
public class ModbusServiceImpl implements ModbusTcpService {
    @Autowired
    private ModbusMasterUtils modbusMasterUtils;

    @Override
    public void connectSlave() {
        try {
            modbusMasterUtils.createModbusConnector(ModbusClientConstants.IP, ModbusClientConstants.TCP_PORT);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：连接失败, {}", e.getMessage());
        }
    }

    private ModbusTcpMaster checkConnectSlave() {
        return modbusMasterUtils.getModbusTcpMaster();
    }

    @Override
    public int[] readHoldingRegisters(SlaveReqDTO slaveReqDTO) throws ExecutionException, InterruptedException {
        // 连接Modbus
        try {
            modbusMasterUtils.createModbusConnector(ModbusClientConstants.IP, ModbusClientConstants.TCP_PORT);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：连接失败, {}", e.getMessage());
        }
        // 读取保持寄存器数据
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：未连接！");
        }
        CompletableFuture<int[]> registerFuture = modbusMasterUtils.readHoldingRegisters(slaveReqDTO.getSlaveId(),
                slaveReqDTO.getAddress(), slaveReqDTO.getQuantity());
        int[] registerValues = registerFuture.get();
        // 关闭连接器并释放相关资源
        modbusMasterUtils.disposeModbusConnector();
        return registerValues;
    }

    @Override
    public void writeSingleRegister(WriteSlaveReqDTO writeSlaveReqDTO) {
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：未连接！");
        }
        modbusMasterUtils.writeSingleRegister(writeSlaveReqDTO.getSlaveId(),
                writeSlaveReqDTO.getAddress(), writeSlaveReqDTO.getValue());
    }

    @Override
    public void writeMultipleRegisters(WriteSlaveReqDTO writeSlaveReqDTO) {
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：未连接！");
        }
        if (writeSlaveReqDTO.getValues().length != writeSlaveReqDTO.getQuantity()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus异常：寄存器数量错误！");
        }
        modbusMasterUtils.writeMultipleRegisters(writeSlaveReqDTO.getSlaveId(), writeSlaveReqDTO.getAddress(),
                writeSlaveReqDTO.getQuantity(), writeSlaveReqDTO.getValues());
    }

}
