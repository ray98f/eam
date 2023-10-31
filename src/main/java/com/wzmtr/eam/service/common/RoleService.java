package com.wzmtr.eam.service.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.common.RoleReqDTO;
import com.wzmtr.eam.dto.req.common.UserRoleReqDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;

import java.util.List;

public interface RoleService {

    Page<Role> listRole(String roleName, PageReqDTO pageReqDTO);

    void deleteRole(String id);

    void addRole(RoleReqDTO role);

    void updateRole(RoleReqDTO role);

    List<String> getRolePerms(String id);

    void authorizeRole(RoleReqDTO role);

    void bindUser(UserRoleReqDTO userRoleReqDTO);

    List<PersonListResDTO> listRoleUsers(String roleId, String roleCode);
}
