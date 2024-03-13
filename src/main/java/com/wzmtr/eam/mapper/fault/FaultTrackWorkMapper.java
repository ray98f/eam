package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultTrackWorkDO;
import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackExportReqDTO;
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
public interface FaultTrackWorkMapper extends BaseMapper<FaultTrackWorkDO> {
    Page<TrackResDTO> query(Page<Object> of, String faultTrackNo, String faultTrackWorkNo, String recStatus, String equipTypeCode, String majorCode, String objectName, String objectCode, String systemCode);

    List<TrackResDTO> export(TrackExportReqDTO reqDTO);

    TrackResDTO detail(@Param("id") String id);

    void report(TrackReportReqDTO reqDTO);

    void close(TrackCloseReqDTO reqDTO);

    void repair(TrackRepairReqDTO reqDTO);
    String selectMaxCode();

}
