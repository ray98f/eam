package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:21
 */
public interface FaultQueryService {
    Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO);

    List<FaultDetailResDTO> queryLimit();

    Page<FaultDetailResDTO> statustucList(FaultQueryReqDTO reqDTO);

    String queryOrderStatus(SidEntity reqDTO);

    void issue(FaultDetailReqDTO reqDTO);

    void export(FaultExportReqDTO reqDTO, HttpServletResponse response);

    Page<ConstructionResDTO> construction(FaultQueryReqDTO reqDTO);

    Page<ConstructionResDTO> cancellation(FaultQueryReqDTO reqDTO);


    List<PersonResDTO> queryUserList(Set<String> userCode, String organCode);

    Boolean compareRows(CompareRowsReqDTO req);

    void sendWork(FaultSendWorkReqDTO reqDTO);

    /**
     * 完工
     * @param reqDTO 完工返回数据
     */
    void finishWork(FaultFinishWorkReqDTO reqDTO);

    void eqCheck(FaultEqCheckReqDTO reqDTO) throws Exception;

    void updateHandler(FaultNosFaultWorkNosReqDTO reqDTO);

    List<FaultRepairDeptResDTO> querydept(String faultNo);

    List<OrganMajorLineType> queryWorker(String workerGroupCode);

    void pageMaterial(String orderCode, HttpServletResponse response) throws IOException;
}
