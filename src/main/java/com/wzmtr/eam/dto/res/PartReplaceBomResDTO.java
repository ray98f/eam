package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class PartReplaceBomResDTO {

    @ApiModelProperty(value = "id")
    private String recId;

    @ApiModelProperty(value = "代码编号")
    private String ename;

    @ApiModelProperty(value = "代码名称")
    private String text;

    @ApiModelProperty(value = "父节点id")
    private String parentId;
}
