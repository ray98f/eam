package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.PillarResDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 11:06
 */
@Mapper
@Repository
public interface SecureMapper {
    Page<SecureCheckRecordListResDTO> query (Page<PillarResDTO> page);

    SecureCheckRecordListResDTO detail(String secRiskId);

    List<SecureCheckRecordListResDTO> list(String secRiskId, String restoreDesc, String riskRank, String inspectDate, String workFlowInstStatus);

    void deleteByIds(@Param("ids") Set<String> ids);
}
