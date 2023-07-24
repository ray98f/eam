package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.entity.HttpResult;
import com.wzmtr.eam.service.common.MdmSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mdmSync")
@Api(tags = "主数据同步管理")
@Validated
public class MdmSyncController {
    @Autowired
    private MdmSyncService mdmSyncService;

    @ApiOperation(value = "全量获取主数据人员数据")
    @GetMapping("/syncAllPerson")
    public HttpResult syncAllPerson() {
        mdmSyncService.syncAllPerson();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取外部单位人员数据")
    @GetMapping("/syncAllSuppContact")
    public HttpResult syncAllSuppContact() {
        mdmSyncService.syncAllSuppContact();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取主数据人员扩展数据")
    @GetMapping("/syncPersonPlus")
    public HttpResult syncPersonPlus() {
        mdmSyncService.syncPersonPlus();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取主数据组织机构数据")
    @GetMapping("/syncAllOrg")
    public HttpResult syncAllOrg() {
        mdmSyncService.syncAllOrg();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取主数据供应商数据")
    @GetMapping("/syncSuppOrg")
    public HttpResult syncSuppOrg() {
        mdmSyncService.syncSuppOrg();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取主数据外部组织机构数据")
    @GetMapping("/syncAllExtraOrg")
    public HttpResult syncAllExtraOrg() {
        mdmSyncService.syncAllExtraOrg();
        return HttpResult.success();
    }

    @ApiOperation(value = "全量获取主数据员工任职信息数据")
    @GetMapping("/syncAllEmpJob")
    public HttpResult syncAllEmpJob() {
        mdmSyncService.syncAllEmpJob();
        return HttpResult.success();
    }
}
