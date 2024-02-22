package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.dto.req.detection.SpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.req.detection.excel.ExcelSpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipTypeResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelSpecialEquipTypeResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.mapper.detection.SpecialEquipTypeMapper;
import com.wzmtr.eam.service.detection.SpecialEquipTypeService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 特种设备管理-特种设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@Service
@Slf4j
public class SpecialEquipTypeServiceImpl implements SpecialEquipTypeService {

    @Autowired
    private SpecialEquipTypeMapper specialEquipTypeMapper;

    @Override
    public Page<SpecialEquipTypeResDTO> pageSpecialEquipType(String typeCode, String typeName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialEquipTypeMapper.pageSpecialEquipType(pageReqDTO.of(), typeCode, typeName);
    }

    @Override
    public SpecialEquipTypeResDTO getSpecialEquipTypeDetail(String id) {
        return specialEquipTypeMapper.getSpecialEquipTypeDetail(id);
    }

    @Override
    public void importSpecialEquipType(MultipartFile file) {
        List<SpecialEquipTypeReqDTO> temp = new ArrayList<>();
        List<ExcelSpecialEquipTypeReqDTO> list = EasyExcelUtils.read(file, ExcelSpecialEquipTypeReqDTO.class);
        for (ExcelSpecialEquipTypeReqDTO reqDTO : list) {
            SpecialEquipTypeReqDTO req = new SpecialEquipTypeReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecId(TokenUtils.getUuId());
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            temp.add(req);
        }
        specialEquipTypeMapper.importSpecialEquipType(temp);
    }

    @Override
    public void addSpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeReqDTO.setRecId(TokenUtils.getUuId());
        specialEquipTypeReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        specialEquipTypeReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        specialEquipTypeMapper.addSpecialEquipType(specialEquipTypeReqDTO);
    }

    @Override
    public void modifySpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        specialEquipTypeReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        specialEquipTypeMapper.modifySpecialEquipType(specialEquipTypeReqDTO);
    }

    @Override
    public void deleteSpecialEquipType(List<String> ids) {
        specialEquipTypeMapper.deleteSpecialEquipType(ids, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
    }

    @Override
    public void exportSpecialEquipType(List<String> ids, HttpServletResponse response) throws IOException {
        List<SpecialEquipTypeResDTO> typeList = specialEquipTypeMapper.exportSpecialEquipType(ids);
        if (StringUtils.isNotEmpty(typeList)) {
            List<ExcelSpecialEquipTypeResDTO> list = new ArrayList<>();
            for (SpecialEquipTypeResDTO resDTO : typeList) {
                ExcelSpecialEquipTypeResDTO res = new ExcelSpecialEquipTypeResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备分类信息", list);
        }
    }

    @Override
    public List<SpecialEquipTypeResDTO> listSpecialEquipType(String typeCode, String typeName) {
        return specialEquipTypeMapper.listSpecialEquipType(typeCode, typeName);
    }
}
