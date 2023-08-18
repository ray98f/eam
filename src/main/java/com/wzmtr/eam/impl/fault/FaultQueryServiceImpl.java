package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.StringUtils;
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
    public void issue(){
        // if (orderStatus.equals("40")) {
        //     /*      */
        //     /*  404 */             map.put("reportStartUserId", currentUser);
        //     /*  405 */             map.put("reportStartTime", dateTimeFormat.format(new Date()));
        //     /*  406 */           } else if (orderStatus.equals("50")) {
        //     /*      */
        //     /*  408 */             map.put("reportFinishUserId", currentUser);
        //     /*  409 */             map.put("reportFinishTime", dateTimeFormat.format(new Date()));
        //     /*  410 */             map.putAll(reportMap);
        //     /*  411 */           } else if (orderStatus.equals("60")) {
        //     /*      */
        //     /*  413 */             map.put("confirmUserId", currentUser);
        //     /*  414 */             map.put("confirmTime", dateTimeFormat.format(new Date()));
        //     /*  415 */           } else if (orderStatus.equals("55")) {
        //     /*      */
        //     /*  417 */             map.put("checkUserId", currentUser);
        //     /*  418 */             map.put("checkTime", dateTimeFormat.format(new Date()));
        //     /*      */           }
        // /*      */
    }
}
