package com.wzmtr.eam.service.bpmn;

public interface OverTodoService {

    void overTodo(String businessRecId, String auditOpinion);

    void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId);

    void insertTodoWithUserGroupAndOrg(String taskTitle, String businessRecId, String businessNo, String stepUserGroup, String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content);

    /**
     *
     * @param taskTitle 标题
     * @param businessRecId recId
     * @param businessNo 编号
     * @param stepUserGroup 阶段用户权限
     * @param stepOrg 阶段组织结构
     * @param stepName 阶段名称
     * @param taskUrl url
     * @param lastStepUserId 上一步用户id
     * @param majorCode 专业编号
     * @param lineCode 线路编号
     * @param orgType 组织机构类别
     * @param content 内容
     */
    void insertTodoWithUserGroupAndAllOrg(String taskTitle, String businessRecId, String businessNo,
                                          String stepUserGroup, String stepOrg, String stepName,
                                          String taskUrl, String lastStepUserId, String majorCode, String lineCode, String orgType, String content);

    void cancelTodo(String businessRecId);
}
