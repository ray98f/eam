package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulStateResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulStateMapper {

    Page<OverhaulStateResDTO> pageOverhaulState(Page<OverhaulOrderDetailResDTO> page, String objectCode, String itemName, String orderCode, String tdmer23RecId);

    OverhaulStateResDTO getOverhaulStateDetail(String id);

    List<OverhaulStateResDTO> listOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId);

}
