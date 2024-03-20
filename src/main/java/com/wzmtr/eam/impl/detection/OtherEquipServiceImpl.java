package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.detection.OtherEquipReqDTO;
import com.wzmtr.eam.dto.req.detection.excel.ExcelOtherEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipResDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipTypeResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelOtherEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelOtherEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.detection.OtherEquipMapper;
import com.wzmtr.eam.mapper.detection.OtherEquipTypeMapper;
import com.wzmtr.eam.service.detection.OtherEquipService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 其他设备管理-其他设备台账
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Service
@Slf4j
public class OtherEquipServiceImpl implements OtherEquipService {

    @Autowired
    private OtherEquipMapper otherEquipMapper;

    @Autowired
    private OtherEquipTypeMapper otherEquipTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OtherEquipResDTO> pageOtherEquip(String equipCode, String equipName, String otherEquipCode, String factNo,
                                                     String useLineNo, String position1Code, String otherEquipType, String equipStatus, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<OtherEquipResDTO> page =  otherEquipMapper.pageOtherEquip(pageReqDTO.of(), equipCode, equipName, otherEquipCode, factNo, useLineNo,
                position1Code, otherEquipType, equipStatus);
        List<OtherEquipResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (OtherEquipResDTO resDTO : list) {
                if (StringUtils.isNotEmpty(resDTO.getManageOrg())) {
                    resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                }
                if (StringUtils.isNotEmpty(resDTO.getSecOrg())) {
                    resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                }
                if (StringUtils.isNotEmpty(resDTO.getVerifyValidityDate())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String day = DateUtils.getDayByMonth(3);
                    try {
                        resDTO.setIsWarn(sdf.parse(day).getTime() <= sdf.parse(resDTO.getVerifyValidityDate()).getTime() ? 0 : 1);
                    } catch (ParseException e) {
                        log.error("设备编码为" + resDTO.getEquipCode() + "的其他设备检测有效期时间异常");
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public OtherEquipResDTO getOtherEquipDetail(String id) {
        OtherEquipResDTO resDTO = otherEquipMapper.getOtherEquipDetail(id);
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(resDTO.getManageOrg())) {
            resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        }
        if (StringUtils.isNotEmpty(resDTO.getSecOrg())) {
            resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        }
        return resDTO;
    }

    @Override
    public void importOtherEquip(MultipartFile file) {
        List<String> otherCode = new ArrayList<>();
        List<ExcelOtherEquipReqDTO> list = EasyExcelUtils.read(file, ExcelOtherEquipReqDTO.class);
        for (ExcelOtherEquipReqDTO reqDTO : list) {
            OtherEquipReqDTO req = new OtherEquipReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecId(TokenUtils.getUuId());
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            req.setRecRevisor(TokenUtils.getCurrentPersonId());
            req.setRecReviseTime(DateUtils.getCurrentTime());
            if (StringUtils.isNotEmpty(reqDTO.getOtherEquipType())) {
                OtherEquipTypeResDTO res = otherEquipTypeMapper.getOtherEquipTypeDetailByType(null, reqDTO.getOtherEquipType());
                if (Objects.isNull(res)) {
                    otherCode.add(req.getOtherEquipCode());
                    continue;
                }
                req.setOtherEquipType(res.getTypeName());
            }
            SysOffice manageOrg = organizationMapper.getByNames(reqDTO.getManageOrg());
            SysOffice secOrg = organizationMapper.getByNames(reqDTO.getSecOrg());
            if (Objects.isNull(manageOrg) || Objects.isNull(secOrg)) {
                otherCode.add(req.getOtherEquipCode());
                continue;
            }
            req.setManageOrg(manageOrg.getId());
            req.setSecOrg(secOrg.getId());
            otherEquipMapper.updateEquip(req);
            otherEquipMapper.modifyOtherEquip(req);
        }
        throw new CommonException(ErrorCode.NORMAL_ERROR, "其他设备编号为" + String.join("、", otherCode) + "的其他设备导入失败，请重试");
    }

    @Override
    public void modifyOtherEquip(OtherEquipReqDTO otherEquipReqDTO) {
        otherEquipReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        otherEquipReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        otherEquipMapper.modifyOtherEquip(otherEquipReqDTO);
    }

    @Override
    public void exportOtherEquip(List<String> ids, HttpServletResponse response) throws IOException {
        List<OtherEquipResDTO> otherEquipResDTOList = otherEquipMapper.listOtherEquip(ids);
        if (otherEquipResDTOList != null && !otherEquipResDTOList.isEmpty()) {
            List<ExcelOtherEquipResDTO> list = new ArrayList<>();
            for (OtherEquipResDTO resDTO : otherEquipResDTOList) {
                ExcelOtherEquipResDTO res = new ExcelOtherEquipResDTO();
                BeanUtils.copyProperties(resDTO, res);
                OtherEquipTypeResDTO otherEquipType = otherEquipTypeMapper.getOtherEquipTypeDetailByType(resDTO.getOtherEquipType(), null);
                if (Objects.isNull(otherEquipType)) {
                    continue;
                }
                res.setOtherEquipType(otherEquipType.getTypeName());
                res.setUseLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getUseLineNo()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "其他设备台账信息", list);
        }
    }

    @Override
    public Page<OtherEquipHistoryResDTO> pageOtherEquipHistory(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return otherEquipMapper.pageOtherEquipHistory(pageReqDTO.of(), equipCode);
    }

    @Override
    public OtherEquipHistoryResDTO getOtherEquipHistoryDetail(String id) {
        return otherEquipMapper.getOtherEquipHistoryDetail(id);
    }

    @Override
    public void exportOtherEquipHistory(String equipCode, HttpServletResponse response) throws IOException {
        List<OtherEquipHistoryResDTO> otherEquipHistoryResDTOList = otherEquipMapper.listOtherEquipHistory(equipCode);
        if (otherEquipHistoryResDTOList != null && !otherEquipHistoryResDTOList.isEmpty()) {
            List<ExcelOtherEquipHistoryResDTO> list = new ArrayList<>();
            for (OtherEquipHistoryResDTO resDTO : otherEquipHistoryResDTOList) {
                ExcelOtherEquipHistoryResDTO res = new ExcelOtherEquipHistoryResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "其他设备检测记录历史信息", list);
        }
    }

}
