package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class TransferExportReqDTO {
    /**
     * 移交单号
     */
    private String transferNo;
    /**
     * 合同清单明细号
     */
    private String itemCode;
    /**
     * 设备名称
     */
    private String itemName;
    /**
     * 位置一
     */
    private String position1Code;
    /**
     * 处理状态
     */
    private String eamProcessStatus;
    /**
     * 专业编号
     */
    private String majorCode;
    /**
     * 合同编号
     */
    private String orderNo;
    /**
     * 合同名称
     */
    private String orderName;
    /**
     * ids
     */
    private List<String> ids;
}
