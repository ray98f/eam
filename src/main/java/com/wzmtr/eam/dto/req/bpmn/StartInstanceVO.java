package com.wzmtr.eam.dto.req.bpmn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lize
 * @Date 2023/4/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartInstanceVO {
    private String defId;
    /**
     * 模型别名(key)
     */
    private String defKey;
    /**
     * 表单信息
     */
    private String formData;
    /**
     * 分类id
     */
    private String groupId;
    /**
     * 模型id
     */
    private String modelId;
    private String typeKey;
    private String typeTitle;

    /**
     * 审核人
     * 必须为NodeInfos的特殊格式
     */
    private String nodeInfos;


}
