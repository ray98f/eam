package com.wzmtr.eam.controller.home;

import com.wzmtr.eam.dto.req.home.CommonFuncReqDTO;
import com.wzmtr.eam.dto.res.home.CommonFuncResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.home.CommonFuncService;
import io.swagger.annotations.Api;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 常用功能管理
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@RestController
@RequestMapping("/common/func")
@Api(tags = "常用功能管理")
public class CommonFuncController {
    @Autowired
    private CommonFuncService commonFuncService;

    /**
     * 获取用户使用的常用功能
     * @param pageReqDTO 分页信息
     * @return 常用功能
     */
    @GetMapping("/list/use")
    public PageResponse<CommonFuncResDTO> listUse(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(commonFuncService.listUse(pageReqDTO));
    }

    /**
     * 获取全部常用功能
     * @param pageReqDTO 分页信息
     * @return 常用功能
     */
    @GetMapping("/list/all")
    public PageResponse<CommonFuncResDTO> listAll(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(commonFuncService.listAll(pageReqDTO));
    }

    /**
     * 编辑用户常用功能
     * @param req 常用功能编辑传参
     * @return 成功
     */
    @PostMapping("/modify")
    public DataResponse<T> modify(@RequestBody CommonFuncReqDTO req) {
        commonFuncService.modify(req);
        return DataResponse.success();
    }
}
