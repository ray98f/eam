package com.wzmtr.eam.mapper.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineFlowRoleReq;
import com.wzmtr.eam.dto.req.bpmn.BpmnExaminePersonIdReq;
import com.wzmtr.eam.dto.req.common.RoleReqDTO;
import com.wzmtr.eam.dto.req.common.UserRoleReqDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.FlowRoleResDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

    List<Role> getLoginRole(@Param("userId") String userId);

    Page<Role> listRole(Page<Role> page, @Param("roleName") String roleName);

    Integer deleteRole(@Param("id") String id);

    Integer deleteRoleMenu(@Param("id") String id);

    Integer insertRole(RoleReqDTO role);

    Integer insertRoleMenu(RoleReqDTO role);

    Integer updateRole(RoleReqDTO role);

    List<String> getRolePerms(@Param("id") String id);

    Integer deleteUserRole(UserRoleReqDTO userRoleReqDTO);

    Integer addUserRole(UserRoleReqDTO userRoleReqDTO);

    List<PersonListResDTO> listRoleUsers(@Param("roleId") String roleId, @Param("roleCode") String roleCode);

    List<BpmnExaminePersonRes> getBpmnExaminePerson(BpmnExaminePersonIdReq req);

    List<BpmnExaminePersonRes> getUserBySubjectAndLineAndRole(@Param("subjectCode") String subjectCode,
                                                              @Param("lineCode") String lineCode,
                                                              @Param("roleCode") String roleCode);

    List<BpmnExaminePersonRes> getUserByOrgAndRole(@Param("orgId") String orgId,
                                                   @Param("roleCode") String roleCode);

    List<FlowRoleResDTO> queryBpmnExamine(BpmnExamineFlowRoleReq req);


    List<String> getNodeIdsByFlowId(@Param("flowId") String flowId);

    /**
     * 获取流程第二步的node
     * @param flowId
     * @return
     */
    String getSubmitNodeId(@Param("flowId") String flowId,String roleId);
}
