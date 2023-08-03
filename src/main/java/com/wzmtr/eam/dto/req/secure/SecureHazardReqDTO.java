package com.wzmtr.eam.dto.req.secure;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 20:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SecureHazardReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "隐患排查单号")
    private String riskId ;
    @ApiModelProperty(value = "发现日期开始")
    private String inspectDateBegin ;
    @ApiModelProperty(value = "发现日期结束")
    private String inspectDateEnd ;
    @ApiModelProperty(value = "隐患等级")
    private String riskRank ;
    @ApiModelProperty(value = "整改情况")
    private String restoreDesc ;
    @ApiModelProperty(value = "记录状态")
    private String recStatus ;
}
