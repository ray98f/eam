package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryPartReqDTO;
import com.wzmtr.eam.dto.req.basic.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.req.basic.Excel.ExcelEquipmentCategoryPartReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryPartResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelEquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.service.basic.EquipmentCategoryService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import com.wzmtr.eam.utils.tree.EquipmentCategoryTreeUtils;
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
 * @author frp
 */
@Service
@Slf4j
public class EquipmentCategoryServiceImpl implements EquipmentCategoryService {

    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Override
    public Page<EquipmentCategoryResDTO> listEquipmentCategory(String name, String code, String parentId, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (result > CommonConstants.ZERO) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        equipmentCategoryReqDTO.setRecId(TokenUtils.getUuId());
        equipmentCategoryReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        equipmentCategoryReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentCategoryMapper.addEquipmentCategory(equipmentCategoryReqDTO);
    }

    @Override
    public void modifyEquipmentCategory(EquipmentCategoryReqDTO equipmentCategoryReqDTO) {
        Integer result = equipmentCategoryMapper.selectEquipmentCategoryIsExist(equipmentCategoryReqDTO);
        if (result > CommonConstants.ZERO) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        equipmentCategoryReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        equipmentCategoryReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        equipmentCategoryMapper.modifyEquipmentCategory(equipmentCategoryReqDTO);
    }

    @Override
    public void deleteEquipmentCategory(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            equipmentCategoryMapper.deleteEquipmentCategory(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportEquipmentCategory(List<String> ids, HttpServletResponse response) throws IOException {
        List<EquipmentCategoryResDTO> categoryResDTOList = equipmentCategoryMapper.exportEquipmentCategory(ids);
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
        String type = null;
        if (StringUtils.isNotEmpty(TokenUtils.getCurrentPerson().getNames())) {
            if (TokenUtils.getCurrentPerson().getNames().contains(CommonConstants.ZTT)) {
                type = CommonConstants.ONE_STRING;
            } else if (TokenUtils.getCurrentPerson().getNames().contains(CommonConstants.ZC)) {
                type = CommonConstants.TWO_STRING;
            }
        }
        return equipmentCategoryMapper.getFirstEquipmentCategory(type);
    }

    @Override
    public List<EquipmentCategoryResDTO> getChildEquipmentCategory(String code) {
        return equipmentCategoryMapper.getChildEquipmentCategory(code);
    }

    @Override
    public List<EquipmentCategoryPartResDTO> listEquipmentCategoryPart(String majorCode,
                                                                       String systemCode,
                                                                       String equipTypeCode) {
        return equipmentCategoryMapper.listEquipmentCategoryModule(majorCode, systemCode, equipTypeCode);
    }

    @Override
    public void importEquipmentCategoryPart(MultipartFile file) {
        List<ExcelEquipmentCategoryPartReqDTO> list = EasyExcelUtils.read(file, ExcelEquipmentCategoryPartReqDTO.class);
        List<EquipmentCategoryPartReqDTO> temp = new ArrayList<>();
        for (ExcelEquipmentCategoryPartReqDTO reqDTO : list) {
            EquipmentCategoryPartReqDTO req = new EquipmentCategoryPartReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecId(TokenUtils.getUuId());
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            temp.add(req);
        }
        if (!temp.isEmpty()) {
            equipmentCategoryMapper.importEquipmentCategoryPart(temp);
        }
    }
}
