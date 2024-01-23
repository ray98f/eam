package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class ReliabilityListResDTO {
    @ApiModelProperty(value = "售票机可靠度")
    ReliabilityDetailResDTO queryTicketFault;
    @ApiModelProperty(value = "进出站闸机可靠度")
    ReliabilityDetailResDTO queryGateBrakeFault;
    @ApiModelProperty(value = "自动扶梯可靠度")
    ReliabilityDetailResDTO queryEscalatorFault;
    @ApiModelProperty(value = "垂直扶梯可靠度")
    ReliabilityDetailResDTO queryVerticalEscalatorFault;
    @ApiModelProperty(value = "列车乘客信息系统可靠度")
    ReliabilityDetailResDTO queryTrainPassengerInformationFault;
    @ApiModelProperty(value = "车站乘客信息系统可靠度")
    ReliabilityDetailResDTO queryStationPassengerInformationFault;
    @ApiModelProperty(value = "消防设备可靠度")
    ReliabilityDetailResDTO queryFireFightingEquipmentFault;
}