package com.wzmtr.eam.service.bpmn;

import java.util.List;

public interface OverTodoService {

    void overTodo(String businessRecId, String auditOpinion);

    void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId);

    /**
     * 根据用户权限推送消息
     * @param taskTitle 标题
     * @param businessRecId recId
     * @param businessNo 编号
     * @param stepUserGroup 阶段用户权限
     * @param stepName 阶段名称
     * @param taskUrl url
     * @param lastStepUserId 上一步用户id
     */
    void insertTodoWithUserGroup(String taskTitle, String businessRecId, String businessNo, String stepUserGroup,
                                 String stepName, String taskUrl, String lastStepUserId);

    void insertTodoWithUserGroupAndOrg(String taskTitle, String businessRecId, String businessNo, String stepUserGroup,
                                       String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content);

    /**
     * 根据用户权限推和阶段组织结构推送消息
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

    /**
     * 根据用户列表发送消息（包含短信和待办推送）
     * @param userIds 用户ids
     * @param taskTitle 标题
     * @param businessRecId recId
     * @param businessNo 编号
     * @param stepName 阶段名称
     * @param taskUrl url
     * @param lastStepUserId 上一步用户id
     * @param content 内容
     */
    void insertTodoWithUserList(List<String> userIds, String taskTitle, String businessRecId, String businessNo,
                                String stepName, String taskUrl, String lastStepUserId, String content);
}
