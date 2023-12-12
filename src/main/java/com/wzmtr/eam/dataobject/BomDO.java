package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Author: Li.Wang
 * Date: 2023/9/5 15:49
 */
@TableName("T_BOM")
@Data
public class BomDO {
    /**
     *    统一编号
     */
    @TableId(value = "REC_ID")
     private String recId = " ";
    /**
     *    代码编号
     */
    private String ename = " ";
    /**
     *    代码名称
     */
    private String cname = " ";
    /**
     *    父节点
     */
    private String parentId = " ";
    /**
     *    是否叶子节点
     */
    private String isLeaf = " ";
    /**
     *    公司代码
     */
    private String companyCode = " ";
    /**
     *    状态
     */
    private String status = " ";
    /**
     *    表名
     */
    private String tblname = " ";
    /**
     *    关联字段id
     */
    private String relationId = " ";
    /**
     *    排序编号
     */
    private BigDecimal sortIndex;
    /**
     *    数量
     */
    private BigDecimal quantity;
    /**
     *    计量单位
     */
    private String measureUnit = " ";
    /**
     *    记录创建者
     */
    private String recCreator = " ";
    /**
     *    记录创建时间
     */
    private String recCreateTime = " ";
    /**
     *    记录修改者
     */
    private String recRevisor = " ";
    /**
     *    记录修改时间
     */
    private String recReviseTime = " ";
    /**
     *    记录删除者
     */
    private String recDeletor = "NULL";
    /**
     *    记录删除时间
     */
    private String recDeleteTime = "NULL";
    /**
     *    删除标记
     */
    private String deleteFlag = " ";
    /**
     *    归档标记
     */
    private String archiveFlag = " ";
    /**
     *    扩展字段1
     */
    private String ext1 = " ";
    /**
     *    扩展字段2
     */
    private String ext2 = " ";
    /**
     *    扩展字段3
     */
    private String ext3 = " ";
    /**
     *    扩展字段4
     */
    private String ext4 = " ";
    /**
     *    扩展字段5
     */
    private String ext5 = " ";
    /**
     *    树形节点
     */
    private String treeId = " ";
}
