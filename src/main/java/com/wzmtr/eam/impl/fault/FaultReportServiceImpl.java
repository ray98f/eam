package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.fault.FaultReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
public class FaultReportServiceImpl implements FaultReportService {
    @Autowired
    private FaultReportMapper faultReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FaultReportReqDTO reqDTO) {
        faultReportMapper.addToFaultOrder(reqDTO);
        faultReportMapper.addToFaultInfo(reqDTO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        return faultReportMapper.list(reqDTO.of(),reqDTO.getFaultNo(),reqDTO.getObjectCode(),reqDTO.getObjectName(),reqDTO.getFaultModule(),reqDTO.getMajorCode(),reqDTO.getSystemCode(),reqDTO.getEquipTypeCode(),reqDTO.getFillinTimeStart(),reqDTO.getFillinTimeEnd());
    }

    @Override
    public Page<FaultReportResDTO> detail(FaultReportPageReqDTO reqDTO) {
        return null;
    }
}
