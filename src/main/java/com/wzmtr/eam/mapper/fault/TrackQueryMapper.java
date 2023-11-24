package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:53
 */
@Mapper
@Repository
public interface TrackQueryMapper extends BaseMapper<FaultTrackDO> {


    TrackQueryResDTO detail(String id);


    FaultInfoDO faultDetail(FaultDetailReqDTO reqDTO);

    FaultDetailResDTO faultOrderDetail(String faultNo, String faultWorkNo);

    void cancellGenZ(TrackQueryResDTO bo);

    Page<TrackQueryResDTO> query(Page<Object> of, String faultTrackNo, String faultNo, String faultTrackWorkNo, String faultWorkNo, String lineCode, String majorCode, String objectCode, String positionCode, String systemCode, String objectName, String recStatus, String equipTypeCode);

    List<TrackQueryResDTO> query(TrackQueryReqDTO req);

    String selectMaxCode();
}
