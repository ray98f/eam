package com.wzmtr.eam.service.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.*;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:25
 */
public interface SecureCheckService {

    Page<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO);

    SecureCheckRecordListResDTO detail(SecureCheckDetailReqDTO reqDTO);

    void export(String secRiskId, String inspectDate, String restoreDesc, String workFlowInstStatus, HttpServletResponse response);

    void delete(BaseIdsEntity reqDTO);

    void add(SecureCheckAddReqDTO reqDTO);

}
