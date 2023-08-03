package com.wzmtr.eam.service.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.*;
import com.wzmtr.eam.dto.res.secure.SecureDangerSourceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:25
 */
public interface SecureDangerSourceService {
    Page<SecureDangerSourceResDTO> dangerSourceList(SecureDangerSourceListReqDTO reqDTO);
    void export(String dangerRiskId,String discDate,HttpServletResponse response);
    SecureDangerSourceResDTO detail(SecureDangerSourceDetailReqDTO reqDTO);
    void add(SecureDangerSourceAddReqDTO reqDTO);

    void delete(BaseIdsEntity reqDTO);

    void update(SecureDangerSourceAddReqDTO reqDTO);
}
