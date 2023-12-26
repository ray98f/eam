package com.wzmtr.eam.impl.secure;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.export.SecureDangerSourceExportBO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureDangerSourceListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureDangerSourceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.mapper.secure.SecureDangerSourceMapper;
import com.wzmtr.eam.service.secure.SecureDangerSourceService;
import com.wzmtr.eam.utils.*;
import io.minio.MinioClient;
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
public class SecureDangerSourceServiceImpl implements SecureDangerSourceService {

    @Autowired
    SecureDangerSourceMapper secureDangerSourceMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<SecureDangerSourceResDTO> dangerSourceList(SecureDangerSourceListReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureDangerSourceResDTO> query = secureDangerSourceMapper.query(reqDTO.of(), reqDTO.getDangerRiskId(), reqDTO.getDiscDateStart(), reqDTO.getDiscDateEnd());
        List<SecureDangerSourceResDTO> records = query.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            records.forEach(this::assemble);
        }
        return query;
    }

    private void assemble(SecureDangerSourceResDTO a) {
        if (StrUtil.isNotEmpty(a.getRecDept())) {
            a.setRecDeptName(organizationMapper.getExtraOrgByAreaId(a.getRecDept()));
        }
        if (StrUtil.isNotEmpty(a.getRespDeptCode())) {
            a.setRespDeptName(organizationMapper.getExtraOrgByAreaId(a.getRespDeptCode()));
        }
        if (StrUtil.isNotEmpty(a.getDangerRiskPic())) {
            a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDangerRiskPic().split(","))));
        }
    }

    @Override
    public void export(String dangerRiskId, String discDate, HttpServletResponse response) {
        List<SecureDangerSourceResDTO> resList = secureDangerSourceMapper.list(dangerRiskId, discDate);
        if (CollectionUtil.isNotEmpty(resList)) {
            List<SecureDangerSourceExportBO> exportList = new ArrayList<>();
            for (SecureDangerSourceResDTO resDTO : resList) {
                SecureDangerSourceExportBO exportBO = BeanUtils.convert(resDTO, SecureDangerSourceExportBO.class);
                exportBO.setInspectDept(organizationMapper.getExtraOrgByAreaId(resDTO.getRecDept()));
                exportBO.setRestoreDept(organizationMapper.getExtraOrgByAreaId(resDTO.getRespDeptCode()));
                exportList.add(exportBO);
            }
            try {
                EasyExcelUtils.export(response, "危险源排查信息", exportList);
            } catch (Exception e) {
                log.error("导出失败",e);
                throw new CommonException(ErrorCode.NORMAL_ERROR);
            }
        }
    }

    @Override
    public SecureDangerSourceResDTO detail(SecureDangerSourceDetailReqDTO reqDTO) {
        SecureDangerSourceResDTO detail = secureDangerSourceMapper.detail(reqDTO.getDangerRiskId());
        if (StringUtils.isNotEmpty(detail.getRecDept())) {
            detail.setRecDeptName(organizationMapper.getExtraOrgByAreaId(detail.getRecDept()));
        }
        if (StringUtils.isNotEmpty(detail.getRespDeptCode())) {
            String extraOrg = organizationMapper.getExtraOrgByAreaId(detail.getRespDeptCode());
            detail.setRespDeptName(StringUtils.isEmpty(extraOrg) ? organizationMapper.getOrgById(detail.getRespDeptCode()) : extraOrg);
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SecureDangerSourceAddReqDTO reqDTO) {
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        reqDTO.setDeleteFlag("0");
        String maxCode = secureDangerSourceMapper.getMaxCode();
        reqDTO.setDangerRiskId(CodeUtils.getNextCode(maxCode, "WX"));
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        secureDangerSourceMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isEmpty(reqDTO.getIds())) {
            return;
        }
        secureDangerSourceMapper.deleteByIds(reqDTO.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SecureDangerSourceAddReqDTO reqDTO) {
        if (StrUtil.isEmpty(reqDTO.getDangerRiskId())) {
            log.warn("危险源记录单号为空!");
            return;
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        secureDangerSourceMapper.update(reqDTO);
    }
    // public void upload(){
    //     minioClient.putObject()
    // }
}
