package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SubmissionRecordService {

    Page<SubmissionRecordResDTO> pageSubmissionRecord(String checkNo, String recStatus, PageReqDTO pageReqDTO);

    SubmissionRecordResDTO getSubmissionRecordDetail(String id);

    void addSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void modifySubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void deleteSubmissionRecord(BaseIdsEntity baseIdsEntity);

    void submitSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) throws Exception;

    void examineSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void exportSubmissionRecord(List<String> ids, HttpServletResponse response) throws IOException;

    Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(String testRecId, PageReqDTO pageReqDTO);

    SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id);

    Page<SubmissionRecordDetailResDTO> getSubmissionRecordDetailByEquip(String equipCode,PageReqDTO pageReqDTO);

    void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void deleteSubmissionRecordDetail(BaseIdsEntity baseIdsEntity);

    void exportSubmissionRecordDetail(List<String> ids, HttpServletResponse response) throws IOException;

}
