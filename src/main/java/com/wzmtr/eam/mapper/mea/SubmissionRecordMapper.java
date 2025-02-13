package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SubmissionRecordMapper {

    Page<SubmissionRecordResDTO> pageSubmissionRecord(Page<SubmissionRecordResDTO> page, String checkNo, String recStatus);

    SubmissionRecordResDTO getSubmissionRecordDetail(String id);

    String getMaxCode();

    void addSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void modifySubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO);

    void deleteSubmissionRecord(String id, String userId, String time);

    void deleteSubmissionRecordDetail(String id, String testRecId, String userId, String time);

    List<SubmissionRecordResDTO> listSubmissionRecord(String checkNo, String recStatus);

    /**
     * 导出检修记录列表
     * @param ids ids
     * @return 检修记录列表
     */
    List<SubmissionRecordResDTO> exportSubmissionRecord(List<String> ids);

    Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(Page<SubmissionRecordDetailResDTO> page, String testRecId);

    SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id);

    Page<SubmissionRecordDetailResDTO> getSubmissionRecordDetailByEquip(Page<SubmissionRecordDetailResDTO> page,String equipCode);

    void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO);

    List<SubmissionRecordDetailResDTO> listSubmissionRecordDetail(String testRecId);

    /**
     * 导出检定记录明细
     * @param ids ids
     * @return 检定记录明细
     */
    List<SubmissionRecordDetailResDTO> exportSubmissionRecordDetail(List<String> ids);
}
