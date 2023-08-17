package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.ObjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:21
 */
@RestController
@RequestMapping("/object")
@Api(tags = "对象查询")
public class ObjectController {
    @Autowired
    private ObjectService objectService;

    @ApiOperation(value = "列表")
    @PostMapping("/queryObject")
    public PageResponse<ObjectResDTO> queryObject(@RequestBody ObjectReqDTO reqDTO) {
        return PageResponse.of(objectService.queryObject(reqDTO));
    }
}
