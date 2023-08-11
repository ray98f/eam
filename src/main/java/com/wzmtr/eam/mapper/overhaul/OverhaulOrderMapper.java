package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulOrderDetailReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
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

    Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    OverhaulOrderResDTO getOverhaulOrderDetail(String id, String objectFlag);

    String getMaxCode();

    void addOverhaulOrder(OverhaulOrderReqDTO overhaulOrderReqDTO);

    List<OverhaulOrderResDTO> listOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO);

    void addOverhaulOrderDetail(OverhaulOrderDetailReqDTO overhaulOrderDetailReqDTO);

}
