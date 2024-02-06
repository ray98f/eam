package com.wzmtr.eam.controller.basic;

import com.wzmtr.eam.dto.req.basic.BomReqDTO;
import com.wzmtr.eam.dto.res.basic.BomResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.basic.BomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Bom结构管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/04
 */
@Slf4j
@RestController
@RequestMapping("/basic/bom")
@Api(tags = "基础管理-Bom结构管理")
@Validated
public class BomController {

    @Resource
    private BomService bomService;

    /**
     * 获取Bom结构列表
     * @param parentId 父级id
     * @param code 编码
     * @param name 名称
     * @param pageReqDTO 分页参数
     * @return Bom结构列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取Bom结构列表")
    public PageResponse<BomResDTO> listBom(@RequestParam(required = false) @ApiParam("父级id") String parentId,
                                           @RequestParam(required = false) @ApiParam("编码") String code,
                                           @RequestParam(required = false) @ApiParam("名称") String name,
                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(bomService.listBom(parentId, code, name, pageReqDTO));
    }

    /**
     * 获取Bom结构详情
     * @param id id
     * @return Bom结构详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取Bom结构详情")
    public DataResponse<BomResDTO> getBomDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(bomService.getBomDetail(id));
    }

    /**
     * 新增Bom结构
     * @param bomReqDTO bom结构数据
     * @return 成功标识
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增Bom结构")
    public DataResponse<T> addBom(@RequestBody BomReqDTO bomReqDTO) {
        bomService.addBom(bomReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改Bom结构
     * @param bomReqDTO bom结构数据
     * @return 成功标识
     */
    @PostMapping("/modify")
    @ApiOperation(value = "修改Bom结构")
    public DataResponse<T> modifyBom(@RequestBody BomReqDTO bomReqDTO) {
        bomService.modifyBom(bomReqDTO);
        return DataResponse.success();
    }

    /**
     * 删除Bom结构
     * @param baseIdsEntity ids
     * @return 成功标识
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除Bom结构")
    public DataResponse<T> deleteBom(@RequestBody BaseIdsEntity baseIdsEntity) {
        bomService.deleteBom(baseIdsEntity);
        return DataResponse.success();
    }

    /**
     * 导入Bom结构
     * @param file 文件
     * @return 成功标识
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入Bom结构")
    public DataResponse<T> importBom(@RequestParam MultipartFile file) {
        bomService.importBom(file);
        return DataResponse.success();
    }
}
