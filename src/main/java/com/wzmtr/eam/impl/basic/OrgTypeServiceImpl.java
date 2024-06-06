package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgTypeResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelOrgTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgTypeMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.service.basic.OrgTypeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Service
@Slf4j
public class OrgTypeServiceImpl implements OrgTypeService {

    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("10", "点巡检/检修专用");
        TYPE_MAP.put("20", "故障维修专用");
        TYPE_MAP.put("30", "管理部门");
    }

    @Autowired
    private OrgTypeMapper orgTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OrgTypeResDTO> listOrgType(String orgCode, String orgType, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgTypeMapper.pageOrgType(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), orgType);
    }

    @Override
    public OrgTypeResDTO getOrgTypeDetail(String id) {
        return orgTypeMapper.getOrgTypeDetail(id);
    }

    @Override
    public void addOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        String orgId = organizationMapper.getIdByAreaId(orgTypeReqDTO.getOrgCode());
        if (StringUtils.isNotEmpty(orgId)) {
            List<String> ids = organizationMapper.downRecursionId(orgId);
            if (StringUtils.isNotEmpty(ids)) {
                List<SysOffice> offices = organizationMapper.getAreaIdsByIds(ids);
                orgTypeReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                orgTypeReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                for (SysOffice office : offices) {
                    orgTypeReqDTO.setOrgCode(office.getAreaId());
                    orgTypeReqDTO.setOrgName(office.getName());
                    Integer result = orgTypeMapper.selectOrgTypeIsExist(orgTypeReqDTO);
                    if (result > CommonConstants.ONE) {
                        continue;
                    }
                    orgTypeReqDTO.setRecId(TokenUtils.getUuId());
                    orgTypeMapper.addOrgType(orgTypeReqDTO);
                }
            }
        }
    }

    @Override
    public void modifyOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        addOrgType(orgTypeReqDTO);
    }

    @Override
    public void deleteOrgType(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            orgTypeMapper.deleteOrgType(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgType(List<String> ids, HttpServletResponse response) throws IOException {
        List<OrgTypeResDTO> orgTypeList = orgTypeMapper.listOrgType(ids);
        if (orgTypeList != null && !orgTypeList.isEmpty()) {
            List<ExcelOrgTypeResDTO> resList = new ArrayList<>();
            for (OrgTypeResDTO resDTO : orgTypeList) {
                ExcelOrgTypeResDTO res = ExcelOrgTypeResDTO.builder()
                        .recId(resDTO.getRecId())
                        .orgCode(resDTO.getOrgCode())
                        .orgName(resDTO.getOrgName())
                        .orgType(TYPE_MAP.get(resDTO.getOrgType()))
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "组织机构线别信息", resList);
        }
    }

}
