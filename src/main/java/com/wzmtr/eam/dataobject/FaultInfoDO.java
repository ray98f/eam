package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 10:53
 */
@Data
@TableName(value = "T_FAULT_INFO")
public class FaultInfoDO {
    @TableId(value = "REC_ID")
    private String recId;
    /**
     * not null
     */
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    /**
     * not null
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障标识（1-标准故障；2-快速故障）")
    private String faultFlag;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    /**
     * not null
     */
    @ApiModelProperty(value = "线路编码")
    private String lineCode;
    /**
     * not null
     */
    @ApiModelProperty(value = "位置编码")
    private String positionCode;
    @TableField(exist = false)
    @ApiModelProperty(value = "位置")
    private String positionName;
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;

    @ApiModelProperty(value = "位置2")
    private String position2Name;
    @ApiModelProperty(value = "部件编码")
    private String partCode;
    @ApiModelProperty(value = "部件名称")
    private String partName;
    @TableField(exist = false)
    @ApiModelProperty(value = "专业")
    private String majorName;
    @TableField(exist = false)
    @ApiModelProperty(value = "系统")
    private String systemName;
    @TableField(exist = false)
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    @ApiModelProperty(value = "专业代码")
    private String majorCode;
    @ApiModelProperty(value = "系统代码")
    private String systemCode;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    /**
     * not null
     */
    @ApiModelProperty(value = "来源编号")
    private String sourceCode;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayCode;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    /**
     * not null
     */
    @ApiModelProperty(value = "发现人姓名")
    private String discovererName;
    /**
     * not null
     */
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "发现人联系方式")
    private String discovererPhone;
    /**
     * not null
     */
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    /**
     * not null
     */
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptCode;
    /**
     * not null
     */
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    /**
     * not null
     */
    @TableField(exist = false)
    private String fillinUserName;
    @ApiModelProperty(value = "主责部门")
    private String respDeptCode;
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    @ApiModelProperty(value = "下发人工号")
    private String publishUserId;
    @ApiModelProperty(value = "下发时间")
    private String publishTime;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障等级")
    private String faultLevel;
    /**
     * not null
     */
    @ApiModelProperty(value = "故障状态")
    private String faultStatus;
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    /**
     * not null
     */
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * not null
     */
    @ApiModelProperty(value = "创建者")
    private String recCreator;
    /**
     * not null
     */
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
    /**
     * not null
     */
    @ApiModelProperty(value = "状态")
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
    @ApiModelProperty(value = "检修车/运营车标识")
    private String trainTag;
    @ApiModelProperty(value = "故障模块")
    private String faultModuleId;
}
