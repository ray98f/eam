package com.wzmtr.eam.dto.res.overhaul;

import com.wzmtr.eam.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulItemResDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;

    @ApiModelProperty(value = "DMER22表主键")
    private String objectId;
    
    @ApiModelProperty(value = "条目")
    private String itemName;
    
    @ApiModelProperty(value = "检修工单")
    private String orderCode;
    
    @ApiModelProperty(value = "模块名称")
    private String modelName;
    
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    
    @ApiModelProperty(value = "用户工号")
    private String workUserId;
    
    @ApiModelProperty(value = "用户姓名")
    private String workUserName;
    
    @ApiModelProperty(value = "类型")
    private String itemType;
    
    @ApiModelProperty(value = "车组号")
    private String trainNumber;
    
    @ApiModelProperty(value = "结果")
    private String workResult;
    
    @ApiModelProperty(value = "条目值")
    private String inspectItemValue;
    
    @ApiModelProperty(value = "时间")
    private String workTime;
    
    @ApiModelProperty(value = "附件")
    private String docId;

    @ApiModelProperty(value = "附件文件列表")
    private List<File> docFile;
    
    @ApiModelProperty(value = "工作说明")
    private String workDetail;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "异常标识")
    private String errorFlag;
    
    @ApiModelProperty(value = "单位")
    private String itemUnit;
    
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    
    @ApiModelProperty(value = "最大参考值")
    private String maxValue;
    
    @ApiModelProperty(value = "最小参考值")
    private String minValue;
    
    @ApiModelProperty(value = "互检人")
    private String mutualInspectionPeople;
    
    @ApiModelProperty(value = "互检人工号")
    private String mutualInspectionPeopleId;
    
    @ApiModelProperty(value = "专检人")
    private String specialInspectionPeople;
    
    @ApiModelProperty(value = "专检人工号")
    private String specialInspectionPeopleId;
    
    @ApiModelProperty(value = "模块顺序")
    private String modelSequence;
    
    @ApiModelProperty(value = "检修项顺序")
    private String sequenceId;
    
    @ApiModelProperty(value = "检修模板项外键")
    private String tdmer02Id;

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
}
