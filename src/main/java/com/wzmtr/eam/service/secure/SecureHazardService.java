package com.wzmtr.eam.service.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:25
 */
public interface SecureHazardService {

    Page<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO);

    SecureHazardResDTO detail(SecureHazardDetailReqDTO reqDTO);

    void export(SecureHazardReqDTO reqDTO, HttpServletResponse response);

    void delete(BaseIdsEntity reqDTO);

    void add(SecureHazardAddReqDTO reqDTO);

    void update(SecureHazardAddReqDTO reqDTO);

}
