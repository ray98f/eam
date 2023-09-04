package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class ReliabilityDetailResDTO {
    List<String> month;
    List<String> data;
    String name;
}