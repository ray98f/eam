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

    /**
     * 判断特种设备台账是否已存在
     * @param specialEquipReqDTO 特种设备台账信息
     * @return 是否已存在
     */
    Integer selectSpecialEquipIsExist(SpecialEquipReqDTO specialEquipReqDTO);

    /**
     * 新增特种设备台账
     * @param specialEquipReqDTO 特种设备台账参数
     */
    void addSpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    List<SpecialEquipResDTO> listSpecialEquip(List<String> ids);

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(Page<SpecialEquipHistoryResDTO> page, String equipCode);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    List<SpecialEquipHistoryResDTO> listSpecialEquipHistory(String equipCode);
}
