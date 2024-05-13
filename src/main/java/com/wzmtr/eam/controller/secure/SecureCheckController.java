package com.wzmtr.eam.controller.secure;

import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.secure.SecureCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 安全管理-检查问题记录
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/01
 */
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
    @PostMapping("/record/export")
    public void export(@RequestBody SecureCheckRecordListReqDTO reqDTO, HttpServletResponse response) {
        secureService.export(reqDTO, response);
    }

}
