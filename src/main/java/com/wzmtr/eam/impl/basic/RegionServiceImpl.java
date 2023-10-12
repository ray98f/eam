package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.basic.RegionReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.service.basic.RegionService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.tree.RegionTreeUtils;
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
    public void exportRegion(String name, String no, String parentId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "节点编号", "节点名称", "记录状态", "备注", "创建者", "创建时间");
        List<RegionResDTO> categoryResDTOList = regionMapper.listRegion(name, no, parentId);
        List<Map<String, String>> list = new ArrayList<>();
        if (categoryResDTOList != null && !categoryResDTOList.isEmpty()) {
            for (RegionResDTO categoryResDTO : categoryResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", categoryResDTO.getRecId());
                map.put("节点编号", categoryResDTO.getNodeCode());
                map.put("节点名称", categoryResDTO.getNodeName());
                map.put("记录状态", "10".equals(categoryResDTO.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", categoryResDTO.getRemark());
                map.put("创建者", categoryResDTO.getRecCreator());
                map.put("创建时间", categoryResDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("位置分类信息", listName, list, null, response);
    }

}
