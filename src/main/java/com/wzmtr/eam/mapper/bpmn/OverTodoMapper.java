package com.wzmtr.eam.mapper.bpmn;

import com.wzmtr.eam.bizobject.QueryNotWorkFlowBO;
import com.wzmtr.eam.dto.res.overTodo.QueryNotWorkFlowResDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OverTodoMapper {

    List<QueryNotWorkFlowResDTO> queryNotWorkFlow(String todoId);

    void updateStatus(StatusWorkFlowLog sLog);

    void insert(StatusWorkFlowLog sLog);

    void delete(QueryNotWorkFlowBO queryNotWorkFlowBO);
}
