package com.wzmtr.eam.impl.bpmn;

import com.wzmtr.eam.dto.req.EipMsgPushReq;
import com.wzmtr.eam.dto.res.overTodo.QueryNotWorkFlowResDTO;
import com.wzmtr.eam.entity.StatusWorkFlowLog;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.bpmn.OverTodoMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.utils.EipMsgPushUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OverTodoServiceImpl implements OverTodoService {

    @Autowired
    private OverTodoMapper overTodoMapper;

    @Override
    public void overTodo(String businessRecId, String auditOpinion) {
        if (StringUtils.isBlank(businessRecId)) {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        try {
            List<QueryNotWorkFlowResDTO> list = overTodoMapper.queryNotWorkFlow(businessRecId);
            if (list != null && !list.isEmpty()) {
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
            e.printStackTrace();
            throw new CommonException(ErrorCode.NORMAL_ERROR, "完成失败");
        }
    }

    @Override
    public void insertTodo(String taskTitle, String businessRecId, String businessNo, String stepUserId, String stepName, String taskUrl, String lastStepUserId) {
        if (StringUtils.isBlank(taskTitle) || StringUtils.isBlank(businessNo) || StringUtils.isBlank(stepUserId) || StringUtils.isBlank(stepName)) {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        try {
            // todo URL
//            String contextPath = CodeFactory.getCodeService().getCodeEName("dm.contextPath", "01", "1");
            String contextPath = "";
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
            overTodoMapper.insert(sLog);
            EipMsgPushUtils.invokeTodoList(eipMsgPushReq);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.NORMAL_ERROR, "新增失败");
        }
    }

    @Override
    public void insertTodoWithUserGroupAndOrg(String taskTitle, String businessRecId, String businessNo, String stepUserGroup, String stepOrg, String stepName, String taskUrl, String lastStepUserId, String content) {
        // todo 根据角色获取用户列表
//        List<Map<String, String>> userList = InterfaceHelper.getUserHelpe().getUserByGroupNameAndOrg(stepUserGroup, stepOrg);
        List<Map<String, String>> userList = new ArrayList<>();
        if (userList != null && userList.size() > 0 && content != null && !"".equals(content.trim())) {
            // todo 发送短信
//            eiInfo.set("contacts", userList);
//            eiInfo.set("content", content);
//            ISendMessage.sendMessageByPhoneList(eiInfo);
        }
        if (userList.size() != 0) {
            for (Map<String, String> user : userList) {
                insertTodo(taskTitle, businessRecId, businessNo, (String) ((Map) user).get("userCode"), stepName, taskUrl, lastStepUserId);
            }
        }
    }
}
