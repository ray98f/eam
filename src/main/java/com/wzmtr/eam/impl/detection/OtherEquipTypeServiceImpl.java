package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.detection.OtherEquipTypeReqDTO;
import com.wzmtr.eam.dto.req.detection.excel.ExcelOtherEquipTypeReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipTypeResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelOtherEquipTypeResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.detection.OtherEquipTypeMapper;
import com.wzmtr.eam.service.detection.OtherEquipTypeService;
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
import java.util.Arrays;
import java.util.List;

/**
 * 其他设备管理-其他设备分类管理
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Service
@Slf4j
public class OtherEquipTypeServiceImpl implements OtherEquipTypeService {

    @Autowired
    private OtherEquipTypeMapper otherEquipTypeMapper;

    @Override
    public Page<OtherEquipTypeResDTO> pageOtherEquipType(String typeCode, String typeName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return otherEquipTypeMapper.pageOtherEquipType(pageReqDTO.of(), typeCode, typeName);
    }

    @Override
    public OtherEquipTypeResDTO getOtherEquipTypeDetail(String id) {
        return otherEquipTypeMapper.getOtherEquipTypeDetail(id);
    }

    @Override
    public void importOtherEquipType(MultipartFile file) {
        try {
            List<OtherEquipTypeReqDTO> temp = new ArrayList<>();
            List<ExcelOtherEquipTypeReqDTO> list = EasyExcelUtils.read(file, ExcelOtherEquipTypeReqDTO.class);
            for (ExcelOtherEquipTypeReqDTO reqDTO : list) {
                OtherEquipTypeReqDTO req = new OtherEquipTypeReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setRecId(TokenUtil.getUuId());
                req.setRecCreator(TokenUtil.getCurrentPersonId());
                req.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                temp.add(req);
            }
            otherEquipTypeMapper.importOtherEquipType(temp);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void addOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO) {
        otherEquipTypeReqDTO.setRecId(TokenUtil.getUuId());
        otherEquipTypeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        otherEquipTypeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        otherEquipTypeMapper.addOtherEquipType(otherEquipTypeReqDTO);
    }

    @Override
    public void modifyOtherEquipType(OtherEquipTypeReqDTO otherEquipTypeReqDTO) {
        otherEquipTypeReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        otherEquipTypeReqDTO.setRecReviseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        otherEquipTypeMapper.modifyOtherEquipType(otherEquipTypeReqDTO);
    }

    @Override
    public void deleteOtherEquipType(List<String> ids) {
        otherEquipTypeMapper.deleteOtherEquipType(ids, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
    }

    @Override
    public void exportOtherEquipType(String typeCode, String typeName, String ids, HttpServletResponse response) throws IOException {
        List<OtherEquipTypeResDTO> typeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            typeList = otherEquipTypeMapper.exportOtherEquipType(null, null, Arrays.asList(ids.split(",")));
        } else {
            typeList = otherEquipTypeMapper.exportOtherEquipType(typeCode, typeName, null);
        }
        if (StringUtils.isNotEmpty(typeList)) {
            List<ExcelOtherEquipTypeResDTO> list = new ArrayList<>();
            for (OtherEquipTypeResDTO resDTO : typeList) {
                ExcelOtherEquipTypeResDTO res = new ExcelOtherEquipTypeResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "其他设备分类信息", list);
        }
    }

    @Override
    public List<OtherEquipTypeResDTO> listOtherEquipType(String typeCode, String typeName) {
        return otherEquipTypeMapper.listOtherEquipType(typeCode, typeName);
    }
}
