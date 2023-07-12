package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SysUser extends BaseEntity{

    @ApiModelProperty(value = "登录用户名")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "账号过期标记")
    private Integer expired;

    @ApiModelProperty(value = "账号锁定标记")
    private Integer disabled;

    @ApiModelProperty(value = "工号")
    private String no;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "部门ID")
    private String officeId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "类型")
    private String userType;

    @ApiModelProperty(value = "登录标识")
    private String loginFlag;

    @ApiModelProperty(value = "新入职标识")
    private String newWorker;

    @ApiModelProperty(value = "照片")
    private String photo;

    @ApiModelProperty(value = "生日")
    private Date birthDate;

    @ApiModelProperty(value = "转正日期")
    private Date hireDate;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "显示序号")
    private Integer orderNum;

    @ApiModelProperty(value = "办公地点")
    private String room;

    @ApiModelProperty(value = "是否股级以上")
    private String flagposition;

    @ApiModelProperty(value = "是否业主代表")
    private String cOwner;

    @ApiModelProperty(value = "mdm人员ID")
    private String personId;
}
