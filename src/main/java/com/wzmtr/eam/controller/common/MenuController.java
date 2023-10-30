package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.dto.req.common.MenuAddReqDTO;
import com.wzmtr.eam.dto.req.common.MenuModifyReqDTO;
import com.wzmtr.eam.dto.res.common.MenuDetailResDTO;
import com.wzmtr.eam.dto.res.common.MenuListResDTO;
import com.wzmtr.eam.dto.res.common.SuperMenuResDTO;
import com.wzmtr.eam.entity.BaseIdEntity;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.common.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/iam/menu")
@Api(tags = "权限管理-菜单权限管理")
@Validated
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/login")
    @ApiOperation(value = "获取登录用户的菜单列表")
    public DataResponse<List<MenuListResDTO>> listLoginMenu() {
        return DataResponse.of(menuService.listLoginMenu());
    }

    @GetMapping("/use")
    public DataResponse<List<MenuListResDTO>> listUseMenu() {
        return DataResponse.of(menuService.listUseMenu());
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取菜单列表")
    public DataResponse<List<MenuListResDTO>> listMenu() {
        return DataResponse.of(menuService.listMenu());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取菜单详情")
    public DataResponse<MenuDetailResDTO> getMenuDetail(@RequestParam @ApiParam(value = "菜单id") String id) {
        return DataResponse.of(menuService.getMenuDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增菜单")
    public DataResponse<T> addMenu(@Valid @RequestBody MenuAddReqDTO menuAddReqDTO) {
        menuService.addMenu(menuAddReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑菜单")
    public DataResponse<T> modifyMenu(@Valid @RequestBody MenuModifyReqDTO menuModifyReqDTO) {
        menuService.modifyMenu(menuModifyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除菜单")
    public DataResponse<T> deleteMenu(@RequestBody BaseIdEntity baseIdEntity) {
        menuService.deleteMenu(baseIdEntity.getId());
        return DataResponse.success();
    }

    @GetMapping("/super")
    @ApiOperation(value = "获取上级菜单列表")
    public DataResponse<List<SuperMenuResDTO>> listSuper(@Valid @NotNull(message = "32000006") @RequestParam @ApiParam(value = "权限类型 1目录、2菜单、3按钮") Integer type) {
        return DataResponse.of(menuService.listSuper(type));
    }

}
