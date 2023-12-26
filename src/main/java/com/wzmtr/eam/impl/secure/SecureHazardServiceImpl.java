package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.export.SecureHazardExportBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.SecureHazardDO;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.ErrorCode;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


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
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureHazardResDTO> query = hazardMapper.query(reqDTO.of(), reqDTO.getRiskId(), reqDTO.getRiskRank(), reqDTO.getInspectDateBegin(), reqDTO.getInspectDateEnd(), reqDTO.getIsRestored(), reqDTO.getRecStatus());
        List<SecureHazardResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
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
            a.setRestoreDesc("整改中");
            if (CommonConstants.TEN_STRING.equals(a.getIsRestored())) {
                a.setRestoreDesc("已完成整改");
            }
            if (CommonConstants.ONE_STRING.equals(a.getIsRestored())) {
                a.setRestoreDesc("未完成整改");
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
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<SecureHazardExportBO> exportList = new ArrayList<>();
        resList.forEach(resDTO -> {
            resDTO.setRestoreDesc("整改中");
            if (CommonConstants.TEN_STRING.equals(resDTO.getIsRestored())) {
                resDTO.setRestoreDesc("已完成整改");
            }
            if (CommonConstants.ONE_STRING.equals(resDTO.getIsRestored())) {
                resDTO.setRestoreDesc("未完成整改");
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
            log.error("导出失败",e);
            throw new CommonException(ErrorCode.NORMAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isEmpty(reqDTO.getIds())) {
            return;
        }
        hazardMapper.deleteByIds(reqDTO.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SecureHazardAddReqDTO reqDTO) {
        String maxCode = hazardMapper.getMaxCode();
        reqDTO.setRiskId(CodeUtils.getNextCode(maxCode, "YH"));
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setDeleteFlag("0");
        reqDTO.setRecCreator(TokenUtil.getCurrentPerson().getPersonName());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        reqDTO.setArchiveFlag("0");
        reqDTO.setRecStatus("10");
        reqDTO.setIsRestored("30");
        hazardMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SecureHazardAddReqDTO reqDTO) {
        if (StrUtil.isEmpty(reqDTO.getRiskId())) {
            log.warn("安全隐患单号为空!");
            return;
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        SecureHazardDO convert = BeanUtils.convert(reqDTO, SecureHazardDO.class);
        hazardMapper.update(convert, new UpdateWrapper<SecureHazardDO>().eq("RISK_ID", reqDTO.getRiskId()));
    }

    @Override
    public void finalExam(SecureHazardReqDTO reqDTO) {
        // todo
    }
}
