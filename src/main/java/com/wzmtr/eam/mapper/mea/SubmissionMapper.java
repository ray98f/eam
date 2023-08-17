package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.SubmissionResDTO;
import com.wzmtr.eam.dto.res.MeaInfoResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SubmissionMapper {

    Page<SubmissionResDTO> pageSubmission(Page<SubmissionResDTO> page, SubmissionListReqDTO req);

    SubmissionResDTO getSubmissionDetail(String id);

    String getMaxCode();

    void addSubmission(SubmissionReqDTO submissionReqDTO);

    void modifySubmission(SubmissionReqDTO submissionReqDTO);

    void deleteSubmission(String id, String userId, String time);

    void deleteSubmissionDetail(String sendVerifyNo, String userId, String time);

    List<SubmissionResDTO> listSubmission(SubmissionListReqDTO submissionListReqDTO);

    List<SubmissionDetailResDTO> listSubmissionDetail(String sendVerifyNo);
}
