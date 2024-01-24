package com.wzmtr.eam.mapper.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:53
 */
@Mapper
@Repository
public interface FaultQueryMapper {

    Page<FaultDetailResDTO> query(Page<FaultQueryReqDTO> of, FaultQueryReqDTO req);

    FaultDetailResDTO queryDetail(@Param("req") FaultQueryDetailReqDTO req);

    List<FaultDetailResDTO> list(@Param("req") FaultQueryDetailReqDTO req);

    FaultInfoDO queryOneFaultInfo(String faultNo, String faultWorkNo);

    FaultOrderDO queryOneFaultOrder(String faultNo, String faultWorkNo);

    List<String> queryOrderStatus(@Param("reqDTO") SidEntity reqDTO);


    List<FaultDetailResDTO> export(FaultExportReqDTO req);

    List<FaultRepairDeptResDTO> queryDeptCode(String lineCode, String majorCode, String orgType);

    Page<ConstructionResDTO> construction(Page<ConstructionResDTO> of, @Param("faultWorkNo") String faultWorkNo);

    Page<ConstructionResDTO> cancellation(Page<ConstructionResDTO> of, @Param("faultWorkNo") String faultWorkNo);


}
