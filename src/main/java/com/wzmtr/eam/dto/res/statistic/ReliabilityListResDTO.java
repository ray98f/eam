package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class ReliabilityListResDTO {
    @ApiModelProperty(value = "售票机可靠度")
    List<ReliabilityResDTO> queryTicketFault;
    List<String> queryTicketFaultMonth;
    List<String> queryTicketFaultData;
    @ApiModelProperty(value = "进出站闸机可靠度")
    List<ReliabilityResDTO> queryGateBrakeFault;
    List<String> queryGateBrakeMonth;
    List<String> queryGateBrakeData;
    @ApiModelProperty(value = "自动扶梯可靠度")
    List<ReliabilityResDTO> queryEscalatorFault;
    List<String> queryEscalatorMonth;
    List<String> queryEscalatorData;
    @ApiModelProperty(value = "垂直扶梯可靠度")
    List<ReliabilityResDTO> queryVerticalEscalatorFault;
    List<String> queryVerticalEscalatorMonth;
    List<String> queryVerticalEscalatorData;
    @ApiModelProperty(value = "列车乘客信息系统可靠度")
    List<ReliabilityResDTO> queryTrainPassengerInformationFault;
    List<String> queryTrainPassengerInformationMonth;
    List<String> queryTrainPassengerInformationData;
    @ApiModelProperty(value = "车站乘客信息系统可靠度")
    List<ReliabilityResDTO> queryStationPassengerInformationFault;
    List<String> queryStationPassengerInformationMonth;
    List<String> queryStationPassengerInformationData;
    @ApiModelProperty(value = "消防设备可靠度")
    List<ReliabilityResDTO> queryFireFightingEquipmentFault;
    List<String> queryFireFightingEquipmentFaultMonth;
    List<String> queryFireFightingEquipmentFaultData;
}