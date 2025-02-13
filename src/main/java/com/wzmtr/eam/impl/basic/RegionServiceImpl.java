package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelRegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.service.basic.RegionService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import com.wzmtr.eam.utils.tree.RegionTreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (result > CommonConstants.ZERO) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setRecId(TokenUtils.getUuId());
        regionReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        regionReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        regionMapper.addRegion(regionReqDTO);
    }

    @Override
    public void modifyRegion(RegionReqDTO regionReqDTO) {
        Integer result = regionMapper.selectRegionIsExist(regionReqDTO);
        if (result > CommonConstants.ZERO) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        regionReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        regionMapper.modifyRegion(regionReqDTO);
    }

    @Override
    public void deleteRegion(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            regionMapper.deleteRegion(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportRegion(List<String> ids, HttpServletResponse response) throws IOException {
        List<RegionResDTO> categoryResDTOList = regionMapper.listRegion(ids);
        if (categoryResDTOList != null && !categoryResDTOList.isEmpty()) {
            List<ExcelRegionResDTO> list = new ArrayList<>();
            for (RegionResDTO resDTO : categoryResDTOList) {
                LineCode lineCode = null;
                if (StringUtils.isNotEmpty(resDTO.getLineCode())) {
                    lineCode = LineCode.getByCode(resDTO.getLineCode());
                }
                ExcelRegionResDTO res = ExcelRegionResDTO.builder()
                        .recId(resDTO.getRecId())
                        .nodeCode(resDTO.getNodeCode())
                        .lineCode(lineCode == null ? resDTO.getLineCode() : lineCode.getDesc())
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
