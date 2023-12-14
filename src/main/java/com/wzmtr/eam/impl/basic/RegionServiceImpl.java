package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.RegionDO;
import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelRegionResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.service.basic.RegionService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import com.wzmtr.eam.utils.tree.RegionTreeUtils;
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
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public Page<RegionResDTO> listRegion(String name, String code, String parentId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return regionMapper.pageRegion(pageReqDTO.of(), name, code, parentId);
    }

    @Override
    public List<RegionResDTO> listRegionTree() {
        List<RegionResDTO> extraRootList = regionMapper.listRegionRootList();
        List<RegionResDTO> extraBodyList = regionMapper.listRegionBodyList();
        RegionTreeUtils extraTree = new RegionTreeUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public RegionResDTO getRegionDetail(String id) {
        return regionMapper.getRegionDetail(id);
    }

    @Override
    public void addRegion(RegionReqDTO regionReqDTO) {
        Integer result = regionMapper.selectRegionIsExist(regionReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setRecId(TokenUtil.getUuId());
        regionReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        regionReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        regionMapper.addRegion(regionReqDTO);
        // RegionDO regionDO = __BeanUtil.convert(regionReqDTO, RegionDO.class);
        // regionMapper.insert(regionDO);
    }

    @Override
    public void modifyRegion(RegionReqDTO regionReqDTO) {
        Integer result = regionMapper.selectRegionIsExist(regionReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        regionReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        regionMapper.modifyRegion(regionReqDTO);
    }

    @Override
    public void deleteRegion(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            regionMapper.deleteRegion(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportRegion(String name, String no, String parentId, HttpServletResponse response) throws IOException {
        List<RegionResDTO> categoryResDTOList = regionMapper.listRegion(name, no, parentId);
        if (categoryResDTOList != null && !categoryResDTOList.isEmpty()) {
            List<ExcelRegionResDTO> list = new ArrayList<>();
            for (RegionResDTO resDTO : categoryResDTOList) {
                ExcelRegionResDTO res = ExcelRegionResDTO.builder()
                        .recId(resDTO.getRecId())
                        .nodeCode(resDTO.getNodeCode())
                        .nodeName(resDTO.getNodeName())
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                list.add(res);
            }
            EasyExcelUtils.export(response, "位置分类信息", list);
        }
    }

}
