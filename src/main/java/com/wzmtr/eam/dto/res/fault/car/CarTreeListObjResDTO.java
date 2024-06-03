package com.wzmtr.eam.dto.res.fault.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/10/17 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarTreeListObjResDTO {
    /**
     * 英文名
     */
    private String label;
    /**
     * 中文名
     */
    private String text;
    /**
     * 父级英文名
     */
    private String pId;
    /**
     * 位置
     */
    private String position;
    /**
     * 位置名
     */
    private String positionName;
    /**
     * 类型
     */
    private String type;
    /**
     * 是否有叶子节点
     */
    private Boolean leaf;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 节点Code
     */
    private String nodeCode;
    /**
     * 线路
     */
    private String line;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 车辆对象
     */
    private String carEquipCode;
    /**
     * 车辆对象姓名
     */
    private String carEquipName;
    /**
     * 公司code
     */
    private String companyCode;
    /**
     * 父级
     */
    private String parent;

    public static CarTreeListObjResDTO toS1ResDTO() {
        CarTreeListObjResDTO s1 = new CarTreeListObjResDTO();
        s1.setLabel("01");
        s1.setText("01 S1线");
        s1.setPId("0");
        s1.setType("xl");
        s1.setLeaf(false);
        s1.setPath("0.01");
        s1.setNodeCode("01");
        s1.setLine("01");
        s1.setParent("0");
        return s1;
    }
    public static CarTreeListObjResDTO toS2ResDTO() {
        CarTreeListObjResDTO s2 = new CarTreeListObjResDTO();
        s2.setLabel("02");
        s2.setText("02 S2线");
        s2.setPId("0");
        s2.setType("xl");
        s2.setLeaf(false);
        s2.setPath("0.02");
        s2.setNodeCode("02");
        s2.setLine("02");
        s2.setParent("0");
        return s2;
    }
}
