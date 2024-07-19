package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderFlowReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulOrderMapper {

    Page<OverhaulOrderResDTO> pageOverhaulOrder(Page<OverhaulOrderResDTO> page, OverhaulOrderListReqDTO req);

    OverhaulOrderResDTO getOverhaulOrderDetail(String id, String objectFlag);

    String getMaxCode();

    void addOverhaulOrder(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void modifyOverhaulOrder(OverhaulOrderReqDTO overhaulOrderReqDTO);

    List<OverhaulOrderResDTO> listOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    void addOverhaulOrderDetail(OverhaulOrderDetailReqDTO overhaulOrderDetailReqDTO);

    /**
     * 获取检修工单列表
     * @param page 分页参数
     * @param req 传参
     * @return 检修工单列表
     */
    Page<OverhaulOrderListResDTO> pageOrder(Page<OverhaulOrderListResDTO> page, OverhaulOrderListReqDTO req);

    /**
     * 根据工单编号和计划编号获取工单详情
     * @param orderCode 工单编号
     * @param planCode 计划编号
     * @return 工单详情
     */
    OverhaulOrderResDTO getCarOrderExt(String orderCode, String planCode);

    /**
     * 根据工单编号和计划编号获取工单规则时间和公路数
     * @param orderCode 工单编号
     * @param planCode 计划编号
     * @return 工单规则时间和公路数
     */
    OverhaulOrderResDTO getCarOrderRuleExt(String orderCode, String planCode);

    OverhaulOrderResDTO getOrder(String recId, String objectFlag);

    /**
     * 根据工单编号获取检修工单流程信息
     * @param orderCode 工单编号
     * @return 检修工单流程信息
     */
    List<OverhaulOrderFlowResDTO> orderFlowDetail(String orderCode);

    /**
     * 获取工器具分页列表
     * @param page 分页参数
     * @param orderCode 检修工单
     * @param mateCode 物资编码
     * @param mateName 物资名称
     * @return 工器具分页列表
     */
    Page<MateBorrowResDTO> pageMateBorrow(Page<MateBorrowResDTO> page, String orderCode, String mateCode, String mateName);

    List<OverhaulOrderResDTO> listOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    List<OverhaulOrderResDTO> getOrderByIds(List<String> list, String objectFlag);

    List<String> getSubjectByUserId(String userId);

    List<String> queryObjMiles(String planCode);

    Page<OverhaulOrderDetailResDTO> pageOverhaulObject(Page<OverhaulOrderDetailResDTO> page, String orderCode, String planCode, String planName, String objectCode);

    /**
     * 获取检修对象列表-开放接口
     * @param orderCode 工单编号
     * @param page 分页参数
     * @return 检修对象列表
     */
    Page<OverhaulOrderDetailOpenResDTO> openPageOverhaulObject(Page<OverhaulOrderDetailOpenResDTO> page, String orderCode);

    OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id);

    /**
     * 根据检修工单编号编辑所有检修对象
     * @param req 检修对象参数
     */
    void modifyOverhaulObjectByCode(OverhaulOrderDetailReqDTO req);

    /**
     * 根据id编辑检修对象
     * @param req 检修对象参数
     */
    void modifyOverhaulObjectById(OverhaulOrderDetailReqDTO req);

    List<OverhaulOrderDetailResDTO> listOverhaulObject(String orderCode, String planCode, String planName, String objectCode);

    void updateone(String faultCode, String faultStatus, String recId);

    /**
     * 根据计划名称获取计划名称
     * @param planName 计划名称
     * @return 计划名称列表
     */
    List<String> queryPlan(String planName);

    /**
     * 新增检修工单流程
     * @param overhaulOrderFlowReqDTO 检修工单流程信息
     */
    void addOverhaulOrderFlow(OverhaulOrderFlowReqDTO overhaulOrderFlowReqDTO);
}
