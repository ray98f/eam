package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.FaultExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.SidEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:53
 */
@Mapper
@Repository
public interface FaultQueryMapper {
    Page<FaultDetailResDTO> list(Page<FaultQueryReqDTO> of, FaultQueryReqDTO req);

    List<String> queryOrderStatus(@Param("reqDTO") SidEntity reqDTO);

    List<FaultDetailResDTO> list(@Param("req") FaultQueryReqDTO req);

    List<FaultDetailResDTO> export(FaultExportReqDTO req);

    Page<ConstructionResDTO> construction(Page<Object> of, FaultQueryReqDTO req);

    Page<ConstructionResDTO> cancellation(Page<Object> of, FaultQueryReqDTO req);

    TrackQueryResDTO queryOneByFaultWorkNoAndFaultNo(String faultNo, String faultWorkNo);

    void transmit(FaultTrackDO bo);
}
