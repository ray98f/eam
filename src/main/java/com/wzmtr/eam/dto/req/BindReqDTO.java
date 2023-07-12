package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel
public class BindReqDTO {

    @ApiModelProperty(value = "部门id")
    @NotBlank(message = "32000006")
    private String officeId;

    @ApiModelProperty(value = "人员id列表")
    private List<String> personList;

    private String createUser;

    private String deleteUser;

    private Integer userLevel;

    private String userId;

}
