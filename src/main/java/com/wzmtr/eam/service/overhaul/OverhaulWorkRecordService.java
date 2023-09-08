package com.wzmtr.eam.service.overhaul;

import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;

public interface OverhaulWorkRecordService {

    void insertRepair(OverhaulOrderReqDTO overhaulOrderReqDTO);

}
