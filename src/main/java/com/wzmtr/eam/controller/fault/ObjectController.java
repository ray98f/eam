package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.CarObjectReqDTO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarObjResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarTreeListObjResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.ObjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对象查询
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/16
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
    @ApiOperation(value = "车辆对象树")
    @PostMapping("/car/getQuery")
    public DataResponse<CarObjResDTO> getQuery(@RequestBody CarObjectReqDTO reqDTO) {
        return DataResponse.of(objectService.getQuery(reqDTO));
    }
    @ApiOperation(value = "车辆对象树列表")
    @PostMapping("/car/query")
    public DataResponse<List<CarTreeListObjResDTO>> query(@RequestBody CarObjectReqDTO reqDTO) {
        return DataResponse.of(objectService.query(reqDTO));
    }

    @ApiOperation(value = "对象查询分页")
    @PostMapping("/car/queryForObject")
    public PageResponse<ObjectResDTO> queryForObject(@RequestBody ObjectReqDTO reqDTO) {
        return PageResponse.of(objectService.queryForObject(reqDTO));
    }

}
