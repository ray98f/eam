package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障查询开放接口部件更换返回类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/25
 */

@Data
@ApiModel
public class FaultPartReplaceOpenResDTO {
    /**
     * 更换配件代码
     */
    @ApiModelProperty(value = "更换配件代码")
    private String replacePartNo;
    /**
     * 更换配件名称
     */
    @ApiModelProperty(value = "更换配件名称")
    private String replacePartName;
    /**
     * 更换原因
     */
    @ApiModelProperty(value = "更换原因")
    private String replaceReason;
    /**
     * 更换日期
     */
    @ApiModelProperty(value = "更换日期")
    private String replaceDate;
    /**
     * 更换数量
     */
    @ApiModelProperty(value = "更换数量")
    private String replaceNum;
}
