package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/23
 */
@Data
public class FlowRes {
    private String modelId;
    private String defId;
    private String defKey;
    private String version;
    private String name;
    private String lastUpdated;
    private String groupId;

}
