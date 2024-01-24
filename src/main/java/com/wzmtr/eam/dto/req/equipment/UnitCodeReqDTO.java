package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class UnitCodeReqDTO {

    private String recId = "";
    private String unitNo = "";
    private String batchNo = "";
    private String assetNo = "";
    private String devNo = "";
    private Integer batchStatus = 0;
    private Integer assetStatus = 0;
    private Integer devStatus = 0;
    private Integer unitnoStatus = 0;
    private Integer status = 1;
    private String repUnitNo = "";
    private String remark = "";
    private String ext1 = "";
    private String ext2 = "";
    private String ext3 = "";
    private String ext4 = "";
    private String ext5 = "";
    private String recCreator = "";
    private String recCreateTime = "";
    private String recRevisor = "";
    private String recReviseTime = "";
    private String recDeletor = "";
    private String recDeleteTime = "";
    private String deleteFlag = "";
    private String archiveFlag = "";
    private Integer comStatus = 0;
    private String proCode = "";
    private String proName = "";
    private String orderNo = "";
    private String orderName = "";
    private String supplierId = "";
    private String supplierName = "";
    private String matSpecifi = "";
    private String brand = "";
}
