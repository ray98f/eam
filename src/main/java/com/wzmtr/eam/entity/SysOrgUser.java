package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class SysOrgUser {

    // departfullpath, jobcategory, jobgrade, activestatus, joblevel, jobname, jobcategoryname, joblevelname,
    // jobgradename, positionlevel, jobleveldate, mdm_user_id, oldinfoid, procresult

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "所属公司")
    private String companyId;

    @ApiModelProperty(value = "所属部门")
    private String officeId;

    @ApiModelProperty(value = "员工编号")
    private String userId;

    @ApiModelProperty(value = "所属部门名称")
    private String officeName;

    @ApiModelProperty(value = "岗位编号")
    private String postcode;

    @ApiModelProperty(value = "岗位名称")
    private String postname;

    @ApiModelProperty(value = "最新到岗日期")
    private Date latestarridate;

    @ApiModelProperty(value = "所属分公司名称")
    private String companyname;

    @ApiModelProperty(value = "离职日期")
    private Date leavedate;

    @ApiModelProperty(value = "状态,1主岗")
    private String leavestatus;

    @ApiModelProperty(value = "所在组织路径")
    private String departfullpath;

    @ApiModelProperty(value = "岗位序列")
    private String jobcategory;

    @ApiModelProperty(value = "职等")
    private String jobgrade;

    @ApiModelProperty(value = "在职状态,1在职0不在职")
    private String activestatus;

    @ApiModelProperty(value = "职级")
    private String joblevel;

    @ApiModelProperty(value = "职位名称")
    private String jobname;

    @ApiModelProperty(value = "岗位序列名称")
    private String jobcategoryname;

    @ApiModelProperty(value = "职级名称")
    private String joblevelname;

    @ApiModelProperty(value = "职等名称")
    private String jobgradename;

    @ApiModelProperty(value = "职位层级")
    private String positionlevel;

    @ApiModelProperty(value = "职级开始日期")
    private Date jobleveldate;

    @ApiModelProperty(value = "主数据表用户主键")
    private String mdmUserId;

    @ApiModelProperty(value = "变动前信息ID")
    private String oldinfoid;

    @ApiModelProperty(value = "审批状态")
    private String procresult;
}
