package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.FaultReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FaultService {

    Page<FaultResDTO> listFault(String code, Integer type, String lineCode, String equipmentCategoryCode, String equipmentTypeName, PageReqDTO pageReqDTO);

    FaultResDTO getFaultDetail(String id);

    void addFault(FaultReqDTO faultReqDTO);

    void modifyFault(FaultReqDTO faultReqDTO);

    void deleteFault(BaseIdsEntity baseIdsEntity);

    void exportFault(List<String> ids, HttpServletResponse response) throws IOException;

    /**
     * 故障查询获取码值列表
     * @param code 故障码
     * @param type 故障类型
     * @param lineCode 线路编号
     * @param equipmentCategoryCode 设备类别编号
     * @return 码值列表
     */
    List<FaultResDTO> listQueryFault(String code, Integer type, String lineCode, String equipmentCategoryCode);
}
