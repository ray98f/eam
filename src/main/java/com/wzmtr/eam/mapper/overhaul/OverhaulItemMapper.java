package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulItemReqDTO;
import com.wzmtr.eam.dto.req.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.OverhaulWorkRecordReqDTO;
import com.wzmtr.eam.dto.res.OverhaulItemResDTO;
import com.wzmtr.eam.dto.res.OverhaulOrderDetailResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulItemMapper {

    Page<OverhaulItemResDTO> pageOverhaulItem(Page<OverhaulOrderDetailResDTO> page, OverhaulItemListReqDTO req);

    OverhaulItemResDTO getOverhaulItemDetail(String id);

    List<OverhaulItemResDTO> listOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO);

    void insert(OverhaulItemReqDTO overhaulItemReqDTO);

}
