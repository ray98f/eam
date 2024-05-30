package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulTplDetailReqDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
    @ApiModelProperty(value = "模板编号")
    private String templateId;
    
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    
    @ApiModelProperty(value = "模块顺序")
    private String modelSequence;
    
    @ApiModelProperty(value = "模块名称")
    private String modelName;
    
    @ApiModelProperty(value = "车组号")
    private String trainNumber;
    
    @ApiModelProperty(value = "检修项")
    private String itemName;
    
    @ApiModelProperty(value = "项目类型")
    private String itemType;
    
    @ApiModelProperty(value = "项目值")
    private String inspectItemValue;
    
    @ApiModelProperty(value = "最大参考值")
    private String maxValue;
    
    @ApiModelProperty(value = "最小参考值")
    private String minValue;
    
    @ApiModelProperty(value = "参考值单位")
    private String itemUnit;
    
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    
    @ApiModelProperty(value = "启用状态")
    private String itemState;
    
    @ApiModelProperty(value = "默认标识")
    private String defaultFlag;
    
    @ApiModelProperty(value = "顺序号")
    private String sequenceId;
    
    @ApiModelProperty(value = "修程")
    private String repairType;
    
    @ApiModelProperty(value = "时间周期")
    private String timePeriod;
    
    @ApiModelProperty(value = "里程周期")
    private String mileagePeriod;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "记录状态")
    private String recStatus;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    @ApiModelProperty(value = "扩展字段4")
    private String ext4;

    @ApiModelProperty(value = "扩展字段5")
    private String ext5;

    @ApiModelProperty(value = "ids")
    private List<String> ids;
}
