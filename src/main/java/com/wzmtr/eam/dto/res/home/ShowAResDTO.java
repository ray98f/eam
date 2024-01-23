package com.wzmtr.eam.dto.res.home;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/13 8:37
 */
@Data
@ApiModel
public class ShowAResDTO {
    private String CNT;
    private String CNAME;
    private String STATUS;
}
