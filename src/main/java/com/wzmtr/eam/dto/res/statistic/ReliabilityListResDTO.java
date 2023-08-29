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
    @ApiModelProperty(value = "进出站闸机可靠度")
    List<ReliabilityResDTO> queryGateBrakeFault;
    @ApiModelProperty(value = "自动扶梯可靠度")
    List<ReliabilityResDTO> queryEscalatorFault;
    @ApiModelProperty(value = "垂直扶梯可靠度")
    List<ReliabilityResDTO> queryVerticalEscalatorFault;
    @ApiModelProperty(value = "列车乘客信息系统可靠度")
    List<ReliabilityResDTO> queryTrainPassengerInformationFault;
    @ApiModelProperty(value = "车站乘客信息系统可靠度")
    List<ReliabilityResDTO> queryStationPassengerInformationFault;
    @ApiModelProperty(value = "消防设备可靠度")
    List<ReliabilityResDTO> queryFireFightingEquipmentFault;
}