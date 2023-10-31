package com.wzmtr.eam.mapper.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.common.RoleReqDTO;
import com.wzmtr.eam.dto.req.common.UserRoleReqDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

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
}
