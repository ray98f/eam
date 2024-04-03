package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultCancelReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultErrorReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFlowReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultOrderResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 20:00
 */
@Mapper
@Repository
public interface FaultReportMapper {
    /**
     * queryNoCar 已提报故障不包含车辆专业
     */
    Page<FaultReportResDTO> list(Page<Object> of, String faultNo, String objectCode, String objectName,
                                 String faultModuleId, String majorCode, String systemCode, String equipTypeCode,
                                 String fillinTimeStart, String fillinTimeEnd, String positionCode, String orderStatus,String faultWorkNo,String lineCode,List<String> majors);

    Page<FaultReportResDTO> openApiList(Page<Object> of, @Param("reqDTO") FaultReportPageReqDTO reqDTO);

    void addToFaultInfo(FaultInfoDO faultInfo);

    void addToFaultOrder(FaultOrderDO faultOrder);

    void updateToFaultInfo(FaultInfoDO faultInfo);

    /**
     * update col by faultNo and faultWorkNo
     *
     * @param faultOrder
     */
    void updateFaultOrder(FaultOrderDO faultOrder);

    /**
     * 更新faultInfo表，忽略null值
     *
     * @param faultInfo
     */
    void updateFaultInfo(FaultInfoDO faultInfo);

    String getFaultInfoFaultNoMaxCode();

    String getFaultOrderFaultWorkNoMaxCode();

    void cancelOrder(FaultCancelReqDTO reqDTO);

    void cancelInfo(FaultCancelReqDTO reqDTO);

    Page<FaultReportResDTO> carFaultReportList(Page<Object> of, String faultNo, String objectCode, String objectName,
                                               String faultModuleId, String majorCode, String systemCode,
                                               String equipTypeCode, String fillinTimeStart, String fillinTimeEnd,
                                               String positionCode, String orderStatus,String faultAffect,
                                               List<String> majors);

    /**
     * 根据故障编号和工单编号查询工单列表
     * @param faultNo 故障编号
     * @param faultWorkNo 工单编号
     * @return 工单列表
     */
    List<FaultOrderResDTO> listOrderByNoAndWorkNo(String faultNo, String faultWorkNo);

    /**
     * 新增故障流程
     * @param faultFlowReqDTO 故障流程数据
     */
    void addFaultFlow(FaultFlowReqDTO faultFlowReqDTO);

    /**
     * 新增故障错误
     * @param faultError 故障错误信息
     */
    void addFaultError(FaultErrorReqDTO faultError);
}
