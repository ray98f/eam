package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 15:22
 */
@Data
@ApiModel
public class CarObjectReqDTO {
    private String type;
    private String label;
    private String text;
    private String nodeCode;
    private String line;
    private String node;
    private String sort;
    private String nodeName;
    private String position;
    private String carEquipCode;
    private String carEquipName;
}
