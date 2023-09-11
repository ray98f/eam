package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.bo.FaultAnalyzeBO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.res.PersonResDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:53
 */
@Mapper
@Repository
public interface AnalyzeMapper {


    Page<AnalyzeResDTO> query(Page<Object> of, String faultNo, String majorCode, String recStatus, String lineCode, String frequency, String positionCode, String discoveryStartTime, String discoveryEndTime, String respDeptCode, String affectCodes);

    List<AnalyzeResDTO> list(String faultAnalysisNo, String faultNo, String faultWorkNo);

    AnalyzeResDTO detail(FaultAnalyzeDetailReqDTO reqDTO);

    List<PersonResDTO> getOrgUsers(Set<String> userCode, String orgCode);

    List<PersonResDTO> queryCoParent(String orgCode);

    List<FaultAnalyzeBO> getFaultAnalysisList(String faultAnalysisNo, String faultNo, String faultWorkNo);

    void update(FaultAnalyzeBO dmfm03);
}
