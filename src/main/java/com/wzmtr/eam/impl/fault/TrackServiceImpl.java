package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackRepairReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReportReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReqDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.mapper.fault.TrackMapper;
import com.wzmtr.eam.service.fault.TrackService;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
public class TrackServiceImpl implements TrackService {
    @Autowired
    private TrackMapper trackMapper;

    @Override
    public Page<TrackResDTO> list(TrackReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackResDTO> list = trackMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getObjectCode(), reqDTO.getSystemCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public TrackResDTO detail(SidEntity reqDTO) {
        return trackMapper.detail(reqDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(TrackReportReqDTO reqDTO) {
        reqDTO.setTrackReportTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackReporterId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("30");
        trackMapper.report(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(TrackCloseReqDTO reqDTO) {
        reqDTO.setTrackCloseTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackCloserId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("40");
        trackMapper.close(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repair(TrackRepairReqDTO reqDTO) {
        // /* 107 */     dmfm22.setWorkerGroupCode(repairDeptCode);
        reqDTO.setDispatchUserId(TokenUtil.getCurrentPersonId());
        reqDTO.setDispatchTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setRecStatus("20");
        trackMapper.repair(reqDTO);
    }
}
