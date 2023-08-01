package com.wzmtr.eam.service.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordDeleteReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:25
 */
public interface SecureService {

    Page<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO);

    SecureCheckRecordListResDTO detail(SecureCheckDetailReqDTO reqDTO);

    void export(String secRiskId, String inspectDate, String restoreDesc, String workFlowInstStatus, String riskRank, HttpServletResponse response);

    void delete(SecureCheckRecordDeleteReqDTO reqDTO);
}
