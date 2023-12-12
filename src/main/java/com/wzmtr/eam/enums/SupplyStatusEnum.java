package com.wzmtr.eam.enums;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lize
 * @Date 2023/6/9
 */
public enum SupplyStatusEnum {
    //当麒麟流程发布新版本时需要更新modelId与defId
    draft("draft", "草稿"),
    approving("approving", "审批中"),
    approved("approved", "审批完成"),
    unapproved("unapproved", "审批不通过");
    private String value;
    private String label;

    SupplyStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return this.value;
    }

    public String label() {
        return this.label;
    }

    public static String getLabelByValue(String value) {
        for (SupplyStatusEnum orgTypeEnum : SupplyStatusEnum.values()) {
            if (orgTypeEnum.value.equals(value)) {
                return orgTypeEnum.label;
            }
        }
        // 如果未找到匹配的 label，则返回 null 或者抛出异常
        return null;
    }

    public static String getValueByLabel(String label) {
        for (SupplyStatusEnum orgTypeEnum : SupplyStatusEnum.values()) {
            if (orgTypeEnum.label.equals(label)) {
                return orgTypeEnum.value;
            }
        }
        // 如果未找到匹配的 label，则返回 null 或者抛出异常
        return null;
    }

    public static List<HashMap<String, String>> printAllValuesAndLabels() {
        List<HashMap<String, String>> list = new ArrayList<>();

        for (SupplyStatusEnum orgTypeEnum : SupplyStatusEnum.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", orgTypeEnum.value);
            map.put("name", orgTypeEnum.label);
            list.add(map);

        }
        return list;
    }

}
