package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackRepairReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReportReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
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
public interface AnalyzeMapper {


    Page<AnalyzeResDTO> query(Page<Object> of, String faultNo, String majorCode, String recStatus, String lineCode, String frequency, String positionCode, String discoveryStartTime, String discoveryEndTime, String respDeptCode, String affectCodes);

    List<AnalyzeResDTO> list(String faultAnalysisNo, String faultNo, String faultWorkNo);
}
