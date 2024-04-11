package com.wzmtr.eam.utils;

import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.*;
import com.digitalpetri.modbus.responses.*;
import com.wzmtr.eam.constant.ModbusClientConstants;
import com.wzmtr.eam.dto.req.modbus.ModbusNetworkAddressReqDTO;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.BitSet;
import java.util.concurrent.CompletableFuture;

/**
 * modbus协议方法类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/11
 */
@Component
@Slf4j
public class ModbusMasterUtils {

    private ModbusTcpMaster modbusMaster = null;

    public ModbusTcpMaster getModbusTcpMaster() {
        return modbusMaster;
    }
 
    /**
     * 将两个int数拼接成为一个浮点数
     * @param highValue 高16位数值
     * @param lowValue  低16位数值
     * @return 返回拼接的浮点数
     */
    public static float concatenateFloat(int highValue, int lowValue) {
        int combinedValue = ((highValue << 16) | (lowValue & 0xFFFF));
        return Float.intBitsToFloat(combinedValue);
    }

    /**
     * 将浮点数转换为数值数组
     * @param floatValue 浮点数
     * @return 数值数组
     */
    public static int[] floatToIntArray(float floatValue) {
        int combinedIntValue = Float.floatToIntBits(floatValue);
        int[] resultArray = new int[2];
        resultArray[0] = (combinedIntValue >> 16) & 0xFFFF;
        resultArray[1] = combinedIntValue & 0xFFFF;
        return resultArray;
    }
 
    /**
     * 将传入的boolean[]类型数组按位转换成byte[]类型数组
     * @param booleans 传入的boolean数组
     * @return 返回转化后的 byte[]
     */
    public static byte[] booleanToByte(boolean[] booleans) {
        BitSet bitSet = new BitSet(booleans.length);
        for (int i = 0; i < booleans.length; i++) {
            bitSet.set(i, booleans[i]);
        }
        return bitSet.toByteArray();
    }
 
    /**
     * 将传入的int[]类型数组转换成为byte[]类型数组
     * @param values 传入的int[]数组
     * @return 返回 byte[]类型的数组
     */
    public static byte[] intToByte(int[] values) {
        byte[] bytes = new byte[values.length * 2];
        for (int i = 0; i < bytes.length; i += 2) {
            bytes[i] = (byte) (values[i / 2] >> 8 & 0xFF);
            bytes[i + 1] = (byte) (values[i / 2] & 0xFF);
        }
        return bytes;
    }
 
    /**
     * 根据传入的ip地址，创建modbus连接器（默认端口）
     * @param ipAddress ip地址
     * @return 创建连接器，并进行连接，之后返回此连接器
     */
    public CompletableFuture<ModbusTcpMaster> createModbusConnector(String ipAddress) {
        return createModbusConnector(ipAddress, ModbusClientConstants.TCP_PORT);
    }
 
    /**
     * 根据传入的ip地址，创建modbus连接器
     * @param ipAddress ip地址
     * @param port 端口号
     * @return 创建连接器，并进行连接，之后返回此连接器
     */
    public CompletableFuture<ModbusTcpMaster> createModbusConnector(String ipAddress, int port) {
        return createModbusConnector(new ModbusNetworkAddressReqDTO(ipAddress, port));
    }
 
    /**
     * 创建modbus连接器
     * @param modbusNetworkAddress 连接参数
     * @return 创建连接器，并进行连接，之后返回此连接器
     */
    public CompletableFuture<ModbusTcpMaster> createModbusConnector(ModbusNetworkAddressReqDTO modbusNetworkAddress) {
        String ipAddress = modbusNetworkAddress.getIpAddress();
        int port = modbusNetworkAddress.getPort();
        if (modbusMaster == null) {
            ModbusTcpMasterConfig masterConfig = new ModbusTcpMasterConfig.Builder(ipAddress).setPort(port)
                    .setTimeout(Duration.parse(ModbusClientConstants.TIMEOUT_DURATION)).setPersistent(true).setLazy(false).build();
            modbusMaster = new ModbusTcpMaster(masterConfig);
        }
        return modbusMaster.connect();
    }

    public void setBooleanArray(short unsignedShortValue, int[] array, int index, int size) {
        for (int i = index; i < index + size; i++) {
            array[i] = (unsignedShortValue & (0x01 << (i - index))) != 0 ? 1 : 0;
        }
    }
 
