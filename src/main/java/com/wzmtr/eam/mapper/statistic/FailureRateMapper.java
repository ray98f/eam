package com.wzmtr.eam.mapper.statistic;

import com.wzmtr.eam.dto.req.statistic.DoorFaultReqDTO;
import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface FailureRateMapper {

    /**
     * 判断指定月份站台门故障数据是否已存在
     * @param req req
     * @return 是否已存在
     */
    Integer selectDoorFaultIsExist(DoorFaultReqDTO req);
    /**
     * 新增站台门故障数据
     * @param req req
     */
    void addDoorFault(DoorFaultReqDTO req);
    /**
     * 编辑站台门故障数据
     * @param req req
     */
    void modifyDoorFault(DoorFaultReqDTO req);
    List<FailureRateResDTO>  exitingRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO> vehicleRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO>  signalRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO>  powerRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO>  PSDrate(FailreRateQueryReqDTO reqDTO);
}
