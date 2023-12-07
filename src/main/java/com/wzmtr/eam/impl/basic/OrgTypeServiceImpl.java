package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelOrgTypeResDTO;
import com.wzmtr.eam.dto.res.basic.OrgTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.basic.OrgTypeMapper;
import com.wzmtr.eam.service.basic.OrgTypeService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgTypeMapper.pageOrgType(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), orgType);
    }

    @Override
    public OrgTypeResDTO getOrgTypeDetail(String id) {
        return orgTypeMapper.getOrgTypeDetail(id);
    }

    @Override
    public void addOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        Integer result = orgTypeMapper.selectOrgTypeIsExist(orgTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgTypeReqDTO.setRecId(TokenUtil.getUuId());
        orgTypeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        orgTypeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgTypeMapper.addOrgType(orgTypeReqDTO);
    }

    @Override
    public void modifyOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        Integer result = orgTypeMapper.selectOrgTypeIsExist(orgTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgTypeReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        orgTypeReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgTypeMapper.modifyOrgType(orgTypeReqDTO);
    }

    @Override
    public void deleteOrgType(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            orgTypeMapper.deleteOrgType(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgType(String orgCode, String orgType, HttpServletResponse response) throws IOException {
        List<String> orgCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        List<OrgTypeResDTO> orgTypeList = orgTypeMapper.listOrgType(StringUtils.getSumArrayList(orgCodes), orgType);
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
