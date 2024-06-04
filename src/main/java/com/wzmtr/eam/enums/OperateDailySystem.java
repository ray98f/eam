package com.wzmtr.eam.enums;

import com.wzmtr.eam.entity.EquipmentCategory;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * 运营日报系统枚举类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/04
 */
@Getter
public enum OperateDailySystem {
    /**
     * 变配电
     */
    CHANGE_DISTRIBUTION("变配电",new EquipmentCategory(Collections.singletonList("13"), Collections.singletonList("1301"))),
    /**
     * 接触网
     */
    CONTACT_NETWORK("接触网", new EquipmentCategory(Collections.singletonList("12"))),
    /**
     * 通信
     */
    COMMUNICATION("通信", new EquipmentCategory(Collections.singletonList("08"))),
    /**
     * 信号
     */
    SIGNAL("信号", new EquipmentCategory(Collections.singletonList("09"))),
    /**
     * 站台门
     */
    PLATFORM_DOORS("站台门", new EquipmentCategory(Collections.singletonList("01"))),
    /**
     * 风水电
     */
    HYDROPOWER("风水电", new EquipmentCategory(Arrays.asList("03", "04", "05"))),
    /**
     * 房建
     */
    BUILDING_CONSTRUCTION("房建", new EquipmentCategory(Collections.singletonList("70"), Collections.singletonList("7001"))),
    /**
     * 综合监控
     */
    MONITOR("综合监控", new EquipmentCategory(Collections.singletonList("10"))),
    /**
     * AFC
     */
    AFC("AFC", new EquipmentCategory(Collections.singletonList("11"))),
    /**
     * FAS
     */
    FAS("FAS", new EquipmentCategory(Collections.singletonList("10"), Collections.singletonList("1002"))),
    /**
     * 电扶梯
     */
    ESCALATOR("电扶梯", new EquipmentCategory(Collections.singletonList("02"))),
    /**
     * 工务
     */
    OFFICIAL_DUTIES("工务", new EquipmentCategory(Collections.singletonList("14"))),
    /**
     * 桥隧
     */
    BRIDGE_TUNNEL("桥隧", new EquipmentCategory(Collections.singletonList("70"), Collections.singletonList("7002"))),
    /**
     * 工程车
     */
    ENGINEERING_VEHICLE("工程车", new EquipmentCategory(Collections.singletonList("17"))),
    /**
     * 车辆
     */
    VEHICLE("车辆", new EquipmentCategory(Collections.singletonList("07")));

    private final String name;
    private final EquipmentCategory equipmentCategory;

    OperateDailySystem(String name, EquipmentCategory equipmentCategory) {
        this.name = name;
        this.equipmentCategory = equipmentCategory;
    }
}
