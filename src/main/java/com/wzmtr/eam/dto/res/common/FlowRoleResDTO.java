package com.wzmtr.eam.dto.res.common;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/11/13 14:39
 */
@Data
@ApiModel
public class FlowRoleResDTO {
    //流程节点Id
    private String nodeId;
    //流程节点名称
    private String nodeName;
    //流程ID
    private String flowId;
    //流程名称
    private String flowName;
    //角色ID
    private String roleId;
    //角色名称
    private String roleName;
    private String isOwnerOrg;
    //第几步
    private String step;
    //属于哪一条流程线
    private String line;
    //父节点
    private String parentId;

    private List<PersonListResDTO> person;

}
