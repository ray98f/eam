package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultAnalyzeDO;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
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
public interface FaultAnalyzeMapper extends BaseMapper<FaultAnalyzeDO> {


    Page<AnalyzeResDTO> query(Page<Object> of, AnalyzeReqDTO req);

    List<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO);

    AnalyzeResDTO detail(FaultAnalyzeDetailReqDTO reqDTO);

    List<PersonResDTO> getOrgUsers(Set<String> userCode, String orgCode);

    List<PersonResDTO> queryCoParent(String orgCode);

    List<FaultAnalyzeDO> getFaultAnalysisList(String faultAnalysisNo, String faultNo, String faultWorkNo);

    void update(FaultAnalyzeDO dmfm03);
}