    /**
     * 异步方法，读取modbus设备的线圈值，对应功能号01
     * @param slaveId  设备id
     * @param address  要读取的寄存器地址
     * @param quantity 要读取的寄存器数量
     * @return 点位信息
     */
    public CompletableFuture<int[]> readCoils(int slaveId, int address, int quantity) {
        CompletableFuture<ReadCoilsResponse> futureResponse = modbusMaster.sendRequest(
                new ReadCoilsRequest(address, quantity), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return null;
            } else {
                ByteBuf byteBuf = response.getCoilStatus();
                int[] values = new int[quantity];
                int minimum = Math.min(quantity, byteBuf.capacity() * 8);
                for (int i = 0; i < minimum; i += 8) {
                    setBooleanArray(byteBuf.readUnsignedByte(), values, i, Math.min(minimum - i, 8));
                }
                ReferenceCountUtil.release(response);
                return values;
            }
        });
    }
 
    /**
     * 异步方法，读取modbus设备的离散输入值，对应功能号02
     * @param slaveId  设备id
     * @param address  要读取的寄存器地址
     * @param quantity 要读取的寄存器数量
     * @return 点位信息
     */
    public CompletableFuture<int[]> readDiscreteInputs(int slaveId, int address, int quantity) {
        CompletableFuture<ReadDiscreteInputsResponse> futureResponse = modbusMaster.sendRequest(
                new ReadDiscreteInputsRequest(address, quantity), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return null;
            } else {
                ByteBuf byteBuf = response.getInputStatus();
                int[] values = new int[quantity];
                int minimum = Math.min(quantity, byteBuf.capacity() * 8);
                for (int i = 0; i < minimum; i += 8) {
                    setBooleanArray(byteBuf.readUnsignedByte(), values, i, Math.min(minimum - i, 8));
                }
                ReferenceCountUtil.release(response);
                return values;
            }
        });
    }

    /**
     * 异步方法，读取modbus设备的保持寄存器值，对应功能号03
     * @param slaveId  设备id
     * @param address  要读取的寄存器地址
     * @param quantity 要读取的寄存器数量
     * @return 点位信息
     */
    public CompletableFuture<int[]> readHoldingRegisters(int slaveId, int address, int quantity) {
        CompletableFuture<ReadHoldingRegistersResponse> futureResponse = modbusMaster.sendRequest(
                new ReadHoldingRegistersRequest(address, quantity), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return null;
            } else {
                ByteBuf byteBuf = response.getRegisters();
                int[] values = new int[quantity];
                for (int i = 0; i < byteBuf.capacity() / 2; i++) {
                    values[i] = byteBuf.readUnsignedShort();
                }
                ReferenceCountUtil.release(response);
                return values;
            }
        });
    }

    /**
     * 异步方法，读取modbus设备的输入寄存器值，对应功能号04
     * @param slaveId  设备id
     * @param address  要读取的寄存器地址
     * @param quantity 要读取的寄存器数量
     * @return 点位信息
     */
    public CompletableFuture<int[]> readInputRegisters(int slaveId, int address, int quantity) {
        CompletableFuture<ReadInputRegistersResponse> futureResponse = modbusMaster.sendRequest(
                new ReadInputRegistersRequest(address, quantity), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return null;
            } else {
                ByteBuf byteBuf = response.getRegisters();
                int[] values = new int[quantity];
                for (int i = 0; i < byteBuf.capacity() / 2; i++) {
                    values[i] = byteBuf.readUnsignedShort();
                }
                ReferenceCountUtil.release(response);
                return values;
            }
        });
    }
 
    /**
     * 异步方法，写入单个线圈的数值，对应功能号05
     * @param slaveId 设备id
     * @param address 要读取的寄存器地址
     * @param value   要写入的boolean值
     * @return 写入状态
     */
    public CompletableFuture<Boolean> writeSingleCoil(int slaveId, int address, boolean value) {
        CompletableFuture<WriteSingleCoilResponse> futureResponse = modbusMaster.sendRequest(
                new WriteSingleCoilRequest(address, value), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return false;
            } else {
                boolean responseValue = response.getValue() != 0;
                ReferenceCountUtil.release(response);
                return responseValue == value;
            }
        });
    }
 
    /**
     * 异步方法，写入单个寄存器的数值，对应功能号06
     * @param slaveId 设备id
     * @param address 要读取的寄存器地址
     * @param value   要写入的值
     */
    public void writeSingleRegister(int slaveId, int address, int value) {
        CompletableFuture<WriteSingleRegisterResponse> futureResponse = modbusMaster.sendRequest(
                new WriteSingleRegisterRequest(address, value), slaveId);
        futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return false;
            } else {
                int responseValue = response.getValue();
                ReferenceCountUtil.release(response);
                return responseValue == value;
            }
        });
    }
 
    /**
     * 异步方法，写入多个线圈的数值，对应功能号15
     * @param slaveId  设备id
     * @param address  要写入的寄存器地址
     * @param quantity 要写入的寄存器个数
     * @param values   要写入的boolean[]
     * @return 写入状态
     */
    public CompletableFuture<Boolean> writeMultipleCoils(int slaveId, int address, int quantity, boolean[] values) {
        byte[] bytes = booleanToByte(values);
        CompletableFuture<WriteMultipleCoilsResponse> futureResponse = modbusMaster.sendRequest(
                new WriteMultipleCoilsRequest(address, quantity, bytes), slaveId);
        return futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return false;
            } else {
                int responseQuantity = response.getQuantity();
                ReferenceCountUtil.release(response);
                return values.length == responseQuantity;
            }
        });
    }
 
    /**
     * 异步方法，写入多个寄存器的数值，对应功能号16
     * @param slaveId  设备id
     * @param address  要写入的寄存器地址
     * @param quantity 要写入的寄存器个数
     * @param values   要写入的int[]
     */
    public void writeMultipleRegisters(int slaveId, int address, int quantity, int[] values) {
        byte[] bytes = intToByte(values);
        CompletableFuture<WriteMultipleRegistersResponse> futureResponse = modbusMaster.sendRequest(
                new WriteMultipleRegistersRequest(address, quantity, bytes), slaveId);
        futureResponse.handle((response, ex) -> {
            if (ex != null) {
                ReferenceCountUtil.release(response);
                return false;
            } else {
                int responseQuantity = response.getQuantity();
                ReferenceCountUtil.release(response);
                return values.length == responseQuantity;
            }
        });
    }
 
    /**
     * 关闭连接器并释放相关资源
     */
    public void disposeModbusConnector() {
        if (modbusMaster != null) {
            modbusMaster.disconnect();
        }
        Modbus.releaseSharedResources();
    }
 
}
