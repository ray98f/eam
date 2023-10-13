package com.wzmtr.eam.controller.secure;

import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordDeleteReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.secure.SecureCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/secure/check")
@Api(tags = "安全管理-检查问题记录")
public class SecureCheckController {

    @Autowired
    private SecureCheckService secureService;

    @ApiOperation(value = "安全/质量/消防/-检查问题记录列表")
    @PostMapping("/record/list")
    public PageResponse<SecureCheckRecordListResDTO> list(@RequestBody SecureCheckRecordListReqDTO reqDTO) {
        return PageResponse.of(secureService.list(reqDTO));
    }
    @ApiOperation(value = "安全/质量/消防/-检查问题单详情")
    @PostMapping("/detail")
    public DataResponse<SecureCheckRecordListResDTO> detail(@RequestBody SecureCheckDetailReqDTO reqDTO) {
        return DataResponse.of(secureService.detail(reqDTO));
    }
    @ApiOperation(value = "安全/质量/消防/-检查问题单新增")
    @PostMapping("/record/add")
    public DataResponse<SecureCheckRecordListResDTO> add(@RequestBody @Valid SecureCheckAddReqDTO reqDTO) {
        secureService.add(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "安全/质量/消防/-检查问题单删除")
    @PostMapping("/record/delete")
    public DataResponse<SecureCheckRecordListResDTO> delete(@RequestBody BaseIdsEntity ids) {
        secureService.delete(ids);
        return DataResponse.success();
    }

    @ApiOperation(value = "安全/质量/消防/-检查问题单编辑")
    @PostMapping("/record/update")
    public DataResponse<SecureCheckRecordListResDTO> update(@RequestBody SecureCheckAddReqDTO reqDTO) {
        secureService.update(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "安全/质量/消防/-检查问题单导出")
    @GetMapping("/record/export")
    public void export(@RequestParam(required = false) @ApiParam("检查问题单号") String secRiskId,
                       @RequestParam(required = false) @ApiParam("发现日期") String inspectDate,
                       @RequestParam(required = false) @ApiParam(value = "整改情况") String restoreDesc,
                       @RequestParam(required = false) @ApiParam(value = "流程状态") String workFlowInstStatus,
                       HttpServletResponse response) {
        secureService.export(secRiskId,inspectDate,restoreDesc,workFlowInstStatus,response);
    }

}
