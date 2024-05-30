package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备管理-设备台账请求类
 * @author  Ray
 * @version 1.0
 * @date 2023/07/24
 */
@Data
@ApiModel
public class EquipmentReqDTO {
    /**
     * 记录编号
     */
    @ApiModelProperty(value = "记录编号")
    private String recId;
    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    private String companyCode;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 部门编号
     */
    @ApiModelProperty(value = "部门编号")
    private String deptCode;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String equipCode;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    /**
     * 专业编号
     */
    @ApiModelProperty(value = "专业编号")
    private String majorCode;
    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业名称")
    private String majorName;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号")
    private String systemCode;
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    /**
     * 设备类型编号
     */
    @ApiModelProperty(value = "设备类型编号")
    private String equipTypeCode;
    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private String equipTypeName;
    /**
     * 是否为特殊设备 10 20
     */
    @ApiModelProperty(value = "是否为特殊设备 10 20")
    private String specialEquipFlag;
    /**
     * 是否为其他设备 10 20
     */
    @ApiModelProperty(value = "是否为其他设备 10 20")
    private String otherEquipFlag;
    /**
     * 设备来源
     */
    @ApiModelProperty(value = "设备来源")
    private String sourceKind;
    /**
     * BOM类型
     */
    @ApiModelProperty(value = "BOM类型")
    private String bomType;
    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacture;
    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号")
    private String orderNo;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String orderName;
    /**
     * 型号规格
     */
    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 出厂日期
     */
    @ApiModelProperty(value = "出厂日期")
    private String manufactureDate;
    /**
     * 出厂编号
     */
    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;
    /**
     * 开始使用时间
     */
    @ApiModelProperty(value = "开始使用时间")
    private String startUseDate;
    /**
     * 停止使用时间
     */
    @ApiModelProperty(value = "停止使用时间")
    private String endUseTime;
    /**
     * 其他特征参数
     */
    @ApiModelProperty(value = "其他特征参数")
    private String otherFeature;
    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    /**
     * 进设备台账时间
     */
    @ApiModelProperty(value = "进设备台账时间")
    private String inAccountTime;
    /**
     * 领用人工号
     */
    @ApiModelProperty(value = "领用人工号")
    private String pickNo;
    /**
     * 领用人姓名
     */
    @ApiModelProperty(value = "领用人姓名")
    private String pickName;
    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    private String equipStatus;
    /**
     * 来源单号
     */
    @ApiModelProperty(value = "来源单号")
    private String sourceAppNo;
    /**
     * 来源明细号
     */
    @ApiModelProperty(value = "来源明细号")
    private String sourceSubNo;
    /**
     * 来源记录编号
     */
    @ApiModelProperty(value = "来源记录编号")
    private String sourceRecId;
    /**
     * 安装单位
     */
    @ApiModelProperty(value = "安装单位")
    private String installDealer;
    /**
     * 附件编号
     */
    @ApiModelProperty(value = "附件编号")
    private String docId;
    /**
     * 来源线别码
     */
    @ApiModelProperty(value = "来源线别码")
    private String originLineNo;
    /**
     * 来源线别名称
     */
    @ApiModelProperty(value = "来源线别名称")
    private String originLineName;
    /**
     * 来源线段代码
     */
    @ApiModelProperty(value = "来源线段代码")
    private String originSegNo;
    /**
     * 来源线段名称
     */
    @ApiModelProperty(value = "来源线段名称")
    private String originSegName;
    /**
     * 应用线别代码
     */
    @ApiModelProperty(value = "应用线别代码")
    private String useLineNo;
    /**
     * 应用线别名称
     */
    @ApiModelProperty(value = "应用线别名称")
    private String useLineName;
    /**
     * 应用线段代码
     */
    @ApiModelProperty(value = "应用线段代码")
    private String useSegNo;
    /**
     * 应用线段名称
     */
    @ApiModelProperty(value = "应用线段名称")
    private String useSegName;
    /**
     * 位置一
     */
    @ApiModelProperty(value = "位置一")
    private String position1Code;
    /**
     * 位置一名称
     */
    @ApiModelProperty(value = "位置一名称")
    private String position1Name;
    /**
     * 位置二
     */
    @ApiModelProperty(value = "位置二")
    private String position2Code;
    /**
     * 位置二名称
     */
    @ApiModelProperty(value = "位置二名称")
    private String position2Name;
    /**
     * 位置三
     */
    @ApiModelProperty(value = "位置三")
    private String position3;
    /**
     * 位置补充说明
     */
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;
    /**
     * 行走里程
     */
    @ApiModelProperty(value = "行走里程")
    private Long totalMiles;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 审批状态
     */
    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;
    /**
     * 工作流实例id
     */
    @ApiModelProperty(value = "工作流实例id")
    private String workFlowInstId;
    /**
     * 工作流实例状态
     */
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    /**
     * 特种设备检测日期
     */
    @ApiModelProperty(value = "特种设备检测日期")
    private String verifyDate;
    /**
     * 特种设备检测有效期
     */
    @ApiModelProperty(value = "特种设备检测有效期")
    private String verifyValidityDate;
    /**
     * 档案标记
     */
    @ApiModelProperty(value = "档案标记")
    private String archiveFlag;
    /**
     * 扩展字段1
     */
    @ApiModelProperty(value = "扩展字段1")
    private String ext1;
    /**
     * 扩展字段2
     */
    @ApiModelProperty(value = "扩展字段2")
    private String ext2;
    /**
     * 扩展字段3
     */
    @ApiModelProperty(value = "扩展字段3")
    private String ext3;
    /**
     * 扩展字段4
     */
    @ApiModelProperty(value = "扩展字段4")
    private String ext4;
    /**
     * 扩展字段5
     */
    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String recCreator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
    /**
     * 编辑人
     */
    @ApiModelProperty(value = "创建人")
    private String recRevisor;
    /**
     * 编辑时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recReviseTime;
    /**
     * 删除者
     */
    @ApiModelProperty(value = "删除者")
    private String recDeletor;
    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;
    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;
    /**
     * 记录状态
     */
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
}
