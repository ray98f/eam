package com.wzmtr.eam.dto.res.common;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/11/13 14:39
 */
@Data
@ApiModel
public class FlowRoleResDTO {
    private String nodeId;
    private String nodeName;
    private String flowId;
    private String flowName;
    private String roleId;
    private String roleName;
    private String isOwnerOrg;
    private String step;
}
