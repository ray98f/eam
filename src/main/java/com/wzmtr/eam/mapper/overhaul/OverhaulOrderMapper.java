package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderDetailReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.res.OverhaulItemResDTO;
import com.wzmtr.eam.dto.res.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.dto.res.OverhaulOrderResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulOrderMapper {

    Page<OverhaulOrderResDTO> pageOverhaulOrder(Page<OverhaulOrderResDTO> page, OverhaulOrderListReqDTO req);

    OverhaulOrderResDTO getOverhaulOrderDetail(String id, String objectFlag);

    String getMaxCode();

    void addOverhaulOrder(OverhaulOrderReqDTO overhaulOrderReqDTO);

    void modifyOverhaulOrder(OverhaulOrderReqDTO overhaulOrderReqDTO);

    List<OverhaulOrderResDTO> listOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    void addOverhaulOrderDetail(OverhaulOrderDetailReqDTO overhaulOrderDetailReqDTO);

    Page<OverhaulOrderResDTO> pageOrder(Page<OverhaulOrderResDTO> page, OverhaulOrderListReqDTO req);

    OverhaulOrderResDTO getOrder(String id, String objectFlag);

    List<OverhaulOrderResDTO> listOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    List<String> getSubjectByUserId(String userId);

    List<String> queryObjMiles(String planCode);

    Page<OverhaulOrderDetailResDTO> pageOverhaulObject(Page<OverhaulOrderDetailResDTO> page, String orderCode, String planCode, String planName, String objectCode);

    OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id);

    List<OverhaulOrderDetailResDTO> listOverhaulObject(String orderCode, String planCode, String planName, String objectCode);

    void updateone(String faultCode, String faultStatus, String recId);
}
