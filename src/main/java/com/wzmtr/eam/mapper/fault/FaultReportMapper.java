package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
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
    void addToFaultOrder(FaultReportReqDTO reqDTO);
    void addToFaultInfo(FaultReportReqDTO reqDTO);

    Page<FaultReportResDTO> list(Page<Object> of, String faultNo, String objectCode, String objectName, String faultModuleId, String majorCode, String systemCode, String equipTypeCode, String fillinTimeStart, String fillinTimeEnd);
}
