package com.wzmtr.eam.dto.res.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
@ApiModel
public class MemberResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "父id路径")
    @JsonIgnore
    private String parentId;

    @ApiModelProperty(value = "父id路径")
    @JsonIgnore
    private String parentIds;

    @ApiModelProperty(value = "所属组织")
    private String orgPath;

    @ApiModelProperty(value = "职位")
    private String postName;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "办公地点")
    private String room;

    @ApiModelProperty(value = "是否显示手机号. 0否, 1是")
    private String showMobileFlag;

    @ApiModelProperty(value = "性别 0 未知 1 男 2 女")
    private String sex;

    @ApiModelProperty(value = "头像图片路径")
    private String avatarPic;

    @ApiModelProperty(value = "工作状态")
    private String workStatus;

    @ApiModelProperty(value = "帐号状态")
    private String isLock;

    @ApiModelProperty(value = "账号有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date lockTime;

    @ApiModelProperty(value = "内部eip角色")
    private String eipRole;
}
