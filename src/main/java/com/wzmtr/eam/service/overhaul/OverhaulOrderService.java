package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulUpStateReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface OverhaulOrderService {

    Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO);

    OverhaulOrderResDTO getOverhaulOrderDetail(String id);

    void exportOverhaulOrder(List<String> ids, HttpServletResponse response);

    List<FaultRepairDeptResDTO> queryDept(String id);

    List<OrganMajorLineType> queryWorker(String workerGroupCode);

    void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException;

    void cancellWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void pageMaterial();

    void receiveMaterial(HttpServletResponse response) throws IOException;

    void returnMaterial();

    Page<ConstructionResDTO> construction(String orderCode, PageReqDTO pageReqDTO);

    Page<ConstructionResDTO> cancellation(String orderCode, PageReqDTO pageReqDTO);

    Page<OverhaulOrderDetailResDTO> pageOverhaulObject(String orderCode, String planCode, String planName, String objectCode, PageReqDTO pageReqDTO);

    OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id);

    void exportOverhaulObject(String orderCode, String planCode, String planName, String objectCode, HttpServletResponse response);

    void checkjx(String orderCode);

    Page<OverhaulItemResDTO> pageOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, PageReqDTO pageReqDTO);

    OverhaulItemResDTO getOverhaulItemDetail(String id);

    void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, HttpServletResponse response);

    Page<OverhaulStateResDTO> pageOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, PageReqDTO pageReqDTO);

    OverhaulStateResDTO getOverhaulStateDetail(String id);

    void exportOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, HttpServletResponse response);

    OverhaulStateOrderResDTO queryOrderInfo(String orderCode);

    void upState(OverhaulUpStateReqDTO overhaulUpStateReqDTO);
}
