package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.res.equipment.PillarResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.PillarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
}
