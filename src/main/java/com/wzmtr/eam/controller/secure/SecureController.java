package com.wzmtr.eam.controller.secure;

import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordDeleteReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.secure.SecureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/secure")
@Api(tags = "安全管理")
public class SecureController {

    @Autowired
    private SecureService secureService;

    @ApiOperation(value = "安全/质量/消防/-检查问题记录列表")
    @PostMapping("/check/record/list")
    public PageResponse<SecureCheckRecordListResDTO> list(@RequestBody SecureCheckRecordListReqDTO reqDTO) {
        return PageResponse.of(secureService.list(reqDTO));
    }
    @ApiOperation(value = "安全/质量/消防/-检查问题单详情")
    @PostMapping("/check/detail")
    public DataResponse<SecureCheckRecordListResDTO> detail(@RequestBody SecureCheckDetailReqDTO reqDTO) {
        return DataResponse.of(secureService.detail(reqDTO));
    }
    // todo
    // @ApiOperation(value = "安全/质量/消防/-检查问题单新增")
    // @PostMapping("/check/record/add")
    // public DataResponse<SecureCheckRecordListResDTO> add(@RequestBody SecureCheckDetailReqDTO reqDTO) {
    //     return DataResponse.of(secureService.add(reqDTO));
    // }
    @ApiOperation(value = "安全/质量/消防/-检查问题单删除")
    @PostMapping("/check/record/add")
    public DataResponse<SecureCheckRecordListResDTO> delete(@RequestBody SecureCheckRecordDeleteReqDTO reqDTO) {
        secureService.delete(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "安全/质量/消防/-检查问题单导出")
    @GetMapping("/check/record/add")
    public void export(@RequestParam(required = false) @ApiParam("安全隐患单号") String secRiskId,
                       @RequestParam(required = false) @ApiParam("发现日期") String inspectDate,
                       @RequestParam(required = false) @ApiParam(value = "整改情况") String restoreDesc,
                       @RequestParam(required = false) @ApiParam(value = "流程状态") String workFlowInstStatus,
                       @RequestParam(required = false) @ApiParam(value = "安全隐患等级") String riskRank,
                       HttpServletResponse response) {
        secureService.export(secRiskId,inspectDate,restoreDesc,workFlowInstStatus,riskRank,response);
    }

}
