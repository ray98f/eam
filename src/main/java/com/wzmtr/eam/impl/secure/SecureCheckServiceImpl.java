package com.wzmtr.eam.impl.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.export.SecureCheckExportBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.SecureRecStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.mapper.secure.SecureCheckMapper;
import com.wzmtr.eam.service.secure.SecureCheckService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SecureCheckServiceImpl implements SecureCheckService {

    public static final String AREA_ID = "0103705";
    @Autowired
    private SecureCheckMapper secureMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public Page<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureCheckRecordListResDTO> list = secureMapper.query(reqDTO.of(), reqDTO);
        if (StringUtils.isNotEmpty(list.getRecords())) {
            List<SecureCheckRecordListResDTO> records = list.getRecords();
            // StreamUtils.map(records,SecureCheckRecordListResDTO::create);
            records.forEach(this::assembly);
            return list;
        }
        return new Page<>();
    }

    private void assembly(SecureCheckRecordListResDTO a) {
        String inspectDeptName = organizationMapper.getOrgById(a.getInspectDeptCode());
        String restoreDeptName = organizationMapper.getExtraOrgByAreaId(a.getRestoreDeptCode());
        if (StringUtils.isEmpty(inspectDeptName)) {
            inspectDeptName = organizationMapper.getExtraOrgByAreaId(a.getInspectDeptCode());
        }
        if (StringUtils.isEmpty(restoreDeptName)) {
            restoreDeptName = organizationMapper.getOrgById(a.getRestoreDeptCode());
        }
        if (StringUtils.isNotEmpty(a.getSecRiskPic())) {
            a.setSecRiskPicFile(fileMapper.selectFileInfo(Arrays.asList(a.getSecRiskPic().split(CommonConstants.COMMA))));
        }
        if (StringUtils.isNotEmpty(a.getRestorePic())) {
            a.setRestorePicFile(fileMapper.selectFileInfo(Arrays.asList(a.getRestorePic().split(CommonConstants.COMMA))));
        }
        //检查部门
        a.setInspectDeptName(inspectDeptName == null ? a.getInspectDeptCode() : inspectDeptName);
        //整改部门
        a.setRestoreDeptName(restoreDeptName == null ? a.getRestoreDeptCode() : restoreDeptName);
        a.setIsRestoredName("整改中");
        if (CommonConstants.TEN_STRING.equals(a.getIsRestored())) {
            a.setIsRestoredName("已完成整改");
        }
        if (CommonConstants.ONE_STRING.equals(a.getIsRestored())) {
            a.setIsRestoredName("未完成整改");
        }
    }

    @Override
    public SecureCheckRecordListResDTO detail(SecureCheckDetailReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getSecRiskId())) {
            return null;
        }
        SecureCheckRecordListResDTO detail = secureMapper.detail(reqDTO.getSecRiskId());
        if (detail == null) {
            return null;
        }
        String inspectDept = organizationMapper.getOrgById(detail.getInspectDeptCode());
        String restoreDept = organizationMapper.getExtraOrgByAreaId(detail.getRestoreDeptCode());
        detail.setInspectDeptName(StringUtils.isEmpty(inspectDept) ? detail.getInspectDeptCode() : inspectDept);
        detail.setRestoreDeptName(StringUtils.isEmpty(restoreDept) ? detail.getRestoreDeptCode() : restoreDept);
        return detail;
    }

    @Override
    public void export(SecureCheckRecordListReqDTO reqDTO, HttpServletResponse response) {
        List<SecureCheckRecordListResDTO> list = secureMapper.list(reqDTO);
        if (StringUtils.isEmpty(list)) {
            log.warn("未查询到数据，丢弃导出操作!");
            return;
        }
        List<SecureCheckExportBO> exportList = new ArrayList<>();
        for (SecureCheckRecordListResDTO res : list) {
            SecureCheckExportBO exportBO = BeanUtils.convert(res, SecureCheckExportBO.class);
            String inspectDept = organizationMapper.getOrgById(res.getInspectDeptCode());
            String restoreDept = organizationMapper.getExtraOrgByAreaId(res.getRestoreDeptCode());
            res.setIsRestoredName("整改中");
            if (CommonConstants.TEN_STRING.equals(res.getIsRestored())) {
                res.setIsRestoredName("已完成整改");
            }
            if (CommonConstants.ONE_STRING.equals(res.getIsRestored())) {
                res.setIsRestoredName("未完成整改");
            }
            if (StringUtils.isEmpty(inspectDept)) {
                inspectDept = organizationMapper.getExtraOrgByAreaId(res.getInspectDeptCode());
            }
            if (StringUtils.isEmpty(restoreDept)) {
                restoreDept = organizationMapper.getOrgById(res.getRestoreDeptCode());
            }
            String desc = null;
            SecureRecStatus secureRecStatus = SecureRecStatus.getByCode(res.getRecStatus());
            if (null != secureRecStatus) {
                desc = secureRecStatus.getDesc();
            }
            exportBO.setInspectDept(StringUtils.isEmpty(inspectDept) ? res.getInspectDeptCode() : inspectDept);
            exportBO.setRestoreDept(StringUtils.isEmpty(restoreDept) ? res.getRestoreDeptCode() : restoreDept);
            exportBO.setIsRestoredName(res.getIsRestoredName() == null ? res.getIsRestored() : res.getIsRestoredName());
            exportBO.setRecStatus(StringUtils.isEmpty(desc) ? res.getRecStatus() : desc);
            exportList.add(exportBO);
        }
        try {
            EasyExcelUtils.export(response, "安全、质量、消防检查记录", exportList);
        } catch (Exception e) {
            log.error("导出失败",e);
            throw new CommonException(ErrorCode.EXPORT_ERROR);
        }
    }

    @Override
    public void delete(BaseIdsEntity reqDTO) {
        if (StringUtils.isNotEmpty(reqDTO.getIds())) {
            secureMapper.deleteByIds(reqDTO.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SecureCheckAddReqDTO reqDTO) {
        reqDTO.setRecId(TokenUtils.getUuId());
        reqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        String secRiskId = CodeUtils.getNextCode(secureMapper.getMaxCode(), "AQ");
        reqDTO.setSecRiskId(secRiskId);
        // 默认初始为0
        reqDTO.setDeleteFlag("0");
        reqDTO.setSecRiskId(secRiskId);
        reqDTO.setRecStatus("10");
        reqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        if (AREA_ID.equals(reqDTO.getRestoreDeptCode().trim())) {
            reqDTO.setExt5("0103705");
        } else {
            reqDTO.setExt5(reqDTO.getRestoreDeptCode());
        }
        secureMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SecureCheckAddReqDTO reqDTO) {
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        secureMapper.update(reqDTO);
    }

}
