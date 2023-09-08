package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulItemResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
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
