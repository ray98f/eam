package com.wzmtr.eam.controller.video;

import com.wzmtr.eam.dto.req.video.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoExportReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.video.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.video.CarVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 车载视频调阅管理
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/07
 */
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

    @ApiOperation(value = "导出")
    @PostMapping("/export")
    public void export(@RequestBody CarVideoExportReqDTO reqDTO,
                       HttpServletResponse response) {
        carVideoService.export(reqDTO, response);
    }
}
