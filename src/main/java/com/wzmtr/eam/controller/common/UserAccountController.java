package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.dto.res.common.UserAccountListResDTO;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysUserAccount;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/iam/user")
@Api(tags = "基础管理-用户账号管理")
@Validated
@CrossOrigin

public class UserAccountController {

    @Resource
    private UserAccountService userAccountService;

    @GetMapping("/list")
    @ApiOperation(value = "用户账户信息列表")
    public PageResponse<UserAccountListResDTO> listUserAccount(@RequestParam(required = false) @ApiParam("检索关键词") String searchKey,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(userAccountService.listUserAccount(searchKey, pageReqDTO));
    }

    @PostMapping("/ids")
    @ApiOperation(value = "根据id获取信息")
    public DataResponse<List<UserAccountListResDTO>> selectUserAccountById(@RequestBody BaseIdsEntity baseIdsEntity) {
        return DataResponse.of(userAccountService.selectUserAccountById(baseIdsEntity.getIds()));
    }

    @GetMapping("/listOut")
    @ApiOperation(value = "外部用户账户信息列表")
    public PageResponse<SysUserAccount> listOutUserAccount(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(userAccountService.listOutUserAccount(pageReqDTO));
    }

    @GetMapping("/getToken")
    @ApiOperation(value = "获取用户token")
    public DataResponse<String> getToken(@RequestParam String userId) {
        return DataResponse.of(userAccountService.getToken(userId));
    }
    @GetMapping("/getCurrentUser")
    @ApiOperation(value = "获取当前登录人信息")
    public DataResponse<CurrentLoginUser> getCurrentUser() {
        return DataResponse.of(TokenUtils.getCurrentPerson());
    }

    @GetMapping("/userInfo")
    @ApiOperation(value = "用户详情")
    public DataResponse<UserCenterInfoResDTO> getUserDetail() {
        return DataResponse.of(userAccountService.getUserDetail());
    }

    /**
     * 人员车站关联导入
     * @param file 导入文件
     * @return 成功
     */
    @PostMapping("/station/import")
    @ApiOperation(value = "人员车站关联导入")
    public DataResponse<T> importUserStation(@RequestParam MultipartFile file) {
        userAccountService.importUserStation(file);
        return DataResponse.success();
    }

}
