package com.wzmtr.eam.service.carVideoCall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoExportReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.spareAndCarVideo.CarVideoResDTO;
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
