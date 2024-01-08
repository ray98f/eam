package com.wzmtr.eam.controller.secure;

import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.secure.SecureHazardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 19:47
 */
@RestController
@RequestMapping("/secure/hazard")
@Api(tags = "安全管理-安全隐患排查记录")
public class SecureHazardController {
    @Autowired
    private SecureHazardService secureHazardService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<SecureHazardResDTO> list(@RequestBody SecureHazardReqDTO reqDTO) {
        return PageResponse.of(secureHazardService.list(reqDTO));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/detail")
    public DataResponse<SecureHazardResDTO> detail(@RequestBody @Valid SecureHazardDetailReqDTO reqDTO) {
        return DataResponse.of(secureHazardService.detail(reqDTO));
    }

    @ApiOperation(value = "安全隐患单新增")
    @PostMapping("/add")
    public DataResponse<SecureHazardResDTO> add(@RequestBody SecureHazardAddReqDTO reqDTO) {
        secureHazardService.add(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "安全隐患单update")
    @PostMapping("/update")
    public DataResponse<SecureHazardResDTO> update(@RequestBody SecureHazardAddReqDTO reqDTO) {
        secureHazardService.update(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "安全隐患单export")
    @GetMapping("/export")
    public void export(@RequestParam(required = false) @ApiParam("安全隐患排查单号") String riskId,
                                                   @RequestParam(required = false) @ApiParam("发现日期开始") String inspectDateBegin,
                                                   @RequestParam(required = false) @ApiParam("发现日期结束") String inspectDateEnd,
                                                   @RequestParam(required = false) @ApiParam("安全隐患等级") String riskRank,
                                                   @RequestParam(required = false) @ApiParam("整改情况") String restoreDesc,
                                                   @RequestParam(required = false) @ApiParam("流程状态") String workFlowInstStatus,
                                                   HttpServletResponse response) {
        secureHazardService.export(riskId, inspectDateBegin, inspectDateEnd, riskRank, restoreDesc, workFlowInstStatus, response);
    }

    @ApiOperation(value = "delete")
    @PostMapping("/delete")
    public DataResponse<SecureHazardResDTO> delete(@RequestBody BaseIdsEntity reqDTO) {
        secureHazardService.delete(reqDTO);
        return DataResponse.success();
    }



}
