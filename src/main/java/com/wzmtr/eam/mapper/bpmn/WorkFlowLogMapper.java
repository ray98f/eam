package com.wzmtr.eam.mapper.bpmn;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzmtr.eam.dataobject.WorkFlowLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WorkFlowLogMapper extends BaseMapper<WorkFlowLogDO> {
}
