package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.req.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.res.SubmissionRecordResDTO;
import com.wzmtr.eam.dto.res.SubmissionRecordDetailResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SubmissionRecordMapper {

    Page<SubmissionRecordResDTO> pageSubmissionRecord(Page<SubmissionRecordResDTO> page, String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId);

    SubmissionRecordResDTO getSubmissionRecordDetail(String id);

    String getMaxCode();

    void addSubmissionRecord(SubmissionRecordReqDTO checkPlanReqDTO);

    void modifySubmissionRecord(SubmissionRecordReqDTO checkPlanReqDTO);

    void deleteSubmissionRecord(String id, String userId, String time);

    void deleteSubmissionRecordDetail(String id, String checkNo, String userId, String time);

    List<SubmissionRecordResDTO> listSubmissionRecord(String recId, String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId);

    Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(Page<SubmissionRecordDetailResDTO> page, String testRecId);

    SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id);

    void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    List<SubmissionRecordDetailResDTO> listSubmissionRecordDetail(String testRecId);
}
