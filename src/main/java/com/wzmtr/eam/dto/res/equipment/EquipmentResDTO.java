package com.wzmtr.eam.dto.res.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "公司编号")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "设备编号")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "专业编号")
    private String majorCode;

    @ApiModelProperty(value = "专业名称")
    private String majorName;

    @ApiModelProperty(value = "系统编号")
    private String systemCode;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "设备类型编号")
    private String equipTypeCode;

    @ApiModelProperty(value = "设备类型名称")
    private String equipTypeName;

    @ApiModelProperty(value = "是否为特殊设备 10 20")
    private String specialEquipFlag;

    @ApiModelProperty(value = "设备来源")
    private String sourceKind;

    @ApiModelProperty(value = "BOM类型")
    private String bomType;

    @ApiModelProperty(value = "生产厂家")
    private String manufacture;

    @ApiModelProperty(value = "合同号")
    private String orderNo;

    @ApiModelProperty(value = "合同名称")
    private String orderName;

    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "出厂日期")
    private String manufactureDate;

    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;

    @ApiModelProperty(value = "开始使用时间")
    private String startUseDate;

    @ApiModelProperty(value = "停止使用时间")
    private String endUseTime;

    @ApiModelProperty(value = "其他特征参数")
    private String otherFeature;

    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "进设备台账时间")
    private String inAccountTime;

    @ApiModelProperty(value = "领用人工号")
    private String pickNo;

    @ApiModelProperty(value = "领用人姓名")
    private String pickName;

    @ApiModelProperty(value = "设备状态")
    private String equipStatus;

    @ApiModelProperty(value = "来源单号")
    private String sourceAppNo;

    @ApiModelProperty(value = "来源明细号")
    private String sourceSubNo;

    @ApiModelProperty(value = "来源记录编号")
    private String sourceRecId;

    @ApiModelProperty(value = "安装单位")
    private String installDealer;

    @ApiModelProperty(value = "附件编号")
    private String docId;

    @ApiModelProperty(value = "来源线别码")
    private String originLineNo;

    @ApiModelProperty(value = "来源线别名称")
    private String originLineName;

    @ApiModelProperty(value = "来源线段代码")
    private String originSegNo;

    @ApiModelProperty(value = "来源线段名称")
    private String originSegName;

    @ApiModelProperty(value = "应用线别代码")
    private String useLineNo;

    @ApiModelProperty(value = "应用线别名称")
    private String useLineName;

    @ApiModelProperty(value = "应用线段代码")
    private String useSegNo;

    @ApiModelProperty(value = "应用线段名称")
    private String useSegName;

    @ApiModelProperty(value = "位置一")
    private String position1Code;

    @ApiModelProperty(value = "位置一名称")
    private String position1Name;

    @ApiModelProperty(value = "位置二")
    private String position2Code;

    @ApiModelProperty(value = "位置二名称")
    private String position2Name;

    @ApiModelProperty(value = "位置三")
    private String position3;

    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;

    @ApiModelProperty(value = "行走里程")
    private Long totalMiles;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;

    @ApiModelProperty(value = "工作流实例id")
    private String workFlowInstId;

    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;

    @ApiModelProperty(value = "特种设备检测日期")
    private String verifyDate;

    @ApiModelProperty(value = "特种设备检测有效期")
    private String verifyValidityDate;

    @ApiModelProperty(value = "档案标记")
    private String archiveFlag;

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

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改人")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "记录状态")
    private String recStatus;
}
