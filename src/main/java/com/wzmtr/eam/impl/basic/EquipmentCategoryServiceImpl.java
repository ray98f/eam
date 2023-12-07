package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.res.excel.ExcelEquipmentCategoryResDTO;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.service.basic.EquipmentCategoryService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.tree.EquipmentCategoryTreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class EquipmentCategoryServiceImpl implements EquipmentCategoryService {

    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Override
    public Page<EquipmentCategoryResDTO> listEquipmentCategory(String name, String code, String parentId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentCategoryMapper.pageEquipmentCategory(pageReqDTO.of(), name, code, parentId);
    }

    @Override
    public List<EquipmentCategoryResDTO> listEquipmentCategoryTree() {
        List<EquipmentCategoryResDTO> extraRootList = equipmentCategoryMapper.listEquipmentCategoryRootList();
        List<EquipmentCategoryResDTO> extraBodyList = equipmentCategoryMapper.listEquipmentCategoryBodyList();
        EquipmentCategoryTreeUtils extraTree = new EquipmentCategoryTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public EquipmentCategoryResDTO getEquipmentCategoryDetail(String id) {
        return equipmentCategoryMapper.getEquipmentCategoryDetail(id);
    }

    @Override
    public void addEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO) {
        Integer result = equipmentCategoryMapper.selectEquipmentCategoryIsExist(equipmentCategoryReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        equipmentCategoryReqDTO.setRecId(TokenUtil.getUuId());
        equipmentCategoryReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        equipmentCategoryReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentCategoryMapper.addEquipmentCategory(equipmentCategoryReqDTO);
    }

    @Override
    public void modifyEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO) {
        Integer result = equipmentCategoryMapper.selectEquipmentCategoryIsExist(equipmentCategoryReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        equipmentCategoryReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        equipmentCategoryReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentCategoryMapper.modifyEquipmentCategory(equipmentCategoryReqDTO);
    }

    @Override
    public void deleteEquipmentCategory(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            equipmentCategoryMapper.deleteEquipmentCategory(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentCategory(String name, String no, String parentId, HttpServletResponse response) throws IOException {
        List<EquipmentCategoryResDTO> categoryResDTOList = equipmentCategoryMapper.listEquipmentCategory(name, no, parentId);
        if (categoryResDTOList != null && !categoryResDTOList.isEmpty()) {
            List<ExcelEquipmentCategoryResDTO> resList = new ArrayList<>();
            for (EquipmentCategoryResDTO categoryResDTO : categoryResDTOList) {
                ExcelEquipmentCategoryResDTO res = ExcelEquipmentCategoryResDTO.builder()
                        .recId(categoryResDTO.getRecId())
                        .nodeCode(categoryResDTO.getNodeCode())
                        .nodeName(categoryResDTO.getNodeName())
                        .recStatus(CommonConstants.TEN_STRING.equals(categoryResDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(categoryResDTO.getRemark())
                        .recCreator(categoryResDTO.getRecCreator())
                        .recCreateTime(categoryResDTO.getRecCreateTime())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "设备分类信息", resList);
        }
    }

    @Override
    public List<EquipmentCategoryResDTO> getFirstEquipmentCategory() {
        return equipmentCategoryMapper.getFirstEquipmentCategory();
    }

    @Override
    public List<EquipmentCategoryResDTO> getChildEquipmentCategory(String code) {
        return equipmentCategoryMapper.getChildEquipmentCategory(code);
    }

}
