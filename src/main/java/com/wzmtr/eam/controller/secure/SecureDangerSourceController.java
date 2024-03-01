package com.wzmtr.eam.controller.secure;

import com.wzmtr.eam.dto.req.secure.SecureDangerSourceAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureDangerSourceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.secure.SecureDangerSourceService;
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
 * 安全管理-危险源排查记录
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/02
 */
@RestController
@RequestMapping("/secure/danger/source")
@Api(tags = "安全管理-危险源排查记录")
public class SecureDangerSourceController {

    @Autowired
    private SecureDangerSourceService secureDangerSourceService;

    @ApiOperation(value = "危险源排查记录列表")
    @PostMapping("/list")
    public PageResponse<SecureDangerSourceResDTO> list(@RequestBody SecureDangerSourceListReqDTO reqDTO) {
        return PageResponse.of(secureDangerSourceService.dangerSourceList(reqDTO));
    }

    @ApiOperation(value = "危险源排查详情")
    @PostMapping("/detail")
    public DataResponse<SecureDangerSourceResDTO> detail(@RequestBody @Valid SecureDangerSourceDetailReqDTO reqDTO) {
        return DataResponse.of(secureDangerSourceService.detail(reqDTO));
    }

    @ApiOperation(value = "危险源排查单新增")
    @PostMapping("/add")
    public DataResponse<SecureDangerSourceResDTO> add(@RequestBody @Valid SecureDangerSourceAddReqDTO reqDTO) {
        secureDangerSourceService.add(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "危险源排查单删除")
    @PostMapping("/delete")
    public DataResponse<SecureDangerSourceResDTO> delete(@RequestBody BaseIdsEntity reqDTO) {
        secureDangerSourceService.delete(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "危险源排查单修改")
    @PostMapping("/update")
    public DataResponse<SecureDangerSourceResDTO> update(@RequestBody SecureDangerSourceAddReqDTO reqDTO) {
        secureDangerSourceService.update(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "危险源排查记录导出")
    @PostMapping("/record/export")
    public void export(@RequestBody SecureDangerSourceListReqDTO reqDTO, HttpServletResponse response) {
        secureDangerSourceService.export(reqDTO, response);
    }
}
