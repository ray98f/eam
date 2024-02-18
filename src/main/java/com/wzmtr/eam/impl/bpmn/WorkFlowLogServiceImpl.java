package com.wzmtr.eam.impl.bpmn;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.WorkFlowLogDO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.bpmn.WorkFlowLogMapper;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.utils.BeanUtils;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        logBO.setId(TokenUtils.getUuId());
        logBO.setCreateTime(DateUtils.getCurrentTime());
        logBO.setCreator(TokenUtils.getCurrentPersonId());
        if (CollectionUtil.isNotEmpty(logBO.getUserIds())){
            String userId = String.join(CommonConstants.COMMA, logBO.getUserIds());
            logBO.setUserId(userId);
        }else {
            logBO.setUserId(CommonConstants.BLANK);
        }
        workFlowLogMapper.insert(BeanUtils.convert(logBO, WorkFlowLogDO.class));
    }

    @Override
    public void ifReviewer(String workFlowInstId) {
        QueryWrapper<WorkFlowLogDO> wrapper = new QueryWrapper<>();
        wrapper.eq("WORK_FLOW_INST_ID", workFlowInstId);
        wrapper.last("AND rownum = 1 ORDER BY CREATE_TIME DESC");
        WorkFlowLogDO res = workFlowLogMapper.selectOne(wrapper);
        if (Objects.isNull(res) || !res.getUserId().contains(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NOT_REVIEWER);
        }
    }
}
