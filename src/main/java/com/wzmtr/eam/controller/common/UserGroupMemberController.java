package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.common.OrganizationService;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:15
 */
@Slf4j
@RestController
@RequestMapping("/iam/org")
@Api(tags = "基础管理-用户与用户组对应信息")
@Validated
public class UserGroupMemberController {

    @Resource
    private OrganizationService organizationService;

    @Autowired
    private UserGroupMemberService userGroupMemberService;

    @GetMapping("/getWorkerGroupBySubjectAndLine")
    public DataResponse<List<OrganMajorLineType>> getWorkerGroupBySubjectAndLine(@RequestParam String equipName) {
        return DataResponse.of(organizationService.getWorkerGroupBySubjectAndLine(equipName));
    }

    @GetMapping("/getDepartmentUserByGroupName")
    public DataResponse<List<OrganMajorLineType>> getDepartmentUserByGroupName(@RequestParam String groupCode) {
        return DataResponse.of(userGroupMemberService.getDepartmentUserByGroupName(groupCode));
    }

    @GetMapping("/getZcOverhaulPlanExamineUser")
    @ApiOperation(value = "获取检修计划（中车）审核人列表")
    public DataResponse<List<BpmnExaminePersonRes>> getZcOverhaulPlanExamineUser(@RequestParam String subjectCode,
                                                                                 @RequestParam String lineCode) {
        return DataResponse.of(userGroupMemberService.getZcOverhaulPlanExamineUser(subjectCode, lineCode));
    }
}
