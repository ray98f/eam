package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.PersonResDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:21
 */
public interface FaultQueryService {
    Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO);

    List<FaultDetailResDTO> statisticList(FaultQueryDetailReqDTO reqDTO);

    String queryOrderStatus(SidEntity reqDTO);

    void issue(FaultDetailReqDTO reqDTO);

    void export(Set<String> faultNos, Set<String> faultWorkNos, HttpServletResponse response);

    Page<ConstructionResDTO> construction(FaultQueryReqDTO reqDTO);

    Page<ConstructionResDTO> cancellation(FaultQueryReqDTO reqDTO);

    void transmit(FaultQueryReqDTO reqDTO);

    void submit(FaultSubmitReqDTO reqDTO);

    List<PersonResDTO> queryUserList(Set<String> userCode, String organCode);

    Boolean compareRows(CompareRowsReqDTO req);

    // 驳回
    void returns(FaultSubmitReqDTO reqDTO);

    void sendWork(FaultSendWorkReqDTO reqDTO);

    void eqCheck(FaultEqCheckReqDTO reqDTO) throws Exception;

    void updateHandler(FaultNosFaultWorkNosReqDTO reqDTO);

    List<FaultRepairDeptResDTO> querydept(String faultNo);

    List<OrganMajorLineType> queryWorker(String workerGroupCode);
}
