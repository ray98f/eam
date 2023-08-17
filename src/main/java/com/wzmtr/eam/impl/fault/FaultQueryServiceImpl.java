package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.mapper.fault.FaultMapper;
import com.wzmtr.eam.service.fault.FaultQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 17:02
 */
@Service
public class FaultQueryServiceImpl implements FaultQueryService {
    @Autowired
    FaultMapper faultMapper;

    @Override
    public Page<FaultDetailResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> list = faultMapper.list(reqDTO.of(), reqDTO);
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }
}
