package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.CarObjectReqDTO;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarObjResDTO;
import com.wzmtr.eam.dto.res.fault.car.CarTreeListObjResDTO;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:40
 */
public interface ObjectService {
    Page<ObjectResDTO> queryObject(ObjectReqDTO reqDTO);

    CarObjResDTO getQuery(CarObjectReqDTO reqDTO);

    List<CarTreeListObjResDTO> query(CarObjectReqDTO reqDTO);

    Page<ObjectResDTO> queryForObject(ObjectReqDTO reqDTO);
}
