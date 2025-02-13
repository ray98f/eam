package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.SubmissionDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SubmissionService {

    Page<SubmissionResDTO> pageSubmission(SubmissionListReqDTO submissionListReqDTO, PageReqDTO pageReqDTO);

    SubmissionResDTO getSubmissionDetail(String id);

    void addSubmission(SubmissionReqDTO submissionReqDTO);

    void modifySubmission(SubmissionReqDTO submissionReqDTO);

    void deleteSubmission(BaseIdsEntity baseIdsEntity);

    void submitSubmission(SubmissionReqDTO submissionReqDTO) throws Exception;

    void examineSubmission(SubmissionReqDTO submissionReqDTO);

    void exportSubmission(List<String> ids, HttpServletResponse response) throws IOException;

    Page<SubmissionDetailResDTO> pageSubmissionDetail(String sendVerifyNo, PageReqDTO pageReqDTO);

    SubmissionDetailResDTO getSubmissionDetailDetail(String id);

    void addSubmissionDetail(SubmissionDetailReqDTO submissionDetailReqDTO);

    void modifySubmissionDetail(SubmissionDetailReqDTO submissionDetailReqDTO);

    void deleteSubmissionDetail(BaseIdsEntity baseIdsEntity);

    void exportSubmissionDetail(String sendVerifyNo, HttpServletResponse response) throws IOException;

}
