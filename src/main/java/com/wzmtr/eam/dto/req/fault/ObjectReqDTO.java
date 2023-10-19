package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 15:22
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ObjectReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "专业")
    private String majorCode;
    @ApiModelProperty(value = "系统")
    private String systemCode;
    @ApiModelProperty(value = "设备类别编号")
    private String equipTypeCode;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "设备编码")
    private String equipCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "位置1")
    private String position1Code;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "生产厂家")
    private String manufacture;
    @ApiModelProperty(value = "出厂日期开始")
    private String manufactureDateStart;
    @ApiModelProperty(value = "出厂日期结束")
    private String manufactureDateEnd;
    @ApiModelProperty(value = "线路")
    private String useLineNo;
    @ApiModelProperty(value = "线段")
    private String useSegNo;
    @ApiModelProperty(value = "car")
    private String car;
    @ApiModelProperty(value = "carNode")
    private String carNode;
    @ApiModelProperty(value = "nodeCode")
    private String nodeCode;
}
