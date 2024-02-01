package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

import java.util.List;

/**
 * 流程图结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/31
 */
@Data
public class FlowChartRes {
    /**
     * 流程
     */
    private List<FlowDetail> flowDetails;
    /**
     * 模型xml
     */
    private String xml;
    /**
     * 完成的节点key
     */
    private List<String> completed;
    /**
     * 运行的节点key
     */
    private List<String> runing;

    /**
     * 流程
     */
    @Data
    public static class FlowDetail {
        /**
         * 意见
         */
        private String opinion;
        /**
         * 颜色
         */
        private String color;
        /**
         * icon
         */
        private String icon;
        /**
         * 卡片类型
         */
        private String cardType;
        /**
         * 任务名称
         */
        private String taskName;
        /**
         * 操作者
         */
        private String handler;
        /**
         * 任务key
         */
        private String taskKey;
        /**
         * 任务附件
         */
        private String bpmTaskAttachment;
        /**
         * 文件模型
         */
        private String fileModel;
        /**
         * 创建时间
         */
        private String create;
        /**
         * 完成时间
         */
        private String end;
    }


}
