package com.wzmtr.eam.mapper.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipResDTO;
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

    void importSpecialEquip(List<SpecialEquipReqDTO> list);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    List<SpecialEquipResDTO> listSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                              String useLineNo, String position1Code, String specialEquipType, String equipStatus);

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(Page<SpecialEquipHistoryResDTO> page, String equipCode);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    List<SpecialEquipHistoryResDTO> listSpecialEquiHistory(String equipCode);
}
