package com.wzmtr.eam.service.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.EquipmentCategoryReqDTO;
import com.wzmtr.eam.dto.res.EquipmentCategoryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.service.basic.EquipmentCategoryService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.tree.CompanyTreeUtils;
import com.wzmtr.eam.utils.tree.EquipmentCategoryTreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
        }
    }

    @Override
    public void exportEquipmentCategory(String name, String no, String parentId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "节点编号", "节点名称", "父节点记录编号", "记录状态", "备注", "创建者", "创建时间");
        List<EquipmentCategoryResDTO> categoryResDTOList = equipmentCategoryMapper.listEquipmentCategory(name, no, parentId);
        List<Map<String, String>> list = new ArrayList<>();
        if (categoryResDTOList != null && !categoryResDTOList.isEmpty()) {
            for (EquipmentCategoryResDTO categoryResDTO : categoryResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", categoryResDTO.getRecId());
                map.put("节点编号", categoryResDTO.getNodeCode());
                map.put("节点名称", categoryResDTO.getNodeName());
                map.put("父节点记录编号", categoryResDTO.getParentNodeRecId());
                map.put("记录状态", "10".equals(categoryResDTO.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", categoryResDTO.getRemark());
                map.put("创建者", categoryResDTO.getRecCreator());
                map.put("创建时间", categoryResDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备分类信息", listName, list, null, response);
    }

    @Override
    public List<EquipmentCategoryResDTO> getFirstEquipmentCategory() {
        return equipmentCategoryMapper.getFirstEquipmentCategory();
    }

}
