package com.wzmtr.eam.dto.res.fault;

import com.wzmtr.eam.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 故障管理-故障跟踪工单派工人员结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/14
 */
@Data
@ApiModel
public class FaultFollowDispatchUserResDTO {
    /**
     * 作业工班id
     */
    @ApiModelProperty(value = "作业工班id")
    private String workerOrgId;
    /**
     * 作业工班名称
     */
    @ApiModelProperty(value = "作业工班名称")
    private String workerOrgName;
    /**
     * 作业人员列表
     */
    @ApiModelProperty(value = "作业人员列表")
    private List<SysUser> workerList;
}
