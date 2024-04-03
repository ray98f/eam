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

    void deleteRole(@Param("id") String id);

    void deleteRoleMenu(@Param("id") String id);

    void insertRole(RoleReqDTO role);

    void insertRoleMenu(RoleReqDTO role);

    void updateRole(RoleReqDTO role);

    /**
     * 修改流程相关角色信息
     * @param role 角色信息
     */
    void updateBpmnRole(RoleReqDTO role);

    List<String> getRolePerms(@Param("id") String id);

    void deleteUserRole(UserRoleReqDTO userRoleReqDTO);

    void addUserRole(UserRoleReqDTO userRoleReqDTO);

    List<PersonListResDTO> listRoleUsers(@Param("roleId") String roleId, @Param("roleCode") String roleCode);

    List<BpmnExaminePersonRes> getBpmnExaminePerson(BpmnExaminePersonIdReq req);

    /**
     * 根据专业、线别、角色获取用户列表
     * @param subjectCode 专业编码
     * @param lineCode 线别编码
     * @param roleCode 角色编码
     * @return 用户列表
     */
    List<BpmnExaminePersonRes> getUserBySubjectAndLineAndRole(@Param("subjectCode") String subjectCode,
                                                              @Param("lineCode") String lineCode,
                                                              @Param("roleCode") String roleCode);

    /**
     * 根据部门、角色获取用户列表
     * @param orgId 部门编号
     * @param roleCode 角色编号
     * @return 用户列表
     */
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
