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

    /**
     * 查询待办信息
     * @param todoId 待办id
     * @param relateId 业务id
     * @return 待办信息
     */
    List<QueryNotWorkFlowResDTO> queryNotWorkFlow(String todoId, String relateId);

    void updateStatus(StatusWorkFlowLog sLog);
    void updateStatusByBizId(StatusWorkFlowLog sLog);

    void insert(StatusWorkFlowLog sLog);

    void delete(QueryNotWorkFlowBO queryNotWorkFlowBO);
}
