package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulUpStateReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public interface OverhaulOrderService {

    Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO);

    OverhaulOrderResDTO getOverhaulOrderDetail(String id);

    void exportOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, HttpServletResponse response);

    void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException;

    void cancellWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

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
