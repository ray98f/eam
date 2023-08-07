package com.wzmtr.eam.controller.spareAndCarVideo;

import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.spareAndCarVideo.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.carVideoCall.CarVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/car/video")
@Api(tags = "车载视频调阅管理")
public class CarVideoController {

    @Autowired
    private CarVideoService carVideoService;

    @ApiOperation(value = "视频调阅列表")
    @PostMapping("/list")
    public PageResponse<CarVideoResDTO> list(@RequestBody CarVideoReqDTO reqDTO) {
        return PageResponse.of(carVideoService.list(reqDTO));
    }

    @ApiOperation(value = "视频调阅单详情")
    @PostMapping("/detail")
    public DataResponse<CarVideoResDTO> detail(@RequestBody SidEntity reqDTO) {
        return DataResponse.of(carVideoService.detail(reqDTO));
    }

    @ApiOperation(value = "视频调阅单新增")
    @PostMapping("/add")
    public DataResponse<CarVideoResDTO> add(@RequestBody CarVideoAddReqDTO reqDTO) {
        carVideoService.add(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "视频调阅单删除")
    @PostMapping("/delete")
    public DataResponse<CarVideoResDTO> delete(@RequestBody BaseIdsEntity ids) {
        carVideoService.delete(ids);
        return DataResponse.success();
    }

    @ApiOperation(value = "视频调阅单更新")
    @PostMapping("/update")
    public DataResponse<CarVideoResDTO> update(@RequestBody CarVideoAddReqDTO reqDTO) {
        carVideoService.update(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "视频调阅单操作")
    @PostMapping("/operate")
    public DataResponse<CarVideoResDTO> operate(@RequestBody CarVideoOperateReqDTO reqDTO) {
        carVideoService.operate(reqDTO);
        return DataResponse.success();
    }

    // @ApiOperation(value = "安全/质量/消防/-检查问题单导出")
    // @GetMapping("/record/export")
    // public void export(@RequestParam(required = false) @ApiParam("检查问题单号") String secRiskId,
    //                    @RequestParam(required = false) @ApiParam("发现日期") String inspectDate,
    //                    @RequestParam(required = false) @ApiParam(value = "整改情况") String restoreDesc,
    //                    @RequestParam(required = false) @ApiParam(value = "流程状态") String workFlowInstStatus,
    //                    HttpServletResponse response) {
    //     secureService.export(secRiskId, inspectDate, restoreDesc, workFlowInstStatus, response);
    // }
}
