package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface OverhaulOrderService {

    Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO);

    /**
     * 获取检修工单列表-开放接口
     * @param overhaulOrderListReqDTO 检修工单返回信息
     * @param pageReqDTO 分页参数
     * @return 检修工单列表
     */
    Page<OverhaulOrderResDTO> openApiPageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO);

    /**
     * 获取检修工单详情
     * @param id id
     * @return 检修工单详情
     */
    OverhaulOrderResDTO getOverhaulOrderDetail(String id);

    /**
     * 获取工器具分页列表
     * @param orderCode 检修工单
     * @param mateCode 物资编码
     * @param mateName 物资名称
     * @param pageReqDTO 分页参数
     * @return 工器具分页列表
     */
    Page<MateBorrowResDTO> pageMateBorrow(String orderCode, String mateCode, String mateName, PageReqDTO pageReqDTO);

    void exportOverhaulOrder(List<String> ids, HttpServletResponse response) throws IOException;

    List<FaultRepairDeptResDTO> queryDept(String id);

    /**
     * 获取工单派工作业人员
     * @param workStatus 工单状态
     * @param workerGroupCode 作业工班编号
     * @return 用户信息
     */
    List<OrganMajorLineType> queryWorker(String workStatus, String workerGroupCode);

    void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    /**
     * 检修工单完工
     * @param req 排查检修项信息
     */
    void finishOrder(OverhaulOrderReqDTO req);

    void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    /**
     * 检修工单完工确认
     * @param overhaulOrderReqDTO 传参
     * @throws ParseException 异常
     */
    void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException;

    void cancellWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO);

    String pageMaterial(String orderCode);

    void receiveMaterial(HttpServletResponse response) throws IOException;

    void returnMaterial(HttpServletResponse response) throws IOException;

    Page<ConstructionResDTO> construction(String orderCode, PageReqDTO pageReqDTO);

    Page<ConstructionResDTO> cancellation(String orderCode, PageReqDTO pageReqDTO);

    Page<OverhaulOrderDetailResDTO> pageOverhaulObject(String orderCode, String planCode, String planName, String objectCode, PageReqDTO pageReqDTO);

    /**
     * 获取检修对象列表-开放接口
     * @param orderCode 工单编号
     * @param pageReqDTO 分页参数
     * @return 检修对象列表
     */
    Page<OverhaulOrderDetailOpenResDTO> openPageOverhaulObject(String orderCode, PageReqDTO pageReqDTO);

    OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id);

    /**
     * 编辑检修对象
     * @param req 检修对象参数
     */
    void modifyOverhaulObject(OverhaulOrderDetailReqDTO req);

    void exportOverhaulObject(String orderCode, String planCode, String planName, String objectCode, HttpServletResponse response) throws IOException;

    void checkjx(String orderCode);

    Page<OverhaulItemResDTO> pageOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, PageReqDTO pageReqDTO);

    /**
     * 获取检修项检修模块列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修模块列表
     */
    List<OverhaulItemResDTO> listOverhaulItemModel(String objectCode, String orderCode);

    /**
     * 根据检修模块获取检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @param modelName 模块名称
     * @return 检修项列表
     */
    List<OverhaulItemResDTO> listOverhaulItem(String objectCode, String orderCode, String modelName);

    /**
     * 获取检修项检修模块与检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修项列表
     */
    List<OverhaulItemTreeResDTO> listOverhaulItemTree(String objectCode, String orderCode);

    OverhaulItemResDTO getOverhaulItemDetail(String id);

    void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, HttpServletResponse response) throws IOException;

    /**
     * 判断是否存在未填报的检修项
     * @param orderCode 工单编号
     * @param objectCode 对象编号
     * @return 是否存在未填报的检修项
     */
    Integer selectHadFinishedOverhaulOrder(String orderCode, String objectCode);

    /**
     * 排查检修项
     * @param req 排查检修项信息
     */
    void troubleshootOverhaulItem(OverhaulItemTroubleshootReqDTO req);

    Page<OverhaulStateResDTO> pageOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, PageReqDTO pageReqDTO);

    OverhaulStateResDTO getOverhaulStateDetail(String id);

    void exportOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, HttpServletResponse response) throws IOException;

    OverhaulStateOrderResDTO queryOrderInfo(String orderCode);

    /**
     * 检修异常升级故障
     * @param reqDTO 传参
     */
    void upState(FaultReportReqDTO reqDTO);
}
