package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.OrgRegionReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgRegionResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelOrgRegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgRegionMapper;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.service.basic.OrgRegionService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 基础管理-组织机构位置
 * @author  Ray
 * @version 1.0
 * @date 2024/03/21
 */
@Service
@Slf4j
public class OrgRegionServiceImpl implements OrgRegionService {

    @Autowired
    private OrgRegionMapper orgMajorMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OrgRegionResDTO> listOrgRegion(String orgCode, String majorCode, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgMajorMapper.pageOrgRegion(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), majorCode);
    }

    @Override
    public List<OrgRegionResDTO> listUseOrgRegion(String majorCode) {
        return orgMajorMapper.listUseOrgRegion(majorCode);
    }

    @Override
    public OrgRegionResDTO getOrgRegionDetail(String id) {
        return orgMajorMapper.getOrgRegionDetail(id);
    }

    @Override
    public void addOrgRegion(OrgRegionReqDTO orgMajorReqDTO) {
        if (StringUtils.isNotEmpty(orgMajorReqDTO.getRegionCodes())) {
            for (String code : orgMajorReqDTO.getRegionCodes()) {
                Integer result = orgMajorMapper.selectOrgRegionIsExist(orgMajorReqDTO);
                if (result > 0) {
                    continue;
                }
                orgMajorReqDTO.setRegionCode(code);
                orgMajorReqDTO.setRecId(TokenUtils.getUuId());
                orgMajorReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                orgMajorReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                orgMajorMapper.addOrgRegion(orgMajorReqDTO);
            }
        }
    }

    @Override
    public void modifyOrgRegion(OrgRegionReqDTO orgMajorReqDTO) {
        Integer result = orgMajorMapper.selectOrgRegionIsExist(orgMajorReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgMajorReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        orgMajorReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        orgMajorMapper.modifyOrgRegion(orgMajorReqDTO);
    }

    @Override
    public void deleteOrgRegion(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            orgMajorMapper.deleteOrgRegion(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgRegion(List<String> ids, HttpServletResponse response) throws IOException {
        List<OrgRegionResDTO> orgMajors = orgMajorMapper.listOrgRegion(ids);
        if (orgMajors != null && !orgMajors.isEmpty()) {
            List<ExcelOrgRegionResDTO> resList = new ArrayList<>();
            for (OrgRegionResDTO resDTO : orgMajors) {
                RegionResDTO region = regionMapper.getRegionByRegionCode(resDTO.getRegionCode());
                String regionName;
                if (Objects.isNull(region)) {
                    regionName = "";
                } else {
                    regionName = region.getNodeName();
                }
                ExcelOrgRegionResDTO res = ExcelOrgRegionResDTO.builder()
                        .recId(resDTO.getRecId())
                        .orgCode(resDTO.getOrgCode())
                        .orgName(resDTO.getOrgName())
                        .regionCode(regionName)
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "组织机构位置信息", resList);
        }
    }

}
