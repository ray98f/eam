package com.wzmtr.eam.bizobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationBO {
    private String mobile;
    private String recId;
    private String stationCode;
    private String userId;
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
