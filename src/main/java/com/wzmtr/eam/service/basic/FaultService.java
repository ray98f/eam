package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.FaultReqDTO;
import com.wzmtr.eam.dto.res.FaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FaultService {

    Page<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode, PageReqDTO pageReqDTO);

    FaultResDTO getFaultDetail(String id);

    void addFault(FaultReqDTO faultReqDTO);

    void modifyFault(FaultReqDTO faultReqDTO);

    void deleteFault(BaseIdsEntity baseIdsEntity);

    void exportFault(String code, Integer type, String lineCode, String equipmentCategoryCode, HttpServletResponse response);
}
