package com.wzmtr.eam.impl.bpmn;

import cn.hutool.core.collection.CollectionUtil;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.WorkFlowLogDO;
import com.wzmtr.eam.mapper.bpmn.WorkFlowLogMapper;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.BeanUtils;
import com.wzmtr.eam.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Li.Wang
 * Date: 2023/11/23 10:36
 */
@Service
public class WorkFlowLogServiceImpl implements IWorkFlowLogService {

    @Autowired
    private WorkFlowLogMapper workFlowLogMapper;

    @Override
    public void add(WorkFlowLogBO logBO) {
        logBO.setId(TokenUtil.getUuId());
        logBO.setCreateTime(DateUtil.getCurrentTime());
        logBO.setCreator(TokenUtil.getCurrentPersonId());
        if (CollectionUtil.isNotEmpty(logBO.getUserIds())){
            String userId = String.join(CommonConstants.COMMA, logBO.getUserIds());
            logBO.setUserId(userId);
        }
        workFlowLogMapper.insert(BeanUtils.convert(logBO, WorkFlowLogDO.class));
    }
}
