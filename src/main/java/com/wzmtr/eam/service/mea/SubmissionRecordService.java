package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface SubmissionRecordService {

    Page<SubmissionRecordResDTO> pageSubmissionRecord(String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId, PageReqDTO pageReqDTO);

    SubmissionRecordResDTO getSubmissionRecordDetail(String id);

    void addSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void modifySubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void deleteSubmissionRecord(BaseIdsEntity baseIdsEntity);

    void submitSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) throws Exception;

    void exportSubmissionRecord(String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId, HttpServletResponse response);

    Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(String testRecId, PageReqDTO pageReqDTO);

    SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id);

    void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void deleteSubmissionRecordDetail(BaseIdsEntity baseIdsEntity);

    void exportSubmissionRecordDetail(String testRecId, HttpServletResponse response);

}
