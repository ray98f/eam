package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/12/14 15:21
 */
@Data
@TableName("SYS_REGION")
public class RegionDO {
    @TableId(value = "REC_ID")
    private String recId;
    private String companyCode;
    private String companyName;
    private String nodeCode;
    private String nodeName;
    private String parentNodeRecId;
    private Integer nodeLevel;
    private String lineCode;
    private String remark;
    private String recCreator;
    private String recCreateTime;
    private String recRevisor;
    private String recReviseTime;
    private String recDeletor;
    private String recDeleteTime;
    private String deleteFlag;
    private String archiveFlag;
    private String recStatus;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

}
