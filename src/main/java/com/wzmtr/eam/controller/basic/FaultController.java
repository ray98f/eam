package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.FaultReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.basic.FaultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/basic/fault")
@Api(tags = "基础管理-故障库")
@Validated
public class FaultController {

    @Resource
    private FaultService faultService;

    @GetMapping("/list")
    @ApiOperation(value = "获取故障库列表")
    public PageResponse<FaultResDTO> listFault(@RequestParam(required = false) @ApiParam("码值编号") String code,
                                               @RequestParam(required = false) @ApiParam("码值类型") Integer type,
                                               @RequestParam(required = false) @ApiParam("线路") String lineCode,
                                               @RequestParam(required = false) @ApiParam("对象编码") String equipmentCategoryCode,
                                               @RequestParam(required = false) @ApiParam("对象名称") String equipmentTypeName,
                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(faultService.listFault(code, type, lineCode, equipmentCategoryCode, equipmentTypeName, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取故障库详情")
    public DataResponse<FaultResDTO> getFaultDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(faultService.getFaultDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增故障库")
    public DataResponse<T> addFault(@RequestBody FaultReqDTO faultReqDTO) {
        faultService.addFault(faultReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改故障库")
    public DataResponse<T> modifyFault(@RequestBody FaultReqDTO faultReqDTO) {
        faultService.modifyFault(faultReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除故障库")
    public DataResponse<T> deleteFault(@RequestBody BaseIdsEntity baseIdsEntity) {
        faultService.deleteFault(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出故障库")
    public void exportFault(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (baseIdsEntity == null || baseIdsEntity.getIds().isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        faultService.exportFault(baseIdsEntity.getIds(), response);
    }

    /**
     * 故障查询获取码值列表
     * @param code 故障码
     * @param type 故障类型
     * @param lineCode 线路编号
     * @param equipmentCategoryCode 设备类别编号
     * @return 码值列表
     */
    @GetMapping("/query/list")
    @ApiOperation(value = "故障查询获取码值列表")
    public DataResponse<List<FaultResDTO>> listQueryFault(@RequestParam(required = false) @ApiParam("码值编号") String code,
                                                          @RequestParam(required = false) @ApiParam("码值类型") Integer type,
                                                          @RequestParam(required = false) @ApiParam("线路") String lineCode,
                                                          @RequestParam(required = false) @ApiParam("对象编码") String equipmentCategoryCode) {
        return DataResponse.of(faultService.listQueryFault(code, type, lineCode, equipmentCategoryCode));
    }
}
