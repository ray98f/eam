package com.wzmtr.eam.dto.res.home;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/13 8:37
 */
@Data
@ApiModel
public class EChartResDTO {
    private List<ShowAResDTO> showA;
    private List<ShowBCResDTO> showCount;
}
