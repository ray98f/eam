package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 工器具结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/28
 */
@Data
@ApiModel
public class MateBorrowResDTO {
    /**
     * 记录ID
     */
    private String recId;
    /**
     * 检修工单
     */
    private String orderCode;
    /**
     * 物资编码
     */
    private String mateCode;
    /**
     * 物资名称
     */
    private String mateName;
    /**
     * 借用人工号
     */
    private String borrowCode;
    /**
     * 借用人姓名
     */
    private String borrowName;
    /**
     * 借用时间
     */
    private String borrowTime;
    /**
     * 归还人姓名
     */
    private String returnName;
    /**
     * 归还人工号
     */
    private String returnCode;
    /**
     * 归还时间
     */
    private String returnTime;
    /**
     * 状态
     */
    private String state;
    /**
     * 备注
     */
    private String remark;
    /**
     * 记录创建时间
     */
    private String recCreateTime;
    /**
     * 记录修改者
     */
    private String recRevisor;
    /**
     * 记录修改时间
     */
    private String recReviseTime;
    /**
     * 记录删除者
     */
    private String recDeletor;
    /**
     * 记录删除时间
     */
    private String recDeleteTime;
    /**
     * 删除标记
     */
    private String deleteFlag;
    /**
     * 扩展字段1
     */
    private String ext1;
    /**
     * 扩展字段2
     */
    private String ext2;
    /**
     * 扩展字段3
     */
    private String ext3;
    /**
     * 扩展字段4
     */
    private String ext4;
    /**
     * 扩展字段5
     */
    private String ext5;
}
