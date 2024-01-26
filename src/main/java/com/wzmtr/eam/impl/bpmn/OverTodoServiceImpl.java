package com.wzmtr.eam.impl.bpmn;

import com.wzmtr.eam.bizobject.QueryNotWorkFlowBO;
import com.wzmtr.eam.dto.req.common.EipMsgPushReq;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.overTodo.QueryNotWorkFlowResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.bpmn.OverTodoMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.utils.EipMsgPushUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OverTodoServiceImpl implements OverTodoService {

    @Autowired
    private OverTodoMapper overTodoMapper;

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public void overTodo(String businessRecId, String auditOpinion) {
        if (StringUtils.isBlank(businessRecId)) {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        try {
            List<QueryNotWorkFlowResDTO> list = overTodoMapper.queryNotWorkFlow(businessRecId);
            if (StringUtils.isNotEmpty(list)) {
                for (QueryNotWorkFlowResDTO l : list) {
                    EipMsgPushReq eipMsgPushReq = new EipMsgPushReq();
                    BeanUtils.copyProperties(l, eipMsgPushReq);
                    eipMsgPushReq.update();
                    eipMsgPushReq.setTodoId(businessRecId);
                    StatusWorkFlowLog sLog = new StatusWorkFlowLog();
                    sLog.setUserId(l.getUserId());
                    sLog.setTodoId(businessRecId);
                    sLog.setAuditOpinion(auditOpinion);
                    sLog.setTodoStatus("2");
                    sLog.setProcessUserId(TokenUtil.getCurrentPersonId());
                    sLog.setTodoDate(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    overTodoMapper.updateStatus(sLog);
                    EipMsgPushUtils.invokeTodoList(eipMsgPushReq);
                }
            }
        } catch (Exception e) {
            log.error("exception message", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR, "完成失败");
        }
    }

    @Override
    public void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId) {
        if (StringUtils.isBlank(taskTitle) || StringUtils.isBlank(businessNo) || StringUtils.isBlank(stepUserId) || StringUtils.isBlank(stepName)) {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        try {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.contextPath", "01");
            if (Objects.isNull(dictionaries)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
            String contextPath = dictionaries.getItemEname();
            String fullPath = contextPath + taskUrl + "?inqu_status-0-workFlowInstId=" + businessRecId;
            EipMsgPushReq eipMsgPushReq = new EipMsgPushReq();
            eipMsgPushReq.add();
            eipMsgPushReq.setUserId(stepUserId);
            eipMsgPushReq.setTodoId(businessRecId);
            eipMsgPushReq.setTitle(taskTitle);
            eipMsgPushReq.setEipUrl(fullPath);
            eipMsgPushReq.setPhoneUrl(fullPath);
            StatusWorkFlowLog sLog = new StatusWorkFlowLog();
            BeanUtils.copyProperties(eipMsgPushReq, sLog);
            sLog.setStepName(stepName);
            sLog.setSyscode("DM");
            sLog.setLastStepUserId(TokenUtil.getCurrentPersonId());
            sLog.setTaskRcvTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            sLog.setExt1(" ");
            overTodoMapper.insert(sLog);
            EipMsgPushUtils.invokeTodoList(eipMsgPushReq);
        } catch (Exception e) {
            log.error("exception message", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR, "新增失败");
        }
    }

    @Override
    public void insertTodoWithUserGroup(String taskTitle, String businessRecId, String businessNo, String stepUserGroup,
                                  String stepName, String taskUrl, String lastStepUserId) {
        // 根据角色获取用户列表
        List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(null, stepUserGroup);
        List<String> userIds = userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                insertTodo(taskTitle, businessRecId, businessNo, userId, stepName, taskUrl, lastStepUserId);
            }
        }
    }


    @Override
    public void insertTodoWithUserGroupAndOrg(String taskTitle, String businessRecId, String businessNo, String stepUserGroup,
                                              String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content) {
        // 根据角色和部门获取用户列表
        List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(stepOrg, stepUserGroup);
        List<String> userIds = userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        insertTodoWithUserList(userIds, taskTitle, businessRecId, businessNo, stepName, taskUrl, lastStepUserId, content);
    }

    @Override
    public void insertTodoWithUserGroupAndAllOrg(String taskTitle, String businessRecId, String businessNo,
                                                 String stepUserGroup, String stepOrg, String stepName,
                                                 String taskUrl, String lastStepUserId, String majorCode, String lineCode, String orgType, String content) {
        insertTodoWithUserGroupAndOrg(taskTitle, businessRecId, businessNo, stepUserGroup, stepOrg, stepName, taskUrl, lastStepUserId, content);
        insertTodoWithUserGroupAndOrgByParent(taskTitle, businessRecId, businessNo, stepUserGroup, stepOrg, stepName, taskUrl, lastStepUserId, content);
        List<String> userList = queryUserByChild(stepUserGroup, stepOrg, majorCode, lineCode, orgType);
        insertTodoWithUserList(userList, taskTitle, businessRecId, businessNo, stepName, taskUrl, lastStepUserId, content);
    }

    @Override
    public void cancelTodo(String businessRecId) {
        List<QueryNotWorkFlowResDTO> queryNotWorkFlowRes = overTodoMapper.queryNotWorkFlow(businessRecId);
        for (QueryNotWorkFlowResDTO l : queryNotWorkFlowRes) {
            QueryNotWorkFlowBO queryNotWorkFlowBO = new QueryNotWorkFlowBO();
            queryNotWorkFlowBO.setUserId(l.getUserId());
            queryNotWorkFlowBO.setWorkFlowInstId(businessRecId);
            queryNotWorkFlowBO.setTodoId(businessRecId);
            overTodoMapper.delete(queryNotWorkFlowBO);
        }
    }

    /**
     * 根据用户权限和组织结构父级发送消息
     * @param taskTitle 标题
     * @param businessRecId recId
     * @param businessNo 编号
     * @param stepUserGroup 阶段用户权限
     * @param stepOrg 阶段组织结构
     * @param stepName 阶段名称
     * @param taskUrl url
     * @param lastStepUserId 上一步用户id
     * @param content 内容
     */
    public void insertTodoWithUserGroupAndOrgByParent(String taskTitle, String businessRecId, String businessNo,
                                                    String stepUserGroup, String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content) {
        List<String> userList = queryUserByParent(stepUserGroup, stepOrg);
        insertTodoWithUserList(userList, taskTitle, businessRecId, businessNo, stepName, taskUrl, lastStepUserId, content);
    }

    /**
     * 查询父级组织结构人员列表
     * @param groupId 权限id
     * @param orgCode 组织结构编号
     * @return 父级组织机构人员列表
     */
    public List<String> queryUserByParent(String groupId, String orgCode) {
        String parentCode = organizationMapper.getParentCodeByCode(orgCode);
        if (StringUtils.isNotEmpty(parentCode)) {
            // 根据角色和部门获取用户列表
            List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(orgCode, groupId);
            if (StringUtils.isNotEmpty(userList)) {
                return userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            }
            return queryUserByParent(groupId, parentCode);
        }
        return null;
    }

    /**
     * 查询子级组织机构人员列表
     * @param groupId 权限id
     * @param orgCode 组织机构编号
     * @param majorCode 专业编号
     * @param lineCode 线路编号
     * @param orgType 部门类型
     * @return 人员列表
     */
    public List<String> queryUserByChild(String groupId, String orgCode, String majorCode, String lineCode, String orgType) {
        String childCode = organizationMapper.getChildCodeByCodeAndMajorLineType(orgCode, majorCode, lineCode, orgType);
        if (StringUtils.isNotEmpty(childCode)) {
            // 根据角色和部门获取用户列表
            List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(orgCode, groupId);
            if (StringUtils.isNotEmpty(userList)) {
                return userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            }
            return queryUserByChild(groupId, orgCode, majorCode, lineCode, orgType);
        }
        return null;
    }
    @Override
    public void insertTodoWithUserList(List<String> userIds, String taskTitle, String businessRecId, String businessNo,
                                       String stepName, String taskUrl, String lastStepUserId, String content) {
        if (StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(content)) {
//            // todo 发送短信
//            eiInfo.set("contacts", userIds);
//            eiInfo.set("content", content);
//            ISendMessage.sendMessageByPhoneList(eiInfo);
        }
        if (StringUtils.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                insertTodo(taskTitle, businessRecId, businessNo, userId, stepName, taskUrl, lastStepUserId);
            }
        }
    }
}
