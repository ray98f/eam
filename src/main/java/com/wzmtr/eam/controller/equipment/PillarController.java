package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.PillarReqDTO;
import com.wzmtr.eam.dto.res.equipment.PillarResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.PillarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 设备管理-触网一杆一档
 * @author  Ray
 * @version 1.0
 * @date 2023/07/28
 */
@Slf4j
@RestController
@RequestMapping("/pillar")
@Api(tags = "设备管理-触网一杆一档")
@Validated
public class PillarController {

    @Resource
    private PillarService pillarService;

    @GetMapping("/page")
    @ApiOperation(value = "获取触网一杆一档列表")
    public PageResponse<PillarResDTO> listPillar(@RequestParam(required = false) @ApiParam("支柱号") String pillarNumber,
                                                 @RequestParam(required = false) @ApiParam("供电区间") String powerSupplySection,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(pillarService.pagePillar(pillarNumber, powerSupplySection, pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "获取触网一杆一档-新增")
    public DataResponse<T> listPillar(@RequestBody PillarReqDTO pillarReqDTO) {
        pillarService.add(pillarReqDTO);
        return DataResponse.success();
    }
}
