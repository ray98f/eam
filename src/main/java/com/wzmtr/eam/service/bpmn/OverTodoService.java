package com.wzmtr.eam.service.bpmn;

import java.util.List;

public interface OverTodoService {

    void overTodo(String businessRecId, String auditOpinion);

    /**
     * @param taskTitle  待办标题
     * @param businessRecId 业务表主键
     * @param businessNo 业务编号
     * @param stepUserId 待办发送人
     * @param stepName   待办步骤名
     * @param taskUrl  看着好像没啥用
     * @param lastStepUserId 最新处理人
     * @param flowId 流程Key 用于前端识别
     */

    void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId,String flowId);

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
                                 String stepName, String taskUrl, String lastStepUserId,String flowId);

    void insertTodoWithUserOrgan(String taskTitle, String businessRecId, String businessNo, String organ,
                                 String stepName, String taskUrl, String lastStepUserId, String flowId);

    void insertTodoWithUserRoleAndOrg(String taskTitle, String businessRecId, String businessNo, String roleId,
                                      String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content, String flowId);

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
                                          String taskUrl, String lastStepUserId, String majorCode, String lineCode, String orgType, String content,String flowId);

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
                                String stepName, String taskUrl, String lastStepUserId, String content,String flowId);

    /**
     * 完成该业务编号下的所有待办 更新状态为已办
     * @param bizNo
     */
    void overTodo(String bizNo);
}
