package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.dto.req.common.RoleReqDTO;
import com.wzmtr.eam.dto.req.common.UserRoleReqDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.entity.BaseIdEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.common.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/iam/role")
@Api(tags = "基础管理-角色管理")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/getLoginRole")
    @ApiOperation(value = "获取登录用户的角色")
    public DataResponse<List<Role>> getLoginRole() {
        return DataResponse.of(roleService.getLoginRole());
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页获取角色信息")
    public PageResponse<Role> listRole(@RequestParam(required = false) @ApiParam("名称模糊查询") String roleName,
                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(roleService.listRole(roleName, pageReqDTO));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除角色")
    public DataResponse<T> deleteRole(@Valid @RequestBody BaseIdEntity baseIdEntity) {
        roleService.deleteRole(baseIdEntity.getId());
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增角色")
    public DataResponse<T> addRole(@Valid @RequestBody RoleReqDTO roleReqDTO) {
        roleService.addRole(roleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改角色")
    public DataResponse<T> updateRole(@Valid @RequestBody RoleReqDTO roleReqDTO) {
        roleService.updateRole(roleReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/perms")
    @ApiOperation(value = "角色权限列表")
    public DataResponse<List<String>> getRolePerms(@RequestParam @ApiParam(value = "角色id") String id) {
        return DataResponse.of(roleService.getRolePerms(id));
    }

    @PostMapping("/authorize")
    @ApiOperation(value = "角色权限分配")
    public DataResponse<T> authorizeRole(@Valid @RequestBody RoleReqDTO roleReqDTO) {
        roleService.authorizeRole(roleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/bindUser")
    @ApiOperation(value = "角色绑定人员")
    public DataResponse<T> bindUser(@RequestBody UserRoleReqDTO userRoleReqDTO) {
        roleService.bindUser(userRoleReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/listRoleUsers")
    @ApiOperation(value = "获取角色已绑定人员")
    public DataResponse<List<PersonListResDTO>> listRoleUsers(@RequestParam(required = false) @ApiParam(value = "角色id") String roleId,
                                                              @RequestParam(required = false) @ApiParam(value = "角色编码") String roleCode) {
        return DataResponse.of(roleService.listRoleUsers(roleId, roleCode));
    }

    @GetMapping("/listFlowUsers")
    @ApiOperation(value = "获取流程节点人员")
    public DataResponse<List<BpmnExaminePersonRes>> listFlowUsers(@RequestParam @ApiParam(value = "流程id") String flowId,
                                                                  @RequestParam @ApiParam(value = "节点id") String nodeId) {
        return DataResponse.of(roleService.listFlowUsers(flowId, nodeId));
    }
}