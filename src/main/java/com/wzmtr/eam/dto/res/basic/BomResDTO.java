package com.wzmtr.eam.dto.res.basic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Bom结构结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/04
 */
@Data
public class BomResDTO {
    /**
     * 统一编号
     */
    private String recId;
    /**
     * 代码编号
     */
    private String ename;
    /**
     * 代码名称
     */
    private String cname;
    /**
     * 父节点
     */
    private String parentId;
    /**
     * 是否叶子节点
     */
    private String isLeaf;
    /**
     * 公司代码
     */
    private String companyCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 表名
     */
    private String tblname;
    /**
     * 关联字段id
     */
    private String relationId;
    /**
     * 排序编号
     */
    private BigDecimal sortIndex;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 计量单位
     */
    private String measureUnit;
    /**
     * 记录创建者
     */
    private String recCreator;
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
     * 归档标记
     */
    private String archiveFlag;
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
    /**
     * 树形节点
     */
    private String treeId;
}
