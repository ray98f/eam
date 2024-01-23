package com.wzmtr.eam.dto.res.basic;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class FaultRespAndRepairDeptResDTO {
   private List<OrgMajorResDTO> repair;
   private List<OrgMajorResDTO> resp;
}
