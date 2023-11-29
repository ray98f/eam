package com.wzmtr.eam.impl.common;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineFlowRoleReq;
import com.wzmtr.eam.dto.req.bpmn.BpmnExaminePersonIdReq;
import com.wzmtr.eam.dto.req.common.RoleReqDTO;
import com.wzmtr.eam.dto.req.common.UserRoleReqDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.FlowRoleResDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.service.common.RoleService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getLoginRole() {
        return roleMapper.getLoginRole(TokenUtil.getCurrentPersonId());
    }

    @Override
    public Page<Role> listRole(String roleName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return roleMapper.listRole(pageReqDTO.of(), roleName);
    }

    @Override
    public void deleteRole(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer deleteRole = roleMapper.deleteRole(id);
        Integer deleteRoleMenu = roleMapper.deleteRoleMenu(id);
        if (deleteRole < 0 || deleteRoleMenu < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void addRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtil.getCurrentPersonId());
        role.setId(TokenUtil.getUuId());
        Integer insertRole = roleMapper.insertRole(role);
        if (insertRole < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void updateRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtil.getCurrentPersonId());
        Integer updateRole = roleMapper.updateRole(role);
        if (updateRole < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public List<String> getRolePerms(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return roleMapper.getRolePerms(id);
    }

    @Override
    public void authorizeRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtil.getCurrentPersonId());
        Integer result = roleMapper.deleteRoleMenu(role.getId());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        Integer updateRole = roleMapper.insertRoleMenu(role);
        if (updateRole < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void bindUser(UserRoleReqDTO userRoleReqDTO) {
        Integer result = roleMapper.deleteUserRole(userRoleReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        Integer updateRole = roleMapper.addUserRole(userRoleReqDTO);
        if (updateRole < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public List<PersonListResDTO> listRoleUsers(String roleId, String roleCode) {
        return roleMapper.listRoleUsers(roleId, roleCode);
    }

    @Override
    public List<BpmnExaminePersonRes> listFlowUsers(String flowId, String nodeId) {
        BpmnExaminePersonIdReq req = new BpmnExaminePersonIdReq();
        req.setFlowId(flowId);
        req.setNodeId(nodeId);
        return roleMapper.getBpmnExaminePerson(req);
    }

    @Override
    public List<FlowRoleResDTO> nextFlowRole(String flowId, String nodeId) {
        if (StringUtils.isEmpty(flowId)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(nodeId)) {
            BpmnExamineFlowRoleReq req = new BpmnExamineFlowRoleReq();
            req.setFlowId(flowId);
            req.setStep("2");
            List<FlowRoleResDTO> flowRoleRes = roleMapper.queryBpmnExamine(req);
            if (CollectionUtil.isNotEmpty(flowRoleRes)) {
                flowRoleRes.forEach(a -> a.setPerson(this.listRoleUsers(null, a.getRoleId())));
            }
            return flowRoleRes;
        }
        BpmnExamineFlowRoleReq req = new BpmnExamineFlowRoleReq();
        req.setFlowId(flowId);
        req.setNodeId(nodeId);
        List<FlowRoleResDTO> flowRoleResDTO = roleMapper.queryBpmnExamine(req);
        if (CollectionUtil.isEmpty(flowRoleResDTO)) {
            return null;
        }
        String step = flowRoleResDTO.get(0).getStep();
        String nextStep = String.valueOf((Integer.parseInt(step) + 1));
        BpmnExamineFlowRoleReq bpmnExamineFlowRoleReq = new BpmnExamineFlowRoleReq();
        bpmnExamineFlowRoleReq.setStep(nextStep);
        bpmnExamineFlowRoleReq.setFlowId(flowId);
        List<FlowRoleResDTO> flowRoleRes = roleMapper.queryBpmnExamine(bpmnExamineFlowRoleReq);
        if (CollectionUtil.isNotEmpty(flowRoleRes)) {
            flowRoleRes.forEach(a -> a.setPerson(this.listRoleUsers(null, a.getRoleId())));
        }
        return flowRoleRes;
    }
}
