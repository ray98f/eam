package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Bom {

    @ApiModelProperty(value = "统一编号")
    private String recId;

    @ApiModelProperty(value = "代码编号")
    private String ename;

    @ApiModelProperty(value = "代码名称")
    private String cname;

    @ApiModelProperty(value = "父节点")
    private String parentId;

    @ApiModelProperty(value = "是否叶子节点")
    private String isLeaf;

    @ApiModelProperty(value = "公司代码")
    private String companyCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "表名")
    private String tblname;

    @ApiModelProperty(value = "关联字段id")
    private String relationId;

    @ApiModelProperty(value = "排序编号")
    private BigDecimal sortIndex;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @ApiModelProperty(value = "记录创建者")
    private String recCreator;

    @ApiModelProperty(value = "记录创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "记录修改者")
    private String recRevisor;

    @ApiModelProperty(value = "记录修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "记录删除者")
    private String recDeletor;

    @ApiModelProperty(value = "记录删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标记")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
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

    @ApiModelProperty(value = "树形节点")
    private String treeId;

}
            
