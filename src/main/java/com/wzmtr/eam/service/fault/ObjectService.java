package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.ObjectReqDTO;
import com.wzmtr.eam.dto.res.fault.ObjectResDTO;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 14:40
 */
public interface ObjectService {
    Page<ObjectResDTO> queryObject(ObjectReqDTO reqDTO);
}
