package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/14 20:20
 */
@Data
@ApiModel
public class CompareRowsReqDTO {
    //选中的行
    List<FaultDetailResDTO> list;
}
