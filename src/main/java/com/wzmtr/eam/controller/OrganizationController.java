package com.wzmtr.eam.controller;

import com.wzmtr.eam.entity.*;
import com.wzmtr.eam.dto.res.MemberResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/iam/org")
@Api(tags = "组织机构管理")
@Validated
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("/listTree")
    @ApiOperation(value = "公司层级结构获取")
    public DataResponse<List<CompanyStructureTreeDTO>> listCompanyStructure() {
        return DataResponse.of(organizationService.listCompanyStructure());
    }

    @GetMapping("/companyList")
    @ApiOperation(value = "分公司列表")
    public DataResponse<List<CompanyStructureTreeDTO>> listCompanyList() {
        return DataResponse.of(organizationService.listCompanyList());
    }

    @GetMapping("/memberList")
    @ApiOperation(value = "组织成员信息")
    public PageResponse<MemberResDTO> listMember(@RequestParam @ApiParam("组织id") String id,
                                                 @RequestParam(required = false) @ApiParam("名称模糊查询") String name,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(organizationService.listMember(id, name, pageReqDTO));
    }

    @ApiOperation(value = "组织成员信息")
    @GetMapping("/membersList")
    public DataResponse<List<MemberResDTO>> listMember(@RequestParam String id) {
        return DataResponse.of(organizationService.listMembers(id));
    }
}
