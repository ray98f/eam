package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackRepairReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReportReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultInfo;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:53
 */
@Mapper
@Repository
public interface TrackQueryMapper {


    TrackQueryResDTO detail(String id);

    Page<TrackQueryResDTO> query(Page<Object> of, String faultTrackNo, String faultNo, String faultTrackWorkNo,
                                 String lineCode, String majorCode, String objectCode, String positionCode,
                                 String systemCode, String objectName, String recStatus, String equipTypeCode);
    FaultInfo faultDetail(String faultNo, String faultworkNo);

    FaultDetailResDTO repairDetail(String faultNo, String faultworkNo);

    void cancellGenZ(TrackQueryResDTO bo);
}
