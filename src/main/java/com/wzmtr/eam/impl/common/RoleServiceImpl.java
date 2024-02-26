package com.wzmtr.eam.impl.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
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
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getLoginRole() {
        return roleMapper.getLoginRole(TokenUtils.getCurrentPersonId());
    }

    @Override
    public Page<Role> listRole(String roleName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return roleMapper.listRole(pageReqDTO.of(), roleName);
    }

    @Override
    public void deleteRole(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        roleMapper.deleteRole(id);
        roleMapper.deleteRoleMenu(id);
    }

    @Override
    public void addRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtils.getCurrentPersonId());
        role.setId(TokenUtils.getUuId());
        roleMapper.insertRole(role);
    }

    @Override
    public void updateRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        role.setCreatedBy(TokenUtils.getCurrentPersonId());
        roleMapper.updateRole(role);
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
        role.setCreatedBy(TokenUtils.getCurrentPersonId());
        roleMapper.deleteRoleMenu(role.getId());
        if (StringUtils.isNotEmpty(role.getMenuIds())) {
            roleMapper.insertRoleMenu(role);
        }
    }

    @Override
    public void bindUser(UserRoleReqDTO userRoleReqDTO) {
        roleMapper.deleteUserRole(userRoleReqDTO);
        roleMapper.addUserRole(userRoleReqDTO);
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
            req.setStep(CommonConstants.TWO_STRING);
            List<FlowRoleResDTO> flowRoleRes = roleMapper.queryBpmnExamine(req);
            if (StringUtils.isEmpty(flowRoleRes)) {
                return null;
            }
            return buildRes(toUniqueList(flowRoleRes));
        }
        BpmnExamineFlowRoleReq req = new BpmnExamineFlowRoleReq();
        req.setFlowId(flowId);
        req.setNodeId(nodeId);
        // 查当前节点的信息
        List<FlowRoleResDTO> flowRoleResDTO = roleMapper.queryBpmnExamine(req);
        if (StringUtils.isEmpty(flowRoleResDTO)) {
            return null;
        }
        FlowRoleResDTO flowRole = flowRoleResDTO.get(0);
        return buildRes((getNextNodeInfo(flowId, flowRole)));
    }

    private List<FlowRoleResDTO> toUniqueList(List<FlowRoleResDTO> flowRoleRes) {
        return flowRoleRes.stream()
                .collect(Collectors.groupingBy(FlowRoleResDTO::getRoleId))
                .values()
                .stream()
                .map(list -> list.get(0))
                .collect(Collectors.toList());
    }

    private List<FlowRoleResDTO> buildRes(List<FlowRoleResDTO> flowRoleRes) {
        if (StringUtils.isNotEmpty(flowRoleRes)) {
            for (FlowRoleResDTO res : flowRoleRes) {
                if (res.getRoleId() != null) {
                    res.setPerson(listRoleUsers(null, res.getRoleId()));
                }
            }
        }
        return flowRoleRes;
    }

    private List<FlowRoleResDTO> getNextNodeInfo(String flowId, FlowRoleResDTO flowRole) {
        // 查下一步的节点信息
        BpmnExamineFlowRoleReq bpmnExamineFlowRoleReq = new BpmnExamineFlowRoleReq();
        if (StringUtils.isNotEmpty(flowRole.getLine())){
            bpmnExamineFlowRoleReq.setLine(flowRole.getLine());
            bpmnExamineFlowRoleReq.setParentId(flowRole.getNodeId());
        }
        String step = flowRole.getStep();
        String nextStep = String.valueOf((Integer.parseInt(step) + 1));
        bpmnExamineFlowRoleReq.setStep(nextStep);
        bpmnExamineFlowRoleReq.setFlowId(flowId);
        return roleMapper.queryBpmnExamine(bpmnExamineFlowRoleReq);
    }

}
