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
    // 统一编号
    @TableId(value = "REC_ID")
     private String recId = " ";
    // 代码编号
    /*  21 */   private String ename = " ";
    // 代码名称
    /*  22 */   private String cname = " ";
    // 父节点
    /*  23 */   private String parentId = " ";
    // 是否叶子节点
    /*  24 */   private String isLeaf = " ";
    // 公司代码
    /*  25 */   private String companyCode = " ";
    // 状态
    /*  26 */   private String status = " ";
    // 表名
    /*  27 */   private String tblname = " ";
    // 关联字段id
    /*  28 */   private String relationId = " ";
    // 排序编号
    /*  29 */   private BigDecimal sortIndex;
    // 数量
    /*  30 */   private BigDecimal quantity;
    // 计量单位
    /*  31 */   private String measureUnit = " ";
    // 记录创建者
    /*  32 */   private String recCreator = " ";
    // 记录创建时间
    /*  33 */   private String recCreateTime = " ";
    // 记录修改者
    /*  34 */   private String recRevisor = " ";
    // 记录修改时间
    /*  35 */   private String recReviseTime = " ";
    // 记录删除者
    /*  36 */   private String recDeletor = "NULL";
    // 记录删除时间
    /*  37 */   private String recDeleteTime = "NULL";
    // 删除标记
    /*  38 */   private String deleteFlag = " ";
    // 归档标记
    /*  39 */   private String archiveFlag = " ";
    // 扩展字段1
    /*  40 */   private String ext1 = " ";
    // 扩展字段2
    /*  41 */   private String ext2 = " ";
    // 扩展字段3
    /*  42 */   private String ext3 = " ";
    // 扩展字段4
    /*  43 */   private String ext4 = " ";
    // 扩展字段5
    /*  44 */   private String ext5 = " ";
    // 树形节点
    private String treeId = " ";
}
