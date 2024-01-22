package com.wzmtr.eam.service.video;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.video.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoExportReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.video.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 10:55
 */
public interface CarVideoService {

    Page<CarVideoResDTO> list(CarVideoReqDTO reqDTO);

    CarVideoResDTO detail(SidEntity reqDTO);

    void delete(BaseIdsEntity reqDTO);

    void add(CarVideoAddReqDTO reqDTO);

    void update(CarVideoAddReqDTO reqDTO);

    void operate(CarVideoOperateReqDTO reqDTO);

    void export(CarVideoExportReqDTO reqDTO,
                HttpServletResponse response);
}
