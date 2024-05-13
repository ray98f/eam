package com.wzmtr.eam.service.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface SpecialEquipService {
    
    Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                              String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO);

    SpecialEquipResDTO getSpecialEquipDetail(String id);

    void importSpecialEquip(MultipartFile file) throws ParseException;

    /**
     * 新增特种设备台账
     * @param specialEquipReqDTO 特种设备台账参数
     */
    void addSpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO);

    void exportSpecialEquip(List<String> ids, HttpServletResponse response) throws IOException;

    Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO);

    SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id);

    void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException;
}
