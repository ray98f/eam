package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipResDTO;
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

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    List<SpecialEquipResDTO> listSpecialEquip(List<String> ids);

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(Page<SpecialEquipHistoryResDTO> page, String equipCode);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    List<SpecialEquipHistoryResDTO> listSpecialEquipHistory(String equipCode);
}
