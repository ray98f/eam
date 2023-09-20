package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.TrackQueryMapper;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
public class TrackQueryServiceImpl implements TrackQueryService {
    @Autowired
    private TrackQueryMapper trackQueryMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackQueryResDTO> list = trackQueryMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultNo(),
                reqDTO.getFaultTrackWorkNo(), reqDTO.getFaultWorkNo(), reqDTO.getLineCode(), reqDTO.getMajorCode(), reqDTO.getObjectCode(),
                reqDTO.getPositionCode(), reqDTO.getSystemCode(), reqDTO.getObjectName(), reqDTO.getRecStatus(),
                reqDTO.getEquipTypeCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO) {
        FaultInfoDO faultInfo = trackQueryMapper.faultDetail(reqDTO);
        FaultDetailResDTO faultOrder = trackQueryMapper.faultOrderDetail(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (faultInfo == null) {
            return faultOrder;
        }
        FaultDetailResDTO res = __BeanUtil.copy(faultInfo, faultOrder);
        String respDeptCode = res.getRespDeptCode();
        res.setRespDeptName(organizationMapper.getOrgById(respDeptCode));
        res.setFillinDeptName(organizationMapper.getOrgById(res.getFillinDeptCode()));
        res.setRepairDeptName(organizationMapper.getExtraOrgByAreaId(res.getRepairDeptCode()));
        return res;
    }

    @Override
    public void cancellGenZ(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isNotEmpty(reqDTO.getIds())) {
            List<String> ids = reqDTO.getIds();
            ids.forEach(a -> {
                TrackQueryResDTO bo = new TrackQueryResDTO();
                bo.setFaultTrackNo(a);
                bo.setRecStatus("99");
                bo.setRecRevisor(TokenUtil.getCurrentPersonId());
                bo.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                bo.setExt1("");
                trackQueryMapper.cancellGenZ(bo);
            });
        }
        // faultTrackNo
    }

    @Override
    public TrackQueryResDTO trackDetail(SidEntity reqDTO) {
        // faultTrackNo
        return trackQueryMapper.detail(reqDTO.getId());
    }


}
