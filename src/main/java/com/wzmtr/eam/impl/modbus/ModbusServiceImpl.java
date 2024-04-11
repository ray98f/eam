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

import java.util.Arrays;
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
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus error: Connect slave fail, {}", e.getMessage());
        }
    }

    private ModbusTcpMaster checkConnectSlave() {
        return modbusMasterUtils.getModbusTcpMaster();
    }

    @Override
    public int[] readHoldingRegisters(SlaveReqDTO slaveReqDTO) throws ExecutionException, InterruptedException {
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus error: Not connect slave");
        }
        CompletableFuture<int[]> registerFuture = modbusMasterUtils.readHoldingRegisters(slaveReqDTO.getSlaveId(), slaveReqDTO.getAddress(), slaveReqDTO.getQuantity());
        int[] registerValues = registerFuture.get();
        log.info("ReadHoldingRegisters info = {}", Arrays.toString(registerValues));
        return registerValues;
    }

    @Override
    public void writeSingleRegister(WriteSlaveReqDTO writeSlaveReqDTO) {
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus error: Not connect slave");
        }
        modbusMasterUtils.writeSingleRegister(writeSlaveReqDTO.getSlaveId(), writeSlaveReqDTO.getAddress(), writeSlaveReqDTO.getValue());
    }

    @Override
    public void writeMultipleRegisters(WriteSlaveReqDTO writeSlaveReqDTO) {
        if (checkConnectSlave() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus error: Not connect slave");
        }
        if (writeSlaveReqDTO.getValues().length != writeSlaveReqDTO.getQuantity()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "Modbus error: Quantity error");
        }
        modbusMasterUtils.writeMultipleRegisters(writeSlaveReqDTO.getSlaveId(), writeSlaveReqDTO.getAddress(), writeSlaveReqDTO.getQuantity(), writeSlaveReqDTO.getValues());
    }

}
