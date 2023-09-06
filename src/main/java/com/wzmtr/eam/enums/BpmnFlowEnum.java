package com.wzmtr.eam.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lize
 * @Date 2023/6/9
 */
public enum BpmnFlowEnum {
    //当麒麟流程发布新版本时需要更新modelId与defId
    CHECK_PLAN_SUBMIT("check_plan_submit", "计量器具检测计划流程"),
    SUBMISSION_RECORD_SUBMIT("submission_record_submit", "计量器具送检流程"),
    FAULT_TRACK("fault_track", "故障跟踪流程"),
    DETECTION_PLAN_SUBMIT("detection_plan_submit", "特种设备检测计划流程"),
    DETECTION_SUBMIT("detection_submit", "特种设备检测记录流程"),
    OVERHAUL_TPL_SUBMIT("overhaul_tpl_submit", "检修模板审批流程"),
    ORDER_PLAN_SUBMIT("order_plan_submit", "检修计划审批流程"),
    OVERHAUL_WEEK_PLAN_SUBMIT("overhaul_week_plan_submit", "检修周计划流程"),
    SUBMISSION_SUBMIT("submission_submit", "计量器具检测单流程");
    private String value;
    private String label;

    BpmnFlowEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static BpmnFlowEnum find(String flowId) {
        for (BpmnFlowEnum bpmnFlowEnum : values()) {
            if (bpmnFlowEnum.value.equals(flowId)) {
                return bpmnFlowEnum;
            }
        }
        return null;
    }

    public String value() {
        return this.value;
    }

    public String label() {
        return this.label;
    }

    public static String getLabelByValue(String value) {
        for (BpmnFlowEnum orgTypeEnum : BpmnFlowEnum.values()) {
            if (orgTypeEnum.value.equals(value)) {
                return orgTypeEnum.label;
            }
        }
        return null; // 如果未找到匹配的 label，则返回 null 或者抛出异常
    }

    public static String getValueByLabel(String label) {
        for (BpmnFlowEnum orgTypeEnum : BpmnFlowEnum.values()) {
            if (orgTypeEnum.label.equals(label)) {
                return orgTypeEnum.value;
            }
        }
        return null; // 如果未找到匹配的 label，则返回 null 或者抛出异常
    }

    public static List<HashMap<String, String>> printAllValuesAndLabels() {
        List<HashMap<String, String>> list = new ArrayList<>();

        for (BpmnFlowEnum orgTypeEnum : BpmnFlowEnum.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", orgTypeEnum.value);
            map.put("name", orgTypeEnum.label);
            list.add(map);

        }
        return list;
    }

}
