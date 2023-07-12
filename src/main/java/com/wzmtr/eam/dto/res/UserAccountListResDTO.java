package com.wzmtr.eam.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserAccountListResDTO {

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
}
