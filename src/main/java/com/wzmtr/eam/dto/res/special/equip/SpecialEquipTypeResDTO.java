package com.wzmtr.eam.dto.res.special.equip;

import lombok.Data;

/**
 * 特种设备分类结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@Data
public class SpecialEquipTypeResDTO {
    /**
     * 记录编号
     */
    private String recId;
    /**
     * 分类编号
     */
    private String typeCode;
    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 检测周期
     */
    private String detectionPeriod;
    /**
     * 创建人
     */
    private String recCreator;
    /**
     * 创建时间
     */
    private String recCreateTime;
    /**
     * 修改人
     */
    private String recRevisor;
    /**
     * 修改时间
     */
    private String recReviseTime;
    /**
     * 删除者
     */
    private String recDeletor;
    /**
     * 删除时间
     */
    private String recDeleteTime;
    /**
     * 删除标志
     */
    private String deleteFlag;
}
