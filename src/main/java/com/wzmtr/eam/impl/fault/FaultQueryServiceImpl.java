package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 17:02
 */
@Service
public class FaultQueryServiceImpl implements FaultQueryService {
    @Autowired
    FaultQueryMapper faultQueryMapper;
    @Autowired
    FaultReportMapper reportMapper;

    @Override
    public Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> list = faultQueryMapper.list(reqDTO.of(), reqDTO);
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public String queryOrderStatus(SidEntity reqDTO) {
        // faultWorkNo
        String status = faultQueryMapper.queryOrderStatus(reqDTO);
        return StringUtils.isEmpty(status) ? null : status;
    }

    @Override
    public void issue(FaultDetailReqDTO reqDTO) {
        String status = faultQueryMapper.queryOrderStatus(SidEntity.builder().id(reqDTO.getFaultWorkNo()).build());
        FaultOrderBO faultOrderBO = new FaultOrderBO();
        switch (status) {
            case "40":
                faultOrderBO.setReportStartUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setReportStartTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "50":
                faultOrderBO.setReportFinishUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setReportFinishTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "60":
                faultOrderBO.setConfirmUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setConfirmTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "55":
                faultOrderBO.setCheckUserId(TokenUtil.getCurrentPersonId());
                faultOrderBO.setCheckTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
        }
        reportMapper.updateFaultOrder(faultOrderBO);
        FaultInfoBO faultInfoBO = new FaultInfoBO();
        reportMapper.updateFaultInfo(faultInfoBO);
    }

}
