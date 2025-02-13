package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class TrainMileageReqDTO {

    private String recId;

    private String equipCode;

    private String equipName;

    private BigDecimal totalMiles;

    private BigDecimal milesIncrement;

    private String fillinTime;

    private String fillinUserId;

    private String remark;

    private String recCreator;

    private String recCreateTime;

    private String recRevisor;

    private String recReviseTime;

    private String recDeletor;

    private String recDeleteTime;

    private String deleteFlag;

    private String archiveFlag;

    private String recStatus;

    private String ext1;

    private String ext2;

    private String ext3;

    private String ext4;

    private String ext5;

    private BigDecimal totalTractionEnergy;

    private BigDecimal totalAuxiliaryEnergy;

    private BigDecimal totalRegenratedElectricity;

    private BigDecimal tractionIncrement;

    private BigDecimal auxiliaryIncrement;

    private BigDecimal regenratedIncrement;
}
