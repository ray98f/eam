package com.wzmtr.eam.dto.req;

import com.wzmtr.eam.dto.res.OverhaulStateOrderResDTO;
import lombok.Data;

@Data
public class OverhaulUpStateReqDTO {
    private String recId;
    private String orderCode;
    private String objectCode;
    private OverhaulStateOrderResDTO resDTO;
}
