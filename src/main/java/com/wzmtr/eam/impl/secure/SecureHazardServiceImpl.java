package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.secure.SecureHazardMapper;
import com.wzmtr.eam.service.secure.SecureHazardService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
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

    @Override
    public Page<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureHazardResDTO> query = hazardMapper.query(reqDTO.of(), reqDTO.getRiskId(), reqDTO.getRiskRank(), reqDTO.getInspectDateBegin(), reqDTO.getInspectDateEnd(), reqDTO.getRestoreDesc(), reqDTO.getRecStatus());
        List<SecureHazardResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return null;
        }
        records.forEach(a -> {
            a.setRestoreDeptName(organizationMapper.getExtraOrgByAreaId(a.getRestoreDeptCode()));
            a.setInspectDeptName(organizationMapper.getExtraOrgByAreaId(a.getInspectDeptCode()));
            a.setNotifyDeptName(organizationMapper.getExtraOrgByAreaId(a.getNotifyDeptCode()));
        });
        return query;
    }

    @Override
    public SecureHazardResDTO detail(SecureHazardDetailReqDTO reqDTO) {
        SecureHazardResDTO detail = hazardMapper.detail(reqDTO.getRiskId());
        detail.setNotifyDeptName(organizationMapper.getExtraOrgByAreaId(detail.getNotifyDeptCode()));
        detail.setInspectDeptName(organizationMapper.getExtraOrgByAreaId(detail.getInspectDeptCode()));
        detail.setRestoreDeptName(organizationMapper.getExtraOrgByAreaId(detail.getRestoreDeptCode()));
        return detail;
    }

    @Override
    public void export(String riskId, String begin, String end, String riskRank, String restoreDesc, String workFlowInstStatus, HttpServletResponse response) {
        List<String> listName = Arrays.asList("安全隐患排查单号", "发现日期", "安全隐患等级", "安全隐患内容", "检查部门", "检查人", "地点", "计划完成日期", "整改部门", "整改情况", "记录状态", "备注");
        List<SecureHazardResDTO> resList = hazardMapper.list(riskId, begin, end, riskRank, restoreDesc, workFlowInstStatus);
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (SecureHazardResDTO resDTO : resList) {
            Map<String, String> map = new HashMap<>();
            map.put("安全隐患排查单号", resDTO.getRiskId());
            map.put("发现日期", resDTO.getInspectDate());
            map.put("安全隐患等级", resDTO.getRiskRank());
            map.put("安全隐患内容", resDTO.getRiskDetail());
            map.put("检查部门", organizationMapper.getExtraOrgByAreaId(resDTO.getInspectDeptCode()));
            map.put("检查人", resDTO.getInspectorCode());
            map.put("地点", resDTO.getPositionDesc());
            map.put("计划完成日期", resDTO.getPlanDate());
            map.put("整改部门", organizationMapper.getExtraOrgByAreaId(resDTO.getRestoreDeptCode()));
            map.put("整改情况", resDTO.getRestoreDesc());
            map.put("记录状态", resDTO.getRestoreDesc());
            map.put("备注", resDTO.getPlanDate());
            list.add(map);
        }
        ExcelPortUtil.excelPort("安全隐患整改信息", listName, list, null, response);
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
        reqDTO.setRecId(TokenUtil.getUuId());
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
        hazardMapper.update(reqDTO);
    }

    @Override
    public void finalExam(SecureHazardReqDTO reqDTO) {
        // todo
    }
}
