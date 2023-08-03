package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
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
public interface SecureHazardMapper {

    void deleteByIds(@Param("ids") List<String> ids);
    Page<SecureHazardResDTO> query(Page<SecureHazardResDTO> of, String riskId, String riskRank, String inspectDateBegin, String inspectDateEnd, String restoreDesc, String recStatus);

    SecureHazardResDTO detail(String riskId);

    void add(SecureHazardAddReqDTO reqDTO);

    void update(SecureHazardAddReqDTO reqDTO);

    List<SecureHazardResDTO> list(String riskId, String begin, String end, String riskRank, String restoreDesc, String workFlowInstStatus);
}
