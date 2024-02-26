package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.export.SecureHazardExportBO;
import com.wzmtr.eam.dataobject.SecureHazardDO;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.ReformCase;
import com.wzmtr.eam.enums.RiskRank;
import com.wzmtr.eam.enums.SecureRecStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.mapper.secure.SecureHazardMapper;
import com.wzmtr.eam.service.secure.SecureHazardService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:37
 */
@Service
@Slf4j
public class SecureHazardServiceImpl implements SecureHazardService {


    @Autowired
    private SecureHazardMapper hazardMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureHazardResDTO> query = hazardMapper.query(reqDTO.of(), reqDTO.getRiskId(), reqDTO.getRiskRank(), reqDTO.getInspectDateBegin(), reqDTO.getInspectDateEnd(), reqDTO.getIsRestored(), reqDTO.getRecStatus());
        List<SecureHazardResDTO> records = query.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(this::assembly);
        return query;
    }

    private void assembly(SecureHazardResDTO a) {
        if (StringUtils.isNotEmpty(a.getRestoreDeptCode())) {
            a.setRestoreDeptName(organizationMapper.getNamesById(a.getRestoreDeptCode()));
        }
        if (StringUtils.isNotEmpty(a.getIsRestored())) {
            a.setRestoreDesc(ReformCase.IN_REF0RM.getDesc());
            ReformCase reformCase = ReformCase.getByCode(a.getIsRestored());
            if (null != reformCase) {
                a.setRestoreDesc(reformCase.getDesc());
            }
        }
        if (StringUtils.isNotEmpty(a.getInspectDeptCode())) {
            a.setInspectDeptName(organizationMapper.getNamesById(a.getInspectDeptCode()));
        }
        if (StringUtils.isNotEmpty(a.getRiskPic())) {
            a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getRiskPic().split(","))));
        }
        if (StringUtils.isNotEmpty(a.getNotifyDeptCode())) {
            a.setNotifyDeptName(organizationMapper.getNamesById(a.getNotifyDeptCode()));
        }
    }

    @Override
    public SecureHazardResDTO detail(SecureHazardDetailReqDTO reqDTO) {
        SecureHazardResDTO detail = hazardMapper.detail(reqDTO.getRiskId());
        if (StringUtils.isNotEmpty(detail.getRestoreDeptCode())) {
            detail.setRestoreDeptName(organizationMapper.getExtraOrgByAreaId(detail.getRestoreDeptCode()));
        }
        if (StringUtils.isNotEmpty(detail.getInspectDeptCode())) {
            detail.setInspectDeptName(organizationMapper.getExtraOrgByAreaId(detail.getInspectDeptCode()));
        }
        if (StringUtils.isNotEmpty(detail.getNotifyDeptCode())) {
            detail.setNotifyDeptName(organizationMapper.getExtraOrgByAreaId(detail.getNotifyDeptCode()));
        }
        return detail;
    }

    @Override
    public void export(String riskId, String begin, String end, String riskRank, String restoreDesc, String workFlowInstStatus, HttpServletResponse response) {
        List<SecureHazardResDTO> resList = hazardMapper.list(riskId, begin, end, riskRank, restoreDesc, workFlowInstStatus);
        if (StringUtils.isEmpty(resList)) {
            return;
        }
        List<SecureHazardExportBO> exportList = new ArrayList<>();
        resList.forEach(resDTO -> {
            if (StringUtils.isNotEmpty(resDTO.getIsRestored())) {
                resDTO.setRestoreDesc(ReformCase.IN_REF0RM.getDesc());
                ReformCase reformCase = ReformCase.getByCode(resDTO.getIsRestored());
                if (null != reformCase) {
                    resDTO.setRestoreDesc(reformCase.getDesc());
                }
            }
            SecureHazardExportBO exportBO = BeanUtils.convert(resDTO, SecureHazardExportBO.class);
            SecureRecStatus secureRecStatus = SecureRecStatus.getByCode(resDTO.getRecStatus());
            RiskRank rank = RiskRank.getByCode(resDTO.getRiskRank());
            exportBO.setRiskRank(rank == null ? resDTO.getRiskRank() : rank.getDesc());
            exportBO.setRecStatus(secureRecStatus == null ? resDTO.getRecStatus() : secureRecStatus.getDesc());
            exportList.add(exportBO);
        });
        try {
            EasyExcelUtils.export(response, "安全隐患整改信息", exportList);
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR);
        }
    }

    @Override
    public void delete(BaseIdsEntity reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getIds())) {
            return;
        }
        hazardMapper.deleteByIds(reqDTO.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
    }

    @Override
    public void add(SecureHazardAddReqDTO reqDTO) {
        String maxCode = hazardMapper.getMaxCode();
        reqDTO.setRiskId(CodeUtils.getNextCode(maxCode, "YH"));
        reqDTO.setRecId(TokenUtils.getUuId());
        reqDTO.setDeleteFlag("0");
        reqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        reqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        reqDTO.setArchiveFlag("0");
        reqDTO.setRecStatus("10");
        reqDTO.setIsRestored(ReformCase.IN_REF0RM.getCode());
        hazardMapper.add(reqDTO);
    }

    @Override
    public void update(SecureHazardAddReqDTO reqDTO) {
        String riskId = Assert.notNull(reqDTO.getRiskId(), ErrorCode.PARAM_ERROR);
        SecureHazardDO secureHazardDO = hazardMapper.selectOne(new QueryWrapper<SecureHazardDO>().eq("RISK_ID", riskId));
        Assert.notNull(secureHazardDO, ErrorCode.RESOURCE_NOT_EXIST);
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        SecureHazardDO convert = BeanUtils.convert(reqDTO, SecureHazardDO.class);
        hazardMapper.update(convert, new UpdateWrapper<SecureHazardDO>().eq("RISK_ID", riskId));
    }
}
