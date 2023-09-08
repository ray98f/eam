package com.wzmtr.eam.dto.req.overhaul;

import com.wzmtr.eam.dto.res.overhaul.OverhaulStateOrderResDTO;
import lombok.Data;

@Data
public class OverhaulUpStateReqDTO {
    private String recId;
    private String orderCode;
    private String objectCode;
    private OverhaulStateOrderResDTO resDTO;
}
