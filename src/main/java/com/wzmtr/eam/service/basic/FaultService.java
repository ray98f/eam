package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.FaultReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface FaultService {

    Page<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode, String equipmentTypeName, PageReqDTO pageReqDTO);

    FaultResDTO getFaultDetail(String id);

    void addFault(FaultReqDTO faultReqDTO);

    void modifyFault(FaultReqDTO faultReqDTO);

    void deleteFault(BaseIdsEntity baseIdsEntity);

    void exportFault(String code, Integer type, String lineCode, String equipmentCategoryCode, HttpServletResponse response);
}
