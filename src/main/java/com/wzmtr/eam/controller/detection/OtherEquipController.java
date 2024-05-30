package com.wzmtr.eam.controller.detection;

import com.wzmtr.eam.dto.req.detection.OtherEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.detection.OtherEquipService;
import com.wzmtr.eam.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

/**
 * 其他设备管理-其他设备台账
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Slf4j
@RestController
@RequestMapping("/other/equip")
@Api(tags = "其他设备管理-其他设备台账")
@Validated
public class OtherEquipController {

    @Resource
    private OtherEquipService otherEquipService;

    /**
     * 获取其他设备台账列表
     * @param equipCode 设备编号
     * @param equipName 设备名称
     * @param otherEquipCode 其他设备编号
     * @param factNo 出厂编号
     * @param useLineNo 线路
     * @param position1Code 位置一
     * @param otherEquipType 其他设备类别
     * @param equipStatus 设备状态
     * @param pageReqDTO 分页信息
     * @return 其他设备台账列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取其他设备台账列表")
    public PageResponse<OtherEquipResDTO> listOtherEquip(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                         @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                         @RequestParam(required = false) @ApiParam("其他设备代码") String otherEquipCode,
                                                         @RequestParam(required = false) @ApiParam("出厂编号") String factNo,
                                                         @RequestParam(required = false) @ApiParam("线路") String useLineNo,
                                                         @RequestParam(required = false) @ApiParam("位置一") String position1Code,
                                                         @RequestParam(required = false) @ApiParam("其他设备类别") String otherEquipType,
                                                         @RequestParam(required = false) @ApiParam("设备状态") String equipStatus,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(otherEquipService.pageOtherEquip(equipCode, equipName, otherEquipCode, factNo, useLineNo,
                position1Code, otherEquipType, equipStatus, pageReqDTO));
    }

    /**
     * 获取其他设备台账详情
     * @param id id
     * @return 设备台账详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取其他设备台账详情")
    public DataResponse<OtherEquipResDTO> getOtherEquipDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(otherEquipService.getOtherEquipDetail(id));
    }

    /**
     * 编辑其他设备台账
     * @param otherEquipReqDTO 其他设备台账信息
     * @return 成功状态
     */
    @PostMapping("/modify")
    @ApiOperation(value = "编辑其他设备台账")
    public DataResponse<T> modifyOtherEquip(@RequestBody OtherEquipReqDTO otherEquipReqDTO) {
        otherEquipService.modifyOtherEquip(otherEquipReqDTO);
        return DataResponse.success();
    }

    /**
     * 导入其他设备台账
     * @param file 文件
     * @return 成功
     * @throws ParseException 异常
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入其他设备台账")
    public DataResponse<T> importOtherEquip(@RequestParam MultipartFile file) throws ParseException {
        otherEquipService.importOtherEquip(file);
        return DataResponse.success();
    }

    /**
     * 导出其他设备台账
     * @param baseIdsEntity ids
     * @param response response
     * @throws IOException 流
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出其他设备台账")
    public void exportOtherEquip(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        otherEquipService.exportOtherEquip(baseIdsEntity.getIds(), response);
    }

    /**
     * 获取其他设备检测历史记录列表
     * @param equipCode 设备编码
     * @param pageReqDTO 分页参数
     * @return 其他设备检测历史记录列表
     */
    @GetMapping("/history/list")
    @ApiOperation(value = "获取其他设备检测历史记录列表")
    public PageResponse<OtherEquipHistoryResDTO> pageOtherEquipHistory(@RequestParam @ApiParam("设备编码") String equipCode,
                                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(otherEquipService.pageOtherEquipHistory(equipCode, pageReqDTO));
    }

    /**
     * 获取其他设备检测历史记录详情
     * @param id id
     * @return 其他设备检测历史记录详情
     */
    @GetMapping("/history/detail")
    @ApiOperation(value = "获取其他设备检测历史记录详情")
    public DataResponse<OtherEquipHistoryResDTO> getOtherEquipHistoryDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(otherEquipService.getOtherEquipHistoryDetail(id));
    }

    /**
     * 导出其他设备检测历史记录详情
     * @param equipCode 设备编码
     * @param response response
     * @throws IOException 流
     */
    @GetMapping("/history/export")
    @ApiOperation(value = "导出其他设备检测历史记录详情")
    public void exportOtherEquipHistory(@RequestParam @ApiParam("设备编码") String equipCode,
                                        HttpServletResponse response) throws IOException {
        otherEquipService.exportOtherEquipHistory(equipCode, response);
    }
}
