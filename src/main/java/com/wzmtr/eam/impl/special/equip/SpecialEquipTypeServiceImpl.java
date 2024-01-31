package com.wzmtr.eam.impl.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.special.equip.SpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.req.special.equip.excel.ExcelSpecialEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.special.equip.SpecialEquipTypeResDTO;
import com.wzmtr.eam.dto.res.special.equip.excel.ExcelSpecialEquipTypeResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.special.equip.SpecialEquipTypeMapper;
import com.wzmtr.eam.service.special.equip.SpecialEquipTypeService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialEquipTypeMapper.pageSpecialEquipType(pageReqDTO.of(), typeCode, typeName);
    }

    @Override
    public SpecialEquipTypeResDTO getSpecialEquipTypeDetail(String id) {
        return specialEquipTypeMapper.getSpecialEquipTypeDetail(id);
    }

    @Override
    public void importSpecialEquipType(MultipartFile file) {
        try {
            List<SpecialEquipTypeReqDTO> temp = new ArrayList<>();
            List<ExcelSpecialEquipTypeReqDTO> list = EasyExcelUtils.read(file, ExcelSpecialEquipTypeReqDTO.class);
            for (ExcelSpecialEquipTypeReqDTO reqDTO : list) {
                SpecialEquipTypeReqDTO req = new SpecialEquipTypeReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setRecId(TokenUtil.getUuId());
                req.setRecCreator(TokenUtil.getCurrentPersonId());
                req.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                temp.add(req);
            }
            specialEquipTypeMapper.importSpecialEquipType(temp);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void addSpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeReqDTO.setRecId(TokenUtil.getUuId());
        specialEquipTypeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        specialEquipTypeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        specialEquipTypeMapper.addSpecialEquipType(specialEquipTypeReqDTO);
    }

    @Override
    public void modifySpecialEquipType(SpecialEquipTypeReqDTO specialEquipTypeReqDTO) {
        specialEquipTypeReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        specialEquipTypeReqDTO.setRecReviseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        specialEquipTypeMapper.modifySpecialEquipType(specialEquipTypeReqDTO);
    }

    @Override
    public void deleteSpecialEquipType(List<String> ids) {
        specialEquipTypeMapper.deleteSpecialEquipType(ids, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
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
