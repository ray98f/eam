package com.wzmtr.eam.service.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.*;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:25
 */
public interface SecureHazardService {

    Page<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO);

    SecureHazardResDTO detail(SecureHazardDetailReqDTO reqDTO);

    void export(String riskId, String begin, String end, String riskRank, String restoreDesc, String workFlowInstStatus, HttpServletResponse response);

    void delete(BaseIdsEntity reqDTO);

    void add(SecureHazardAddReqDTO reqDTO);

    void update(SecureHazardAddReqDTO reqDTO);

}
