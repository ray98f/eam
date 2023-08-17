package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import org.apache.ibatis.annotations.Mapper;
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
    FaultInfoBO faultDetail(String faultNo, String faultworkNo);

    FaultDetailResDTO repairDetail(String faultNo, String faultworkNo);

    void cancellGenZ(TrackQueryResDTO bo);
}
