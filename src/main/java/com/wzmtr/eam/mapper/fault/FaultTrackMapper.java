package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFlowResDTO;
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
public interface FaultTrackMapper extends BaseMapper<FaultTrackDO> {


    TrackQueryResDTO detail(FaultBaseNoReqDTO reqDTO);


    FaultInfoDO faultDetail(FaultDetailReqDTO reqDTO);

    FaultDetailResDTO faultOrderDetail(String faultNo, String faultWorkNo);

    /**
     * 根据故障编号、工单编号获取故障流程信息
     * @param faultNo 故障编号
     * @param faultWorkNo 故障工单编号
     * @return 故障流程信息
     */
    List<FaultFlowResDTO> faultFlowDetail(String faultNo, String faultWorkNo);

    void cancellGenZ(TrackQueryResDTO bo);

    Page<TrackQueryResDTO> query(Page<Object> of, TrackQueryReqDTO req);

    List<TrackQueryResDTO> export(TrackQueryReqDTO req);
    void transmit(FaultTrackDO faultTrackDO);
    String selectMaxCode();

    List<FaultTrackDO> queryList(String faultNo, String faultWorkNo, String faultAnalysisNo, String faultTrackNo);
}
