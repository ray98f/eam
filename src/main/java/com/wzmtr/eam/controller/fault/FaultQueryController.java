package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 20:36
 */
@RestController
@RequestMapping("/fault/query")
@Api(tags = "故障管理-故障查询")
public class FaultQueryController {
    @Autowired
    private FaultQueryService faultQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<FaultDetailResDTO> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.list(reqDTO));
    }
    @ApiOperation(value = "查询订单状态")
    @PostMapping("/queryOrderStatus")
    public DataResponse<String> queryOrderStatus(@RequestBody SidEntity reqDTO) {
        return DataResponse.of(faultQueryService.queryOrderStatus(reqDTO));
    }
    // 	<select id="queryOrderStatus" parameterClass="java.util.HashMap"
    // resultClass="java.util.HashMap">
    // select
    // df2.ORDER_STATUS as "orderStatus"
    // from WBPLAT.TDMFM01 df1,WBPLAT.TDMFM02 df2
    // where 1=1 and  df1.FAULT_NO=df2.FAULT_NO
    //         <isNotEmpty prepend=" AND " property="faultWorkNo">
    // df2.FAULT_WORK_NO = #faultWorkNo#
    // 	</isNotEmpty>
    // </select>


}
