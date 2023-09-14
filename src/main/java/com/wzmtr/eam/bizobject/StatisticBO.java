package com.wzmtr.eam.bizobject;

import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/29 10:20
 */
@Data
public class StatisticBO {
    private List<Integer> count;
    private String fillinTime;
    private String objName;
}
