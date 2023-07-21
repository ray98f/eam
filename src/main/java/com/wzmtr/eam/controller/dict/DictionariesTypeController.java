package com.wzmtr.eam.controller.dict;

import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.DictionariesType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.dict.IDictionariesTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/dict/type")
@Api(tags = "字典管理-字典类型管理")
@Validated
public class DictionariesTypeController {

    @Autowired
    private IDictionariesTypeService dictionariesTypeService;

    @GetMapping("/page")
    @ApiOperation(value = "字典类型列表(分页)")
    public PageResponse<DictionariesType> page(@RequestParam(required = false) @ApiParam("名称") String name,
                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dictionariesTypeService.page(name, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "字典类型详情")
    public DataResponse<DictionariesType> detail(@RequestParam("id") Integer id) {
        return DataResponse.of(dictionariesTypeService.detail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增-字典类型")
    public DataResponse<T> add(@RequestBody DictionariesType dictionariesType) {
        dictionariesTypeService.add(dictionariesType);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑-字典类型")
    public DataResponse<T> modify(@RequestBody DictionariesType dictionariesType) {
        dictionariesTypeService.modify(dictionariesType);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除-字典类型(单删+批量删除)")
    public DataResponse<T> delete(@RequestBody BaseIdsEntity baseIdsEntity) {
        dictionariesTypeService.delete(baseIdsEntity.getIds());
        return DataResponse.success();
    }
}
