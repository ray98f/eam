package com.wzmtr.eam.service.bpmn;

public interface OverTodoService {

    void overTodo(String businessRecId, String auditOpinion);

    void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId);

    void insertTodoWithUserGroupAndOrg(String taskTitle, String businessRecId, String businessNo, String stepUserGroup, String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content);

    void cancelTodo(String businessRecId);
}
