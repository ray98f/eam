package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultCancelReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 20:00
 */
@Mapper
@Repository
public interface FaultReportMapper {

    Page<FaultReportResDTO> list(Page<Object> of, String faultNo, String objectCode, String objectName, String faultModuleId, String majorCode, String systemCode, String equipTypeCode, String fillinTimeStart, String fillinTimeEnd,String positionCode);

    void addToFaultInfo(FaultInfoDO faultInfo);

    void addToFaultOrder(FaultOrderDO faultOrder);

    /**
     * update col by faultNo and faultWorkNo
     * @param faultOrder
     */
    void updateFaultOrder(FaultOrderDO faultOrder);

    void updateFaultInfo(FaultInfoDO faultInfo);

    String getFaultInfoFaultNoMaxCode();

    String getFaultOrderFaultWorkNoMaxCode();

    void cancelOrder(FaultCancelReqDTO reqDTO);

    void cancelInfo(FaultCancelReqDTO reqDTO);
}
