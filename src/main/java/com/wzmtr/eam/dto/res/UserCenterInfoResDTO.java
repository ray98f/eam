package com.wzmtr.eam.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class UserCenterInfoResDTO {

    @ApiModelProperty(value = "人员ID")
    private String id;

    private String photo;

    private String bigPhoto;

    private String smallPhoto;

    @ApiModelProperty(value = "人员编号")
    private String no;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    private String room;

    private String postName;

    private String companyId;

    private String companyName;

    private String officeName;

    private String officeId;

    @ApiModelProperty(value = "是否显示手机号. 0否, 1是")
    private String showMobileFlag;

    @ApiModelProperty(value = "性别 0 未知 1 男 2 女")
    private String sex;

    @ApiModelProperty(value = "头像图片路径")
    private String avatarPic;

    @ApiModelProperty(value = "工作状态")
    private String workStatus;

    @ApiModelProperty(value = "入职时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date hireDate;

    @ApiModelProperty(value = "入职日期")
    private Integer distanceDay;

    @ApiModelProperty(value = "操作手册")
    private String operationManual;

    @ApiModelProperty(value = "用户权限")
    private List<String> userRoles;
}
