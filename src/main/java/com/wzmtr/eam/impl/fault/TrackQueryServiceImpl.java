package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultInfo;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.fault.TrackQueryMapper;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
public class TrackQueryServiceImpl implements TrackQueryService {
    @Autowired
    private TrackQueryMapper trackQueryMapper;

    @Override
    public Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackQueryResDTO> list = trackQueryMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultNo(),
                reqDTO.getFaultTrackWorkNo(), reqDTO.getLineCode(), reqDTO.getMajorCode(), reqDTO.getObjectCode(),
                reqDTO.getPositionCode(), reqDTO.getSystemCode(), reqDTO.getObjectName(), reqDTO.getRecStatus(),
                reqDTO.getEquipTypeCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO) {
        FaultInfo faultDetail = trackQueryMapper.faultDetail(reqDTO.getFaultNo(),reqDTO.getFaultworkNo());
        FaultDetailResDTO repairDetail = trackQueryMapper.repairDetail(reqDTO.getFaultNo(),reqDTO.getFaultworkNo());
        if (faultDetail == null ) {
            return repairDetail;
        }
        return __BeanUtil.copy(faultDetail, repairDetail);
    }

    @Override
    public void cancellGenZ(SidEntity reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getId())){
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        TrackQueryResDTO bo = new TrackQueryResDTO();
        bo.setFaultTrackNo(reqDTO.getId());
        bo.setRecRevisor(TokenUtil.getCurrentPersonId());
        bo.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        bo.setExt1("");
        trackQueryMapper.cancellGenZ(bo);
    }

    @Override
    public TrackQueryResDTO trackDetail(SidEntity reqDTO) {
        return trackQueryMapper.detail(reqDTO.getId());
    }

    // @Override
    // public DataResponse<TrackQueryResDTO> detail(TrackQueryReqDTO reqDTO) {
    //     PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
    //     Page<TrackQueryResDTO> list = trackQueryMapper.query(reqDTO.of(),reqDTO);
    //     if (CollectionUtil.isEmpty(list.getRecords())) {
    //         return new Page<>();
    //     }
    //     return list;
    // }


}
