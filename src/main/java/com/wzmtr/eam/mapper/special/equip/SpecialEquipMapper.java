package com.wzmtr.eam.mapper.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.special.equip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SpecialEquipMapper {
    
    Page<SpecialEquipResDTO> pageSpecialEquip(Page<SpecialEquipResDTO> page, String equipCode, String equipName, String specialEquipCode, String factNo,
                                              String useLineNo, String position1Code, String specialEquipType, String equipStatus);

    SpecialEquipResDTO getSpecialEquipDetail(String id);

    void updateEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void importSpecialEquip(List<SpecialEquipReqDTO> list);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    List<SpecialEquipResDTO> listSpecialEquip(List<String> ids);

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(Page<SpecialEquipHistoryResDTO> page, String equipCode);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    List<SpecialEquipHistoryResDTO> listSpecialEquiHistory(String equipCode);
}
