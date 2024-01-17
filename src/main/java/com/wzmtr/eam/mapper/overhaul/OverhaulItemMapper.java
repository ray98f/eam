package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulItemTroubleshootReqDTO;
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

    /**
     * 获取检修项检修模块列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修模块列表
     */
    List<OverhaulItemResDTO> listOverhaulItemModel(String objectCode, String orderCode);

    /**
     * 根据检修模块获取检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @param modelName 模块名称
     * @return 检修项列表
     */
    List<OverhaulItemResDTO> listOverhaulItemByModel(String objectCode, String orderCode, String modelName);

    OverhaulItemResDTO getOverhaulItemDetail(String id);

    List<OverhaulItemResDTO> listOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO);

    /**
     * 排查检修项
     * @param troubleshootReqDTO 排查检修项信息
     */
    void troubleshootOverhaulItem(OverhaulItemTroubleshootReqDTO troubleshootReqDTO);

    void insert(OverhaulItemReqDTO overhaulItemReqDTO);

}
