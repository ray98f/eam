package com.wzmtr.eam.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
