package com.wzmtr.eam.mapper.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.EquipmentChargeResDTO;
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
public interface SecureCheckMapper {
    Page<SecureCheckRecordListResDTO> query (Page<SecureCheckRecordListReqDTO> page);

    SecureCheckRecordListResDTO detail(String secRiskId);

    List<SecureCheckRecordListResDTO> list(String secRiskId, String restoreDesc,String inspectDate, String workFlowInstStatus);

    void deleteByIds(@Param("ids") List<String> ids, @Param("userId") String userId, @Param("time") String time);

    void add(SecureCheckAddReqDTO reqDTO);
}
