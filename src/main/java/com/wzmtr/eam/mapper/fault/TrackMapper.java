package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.bo.FaultTrackBO;
import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackRepairReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReportReqDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:53
 */
@Mapper
@Repository
public interface TrackMapper {
    Page<TrackResDTO> query(Page<Object> of, String faultTrackNo, String faultTrackWorkNo, String recStatus, String equipTypeCode, String majorCode, String objectName, String objectCode, String systemCode);

    TrackResDTO detail(@Param("id") String id);

    void report(TrackReportReqDTO reqDTO);

    void close(TrackCloseReqDTO reqDTO);
    TrackResDTO queryFault(@Param("id") String id);

    void repair(TrackRepairReqDTO reqDTO);

    List<FaultTrackBO> queryOne(String faultNo, String faultWorkNo, String faultAnalysisNo, String faultTrackNo);

    void update(FaultTrackBO dmfm09);
}
