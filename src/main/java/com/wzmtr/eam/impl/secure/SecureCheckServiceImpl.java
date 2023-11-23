package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
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
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:37
 */
@Service
@Slf4j
public class SecureCheckServiceImpl implements SecureCheckService {

    @Autowired
    private SecureCheckMapper secureMapper;

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public Page<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureCheckRecordListResDTO> list = secureMapper.query(reqDTO.of(), reqDTO.getSecRiskId(), reqDTO.getInspectDateStart(), reqDTO.getInspectDateEnd(), reqDTO.getIsRestoredCode(), reqDTO.getRecStatus());
        if (CollectionUtil.isNotEmpty(list.getRecords())) {
            List<SecureCheckRecordListResDTO> records = list.getRecords();
            // __StreamUtil.map(records,SecureCheckRecordListResDTO::create);
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
        if(StringUtils.isNotEmpty(a.getSecRiskPic())){
            a.setSecRiskPicFile(fileMapper.selectFileInfo(Arrays.asList(a.getSecRiskPic().split(","))));
        }
        if(StringUtils.isNotEmpty(a.getRestorePic())){
            a.setRestorePicFile(fileMapper.selectFileInfo(Arrays.asList(a.getRestorePic().split(","))));
        }
        //检查部门
        a.setInspectDeptName(inspectDeptName == null ? a.getInspectDeptCode() : inspectDeptName);
        //整改部门
        a.setRestoreDeptName(restoreDeptName == null ? a.getRestoreDeptCode() : restoreDeptName);
        a.setIsRestoredName("整改中");
        if ("10".equals(a.getIsRestored())) {
            a.setIsRestoredName("已完成整改");
        }
        if ("1".equals(a.getIsRestored())) {
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
    public void export(String secRiskId, String inspectDate, String restoreDesc, String workFlowInstStatus, HttpServletResponse response) {
        List<String> listName = Arrays.asList("检查问题单号", "发现日期", "检查问题", "检查部门", "检查人", "地点", "整改措施",
                "计划完成日期", "整改部门", "整改情况", "记录状态");
        List<SecureCheckRecordListResDTO> list = secureMapper.list(secRiskId, restoreDesc, inspectDate, workFlowInstStatus);
        if (CollectionUtil.isEmpty(list)) {
            log.warn("未查询到数据，丢弃导出操作!");
            return;
        }
        List<Map<String, String>> exportList = new ArrayList<>();
        for (SecureCheckRecordListResDTO res : list) {
            String inspectDept = organizationMapper.getOrgById(res.getInspectDeptCode());
            String restoreDept = organizationMapper.getExtraOrgByAreaId(res.getRestoreDeptCode());
            res.setIsRestoredName("整改中");
            if ("10".equals(res.getIsRestored())) {
                res.setIsRestoredName("已完成整改");
            }
            if ("1".equals(res.getIsRestored())) {
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
            Map<String, String> map = new HashMap<>();
            map.put("检查问题单号", res.getSecRiskId());
            map.put("发现日期", res.getInspectDate());
            map.put("检查问题", res.getSecRiskDetail());
            map.put("检查部门", StringUtils.isEmpty(inspectDept) ? res.getInspectDeptCode() : inspectDept);
            map.put("检查人", res.getInspectorCode());
            map.put("地点", res.getPositionDesc());
            map.put("整改措施", res.getRestoreDetail());
            map.put("计划完成日期", res.getPlanDate());
            map.put("整改部门", StringUtils.isEmpty(restoreDept) ? res.getRestoreDeptCode() : restoreDept);
            map.put("整改情况", res.getIsRestoredName() == null ? res.getIsRestored() : res.getIsRestoredName());
            map.put("记录状态", StringUtils.isEmpty(desc) ? res.getRecStatus() : desc);
            exportList.add(map);
        }
        ExcelPortUtil.excelPort("安全、质量、消防检查记录", listName, exportList, null, response);
    }

    @Override
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isNotEmpty(reqDTO.getIds())) {
            secureMapper.deleteByIds(reqDTO.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SecureCheckAddReqDTO reqDTO) {
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        String secRiskId = CodeUtils.getNextCode(secureMapper.getMaxCode(), "AQ");
        reqDTO.setSecRiskId(secRiskId);
        // 默认初始为0
        reqDTO.setDeleteFlag("0");
        reqDTO.setSecRiskId(secRiskId);
        reqDTO.setRecStatus("10");
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        if (reqDTO.getRestoreDeptCode().trim().equals("0103705")) {
            reqDTO.setExt5("0103705");
        } else {
            reqDTO.setExt5(reqDTO.getRestoreDeptCode());
        }
        secureMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SecureCheckAddReqDTO reqDTO) {
        reqDTO.setRecReviseTime(DateUtil.dateTimeNow("yyyyMMddHHmmss"));
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        secureMapper.update(reqDTO);
    }

}
