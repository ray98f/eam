package com.wzmtr.eam.controller.dict;

import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.dict.IDictionariesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dict")
@Api(tags = "字典管理-字典值管理")
@Validated
public class DictionariesController {

    @Autowired
    private IDictionariesService dictionariesService;

    @GetMapping("/page")
    @ApiOperation(value = "字典列表(分页)")
    public PageResponse<Dictionaries> page(@RequestParam(required = false) @ApiParam("名称") String itemName,
                                           @RequestParam(required = false) @ApiParam("编号") String itemCode,
                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dictionariesService.page(itemName, itemCode, pageReqDTO));
    }

    @GetMapping("/list")
    @ApiOperation(value = "根据字典类型获取字典值")
    public DataResponse<List<Dictionaries>> list(@RequestParam @ApiParam("名称") String codesetCode,
                                                 @RequestParam(required = false) @ApiParam("编码") String itemCode,
                                                 @RequestParam(required = false) @ApiParam("状态") String status) {
        return DataResponse.of(dictionariesService.list(codesetCode, itemCode, status));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "字典详情")
    public DataResponse<Dictionaries> detail(@RequestParam("itemCode") String itemCode) {
        return DataResponse.of(dictionariesService.detail(itemCode));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增-字典")
    public DataResponse<T> add(@RequestBody Dictionaries dictionaries) {
        dictionariesService.add(dictionaries);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑-字典")
    public DataResponse<T> modify(@RequestBody Dictionaries dictionaries) {
        dictionariesService.modify(dictionaries);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除-字典(单删+批量删除)")
    public DataResponse<T> delete(@RequestBody BaseIdsEntity baseIdsEntity) {
        dictionariesService.delete(baseIdsEntity.getIds());
        return DataResponse.success();
    }
}
