package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.SubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface SubmissionService {

    Page<SubmissionResDTO> pageSubmission(SubmissionListReqDTO submissionListReqDTO, PageReqDTO pageReqDTO);

    SubmissionResDTO getSubmissionDetail(String id);

    void addSubmission(SubmissionReqDTO submissionReqDTO);

    void modifySubmission(SubmissionReqDTO submissionReqDTO);

    void deleteSubmission(BaseIdsEntity baseIdsEntity);

    void submitSubmission(SubmissionReqDTO submissionReqDTO);

    void exportSubmission(SubmissionListReqDTO submissionListReqDTO, HttpServletResponse response);

}
