package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dataobject.SecureHazardDO;
import com.wzmtr.eam.dto.req.secure.SecureHazardAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureHazardReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureHazardResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.RiskRank;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
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
    private DictionariesMapper dictionariesMapper;

    @Override
    public Page<SecureHazardResDTO> list(SecureHazardReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureHazardResDTO> query = hazardMapper.query(reqDTO.of(), reqDTO.getRiskId(), reqDTO.getRiskRank(), reqDTO.getInspectDateBegin(), reqDTO.getInspectDateEnd(), reqDTO.getIsRestored(), reqDTO.getRecStatus());
        List<SecureHazardResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> {
            if (StringUtils.isNotEmpty(a.getRestoreDeptCode())) {
                a.setRestoreDeptName(organizationMapper.getExtraOrgByAreaId(a.getRestoreDeptCode()));
            }
            if (StringUtils.isNotEmpty(a.getInspectDeptCode())) {
                a.setInspectDeptName(organizationMapper.getExtraOrgByAreaId(a.getInspectDeptCode()));
            }
            if (StringUtils.isNotEmpty(a.getNotifyDeptCode())) {
                a.setNotifyDeptName(organizationMapper.getExtraOrgByAreaId(a.getNotifyDeptCode()));
            }
        });
        return query;
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
        List<String> listName = Arrays.asList("安全隐患排查单号", "发现日期", "安全隐患等级", "安全隐患内容", "检查部门", "检查人", "地点", "计划完成日期", "整改部门", "整改情况", "记录状态", "备注");
        List<SecureHazardResDTO> resList = hazardMapper.list(riskId, begin, end, riskRank, restoreDesc, workFlowInstStatus);
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (SecureHazardResDTO resDTO : resList) {
            RiskRank rank = RiskRank.getByCode(resDTO.getRiskRank());
            Map<String, String> map = new HashMap<>();
            map.put("安全隐患排查单号", resDTO.getRiskId());
            map.put("发现日期", resDTO.getInspectDate());
            map.put("安全隐患等级", rank == null ? resDTO.getRiskRank() : rank.getDesc());
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
        SecureHazardDO convert = __BeanUtil.convert(reqDTO, SecureHazardDO.class);
        hazardMapper.updateById(convert);
    }

    @Override
    public void finalExam(SecureHazardReqDTO reqDTO) {
        // todo
    }
}
