package com.wzmtr.eam.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserAccountDetailResDTO {

    @ApiModelProperty(value = "父id路径")
    @JsonIgnore
    private String parentIds;

    @ApiModelProperty(value = "所属组织")
    private String orgPath;

    @ApiModelProperty(value = "职位")
    private String postName;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "0 正常 1 锁定")
    private Integer disabled;

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "人员编号")
    private String no;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "后台角色")
    private List<UserRoleResDTO> roles;

    @ApiModelProperty(value = "EIP角色")
    private List<UserRoleResDTO> eipRoles;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "办公地点")
    private String room;

    @ApiModelProperty(value = "入职时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date hireDate;

    public UserAccountDetailResDTO() {
    }

    public UserAccountDetailResDTO(String parentIds, String orgPath, String postName, String id, Integer disabled, String loginName, String no, String name, List<UserRoleResDTO> roles, List<UserRoleResDTO> eipRoles, String email, String phone, String mobile, String room, Date hireDate) {
        this.parentIds = parentIds;
        this.orgPath = orgPath;
        this.postName = postName;
        this.id = id;
        this.disabled = disabled;
        this.loginName = loginName;
        this.no = no;
        this.name = name;
        this.roles = roles;
        this.eipRoles = eipRoles;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.room = room;
        this.hireDate = hireDate;
    }

    public UserAccountDetailResDTO(String id, String no, String name) {
        this.id = id;
        this.no = no;
        this.name = name;
    }
}
