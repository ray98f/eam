package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.detection.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.req.detection.excel.ExcelSpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelSpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelSpecialEquipResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.detection.SpecialEquipMapper;
import com.wzmtr.eam.service.detection.SpecialEquipService;
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
 * @author frp
 */
@Service
@Slf4j
public class SpecialEquipServiceImpl implements SpecialEquipService {

    @Autowired
    private SpecialEquipMapper specialEquipMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Override
    public Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                                     String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<SpecialEquipResDTO> page =  specialEquipMapper.pageSpecialEquip(pageReqDTO.of(), equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus);
        List<SpecialEquipResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (SpecialEquipResDTO resDTO : list) {
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
                        log.error("设备编码为" + resDTO.getEquipCode() + "的特种设备检测有效期时间异常");
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public SpecialEquipResDTO getSpecialEquipDetail(String id) {
        SpecialEquipResDTO resDTO = specialEquipMapper.getSpecialEquipDetail(id);
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
    public void importSpecialEquip(MultipartFile file) {
        List<ExcelSpecialEquipReqDTO> list = EasyExcelUtils.read(file, ExcelSpecialEquipReqDTO.class);
        for (ExcelSpecialEquipReqDTO reqDTO : list) {
            SpecialEquipReqDTO req = new SpecialEquipReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecId(TokenUtils.getUuId());
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            req.setRecRevisor(TokenUtils.getCurrentPersonId());
            req.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            if (StringUtils.isNotEmpty(reqDTO.getSpecialEquipType())) {
                req.setSpecialEquipType("电梯".equals(reqDTO.getSpecialEquipType()) ? "10" : "起重机".equals(reqDTO.getSpecialEquipType()) ? "20" : "场（厂）内专用机动车辆".equals(reqDTO.getSpecialEquipType()) ? "30" : "40");
            }
            SysOffice manageOrg = organizationMapper.getByNames(reqDTO.getManageOrg());
            SysOffice secOrg = organizationMapper.getByNames(reqDTO.getSecOrg());
            if (Objects.isNull(manageOrg) || Objects.isNull(secOrg)) {
                continue;
            }
            req.setManageOrg(manageOrg.getId());
            req.setSecOrg(secOrg.getId());
            specialEquipMapper.updateEquip(req);
            specialEquipMapper.modifySpecialEquip(req);
        }
    }

    @Override
    public void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO) {
        specialEquipReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        specialEquipReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        specialEquipMapper.modifySpecialEquip(specialEquipReqDTO);
    }

    @Override
    public void exportSpecialEquip(List<String> ids, HttpServletResponse response) throws IOException {
        List<SpecialEquipResDTO> specialEquipResDTOList = specialEquipMapper.listSpecialEquip(ids);
        if (specialEquipResDTOList != null && !specialEquipResDTOList.isEmpty()) {
            List<ExcelSpecialEquipResDTO> list = new ArrayList<>();
            for (SpecialEquipResDTO resDTO : specialEquipResDTOList) {
                ExcelSpecialEquipResDTO res = new ExcelSpecialEquipResDTO();
                BeanUtils.copyProperties(resDTO, res);
                List<Dictionaries> dicList = dictionariesMapper.list("dm.specialequiptype", resDTO.getSpecialEquipType() == null ? " " : resDTO.getSpecialEquipType(), null);
                if (dicList != null && !dicList.isEmpty()) {
                    res.setSpecialEquipType(dicList.get(0).getItemCname());
                } else {
                    res.setSpecialEquipType("");
                }
                res.setUseLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getUseLineNo()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备台账信息", list);
        }
    }

    @Override
    public Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialEquipMapper.pageSpecialEquipHistory(pageReqDTO.of(), equipCode);
    }

    @Override
    public SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id) {
        return specialEquipMapper.getSpecialEquipHistoryDetail(id);
    }

    @Override
    public void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException {
        List<SpecialEquipHistoryResDTO> specialEquipHistoryResDTOList = specialEquipMapper.listSpecialEquipHistory(equipCode);
        if (specialEquipHistoryResDTOList != null && !specialEquipHistoryResDTOList.isEmpty()) {
            List<ExcelSpecialEquipHistoryResDTO> list = new ArrayList<>();
            for (SpecialEquipHistoryResDTO resDTO : specialEquipHistoryResDTOList) {
                ExcelSpecialEquipHistoryResDTO res = new ExcelSpecialEquipHistoryResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备检测记录历史信息", list);
        }
    }

}
