package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * 麒麟审核人
 *
 * @Author lize
 * @Date 2023/5/30
 */
@Data
public class NodeInfo {
    private String nodeId;
    private String nodeHanderUser;

    @Override
    public String toString() {
        return "{\"nodeId\":\"" + getNodeId() + "\",\"nodeHanderUser\":\"" + getNodeHanderUser() + "\"}";
    }

}
