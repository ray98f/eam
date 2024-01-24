package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderResDTO;
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

    OverhaulOrderResDTO getOrder(String recId, String objectFlag);

    List<OverhaulOrderResDTO> listOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    List<OverhaulOrderResDTO> getOrderByIds(List<String> list, String objectFlag);

    List<String> getSubjectByUserId(String userId);

    List<String> queryObjMiles(String planCode);

    Page<OverhaulOrderDetailResDTO> pageOverhaulObject(Page<OverhaulOrderDetailResDTO> page, String orderCode, String planCode, String planName, String objectCode);

    OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id);

    List<OverhaulOrderDetailResDTO> listOverhaulObject(String orderCode, String planCode, String planName, String objectCode);

    void updateone(String faultCode, String faultStatus, String recId);

    /**
     * 根据计划名称获取计划名称
     * @param planName 计划名称
     * @return 计划名称列表
     */
    List<String> queryPlan(String planName);
}
