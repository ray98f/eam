package com.wzmtr.eam.impl.equipment;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelEquipmentReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipmentResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.equipment.EquipmentService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.QrUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
public class EquipmentServiceImpl implements EquipmentService {

    private static final String ES = "ES";
    private static final String REGION_CODE_ES1 = "ES1";
    private static final String REGION_CODE_ES2 = "ES2";

    @Resource
    private UserAccountService userAccountService;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public List<RegionResDTO> listTrainRegion(String lineCode) {
        return equipmentMapper.listTrainRegion(lineCode);
    }

    @Override
    public EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode) {
        EquipmentTreeResDTO res = new EquipmentTreeResDTO();
        if (StringUtils.isEmpty(lineCode)) {
            res.setLine(equipmentMapper.listLine());
        } else {
            List<RegionResDTO> region = equipmentMapper.listRegion(lineCode, regionCode, recId);
            boolean bool = StringUtils.isEmpty(equipmentCategoryCode) && StringUtils.isNotEmpty(region);
            // 判断是否为车辆层级
            boolean carBool = (CommonConstants.LINE_CODE_ONE.equals(parentNodeRecId) && REGION_CODE_ES1.equals(recId) && CommonConstants.LINE_CODE_ONE.equals(lineCode))
                    || (CommonConstants.LINE_CODE_TWO.equals(parentNodeRecId) && REGION_CODE_ES2.equals(recId) && CommonConstants.LINE_CODE_TWO.equals(lineCode));
            if (bool) {
                if (StringUtils.isEmpty(regionCode)) {
                    RegionResDTO regionResDTO = new RegionResDTO();
                    String name = "E" + (CommonConstants.LINE_CODE_ONE.equals(lineCode) ? "S1" : "S2");
                    regionResDTO.setRecId(name);
                    regionResDTO.setParentNodeRecId(lineCode);
                    regionResDTO.setNodeCode(name);
                    regionResDTO.setNodeName((CommonConstants.LINE_CODE_ONE.equals(lineCode) ? "S1线" : "S2线") + "车辆");
                    regionResDTO.setLineCode(lineCode);
                    region.add(regionResDTO);
                } else if (REGION_CODE_ES1.equals(regionCode) || REGION_CODE_ES2.equals(regionCode)) {
                    region = equipmentMapper.listCarRegion(lineCode, recId);
                }
                res.setRegion(region);
            } else if (carBool) {
                res.setRegion(equipmentMapper.listCarRegion(lineCode, recId));
            } else if (StringUtils.isNotEmpty(parentNodeRecId)
                    && !REGION_CODE_ES1.equals(parentNodeRecId)
                    && !REGION_CODE_ES2.equals(parentNodeRecId)) {
                res.setEquipment(equipmentMapper.listEquipmentCategory(equipmentCategoryCode, lineCode, recId, regionCode));
            }
        }
        return res;
    }

    @Override
    public Page<EquipmentResDTO> pageEquipment(String equipCode, String equipName, String useLineNo,
                                               String useSegNo, String position1Code, String majorCode,
                                               String systemCode, String equipTypeCode, String brand,
                                               String startTime, String endTime, String manufacture, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        //  专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(majorCode)) {
            userMajorList = userAccountService.listUserMajor();
        }
        if (StringUtils.isNotEmpty(position1Code) && position1Code.contains(ES)) {
            majorCode = "07";
            position1Code = null;
        }
        return equipmentMapper.pageEquipment(pageReqDTO.of(), equipCode, equipName, useLineNo, useSegNo, position1Code,
                majorCode, systemCode, equipTypeCode, brand, startTime, endTime, manufacture, userMajorList);
    }

    @Override
    public List<EquipmentResDTO> allList(String equipCode, String equipName, String useLineNo,
                                         String useSegNo, String position1Code, String majorCode,
                                         String systemCode, String equipTypeCode, String brand,
                                         String startTime, String endTime, String manufacture) {
        if (StringUtils.isNotEmpty(position1Code) && position1Code.contains(ES)) {
            majorCode = "07";
            position1Code = null;
        }
        return equipmentMapper.allList(equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture);
    }

    @Override
    public EquipmentResDTO getEquipmentDetail(String id) {
        return equipmentMapper.getEquipmentDetail(id);
    }

    @Override
    public void addEquipment(EquipmentReqDTO equipmentReqDTO) {
        equipmentReqDTO.setRecId(TokenUtils.getUuId());
        equipmentReqDTO.setApprovalStatus("30");
        equipmentReqDTO.setQuantity(new BigDecimal("1"));
        equipmentReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        equipmentReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentReqDTO.setInAccountTime(DateUtils.getCurrentTime());
        CurrentLoginUser user = TokenUtils.getCurrentPerson();
        equipmentReqDTO.setCompanyCode(StringUtils.isNotEmpty(user.getCompanyAreaId()) ? user.getCompanyAreaId() : " ");
        equipmentReqDTO.setCompanyName(StringUtils.isNotEmpty(user.getCompanyName()) ? user.getCompanyName() : " ");
        equipmentReqDTO.setDeptCode(StringUtils.isNotEmpty(user.getOfficeAreaId()) ? user.getOfficeAreaId() : " ");
        equipmentReqDTO.setDeptName(StringUtils.isNotEmpty(user.getOfficeName()) ? user.getOfficeName() : " ");
        equipmentReqDTO.setEquipCode(getEquipCode());
        equipmentReqDTO.setSpecialEquipFlag("10");
        equipmentReqDTO.setOtherEquipFlag("10");
        equipmentMapper.addEquipment(equipmentReqDTO);
    }

    @Override
    public void modifyEquipment(EquipmentReqDTO equipmentReqDTO) {
        equipmentReqDTO.setApprovalStatus("30");
        equipmentReqDTO.setQuantity(new BigDecimal("1"));
        equipmentReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        equipmentReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        CurrentLoginUser user = TokenUtils.getCurrentPerson();
        equipmentReqDTO.setCompanyCode(user.getCompanyAreaId());
        equipmentReqDTO.setCompanyName(user.getCompanyName());
        equipmentReqDTO.setDeptCode(user.getOfficeAreaId());
        equipmentReqDTO.setDeptName(user.getOfficeName());
        equipmentReqDTO.setSpecialEquipFlag("10");
        equipmentReqDTO.setOtherEquipFlag("10");
        equipmentMapper.modifyEquipment(equipmentReqDTO);
    }

    @Override
    public void deleteEquipment(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            equipmentMapper.deleteEquipment(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importEquipment(MultipartFile file) {
        List<ExcelEquipmentReqDTO> list = EasyExcelUtils.read(file, ExcelEquipmentReqDTO.class);
        List<EquipmentReqDTO> temp = new ArrayList<>();
        String equipCode = getEquipCode();
        for (ExcelEquipmentReqDTO reqDTO : list) {
            EquipmentReqDTO req = new EquipmentReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setUseLineNo(Objects.isNull(reqDTO.getUseLineName()) ? "" :
                    "S1线".equals(reqDTO.getUseLineName()) ? "01" : "02");
            req.setUseSegNo(Objects.isNull(reqDTO.getUseSegName()) ? "" :
                    "一期".equals(reqDTO.getUseSegName()) ? "01" :
                            "二期".equals(reqDTO.getUseSegName()) ? "02" : "03");
            req.setSpecialEquipFlag(Objects.isNull(reqDTO.getSpecialEquipFlag()) ? "" :
                    "否".equals(reqDTO.getSpecialEquipFlag()) ? "10" : "20");
            req.setOtherEquipFlag(Objects.isNull(reqDTO.getOtherEquipFlag()) ? "" :
                    "否".equals(reqDTO.getOtherEquipFlag()) ? "10" : "20");
            req.setRecId(TokenUtils.getUuId());
            req.setApprovalStatus("30");
            req.setQuantity(new BigDecimal("1"));
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            CurrentLoginUser user = TokenUtils.getCurrentPerson();
            req.setCompanyCode(StringUtils.isNotEmpty(user.getCompanyAreaId()) ? user.getCompanyAreaId() : " ");
            req.setCompanyName(StringUtils.isNotEmpty(user.getCompanyName()) ? user.getCompanyName() : " ");
            req.setDeptCode(StringUtils.isNotEmpty(user.getOfficeAreaId()) ? user.getOfficeAreaId() : " ");
            req.setDeptName(StringUtils.isNotEmpty(user.getOfficeName()) ? user.getOfficeName() : " ");
            req.setEquipCode(equipCode);
            temp.add(req);
            // 获取下一个设备编号
            equipCode = CodeUtils.getNextCode(equipCode, 2);
        }
        if (!temp.isEmpty()) {
            if (temp.size() <= CommonConstants.ONE_THOUSAND) {
                equipmentMapper.importEquipment(temp);
            } else {
                int times = (int) Math.ceil(temp.size() / 1000.0);
                for (int i = 0; i < times; i++) {
                    equipmentMapper.importEquipment(temp.subList(i * 1000, Math.min((i + 1) * 1000, temp.size() - 1)));
                }
            }
        }
    }

    @Override
    public void exportEquipment(EquipmentExportReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.listEquipment(reqDTO);
        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
            List<ExcelEquipmentResDTO> list = new ArrayList<>();
            for (EquipmentResDTO resDTO : equipmentResDTOList) {
                ExcelEquipmentResDTO res = new ExcelEquipmentResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                res.setSpecialEquipFlag(CommonConstants.TEN_STRING.equals(resDTO.getSpecialEquipFlag()) ? "非特殊设备" : "特殊设备");
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备台账信息", list);
        }
    }

    @Override
    public List<EquipmentQrResDTO> generateQr(BaseIdsEntity baseIdsEntity) throws ParseException {
        List<EquipmentQrResDTO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                EquipmentQrResDTO res = new EquipmentQrResDTO();
                EquipmentResDTO resDTO = equipmentMapper.getEquipmentDetail(id);
                if (!Objects.isNull(resDTO)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                    String qr = resDTO.getEquipCode() + "\n设备名称：" + resDTO.getEquipName() + "\n开始使用时间：" + sdf1.format(sdf.parse(resDTO.getStartUseDate()));
                    res.setRecId(id);
                    res.setCompanyName(resDTO.getCompanyName());
                    res.setDeptName(resDTO.getDeptName());
                    res.setEquipCode(resDTO.getEquipCode());
                    res.setEquipName(resDTO.getEquipName());
                    res.setStartUseDate(sdf1.format(sdf.parse(resDTO.getStartUseDate())));
                    res.setQr(QrCodeUtil.generateAsBase64(qr, QrUtils.initQrConfig(), "png"));
                    list.add(res);
                }
            }
        }
        return list;
    }

    @Override
    public Page<OverhaulOrderDetailResDTO> listOverhaul(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listOverhaul(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<FaultDetailResDTO> listFault(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listFault(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<PartReplaceResDTO> listPartReplace(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listPartReplace(pageReqDTO.of(), equipCode);
    }

    /**
     * 获取设备编号
     * @return 设备编号
     */
    private String getEquipCode() {
        String maxCode = equipmentMapper.getMaxCode();
        String equipCode;
        if (StringUtils.isNotEmpty(maxCode)) {
            equipCode = CodeUtils.getNextCode(maxCode, 2);
        } else {
            equipCode = CommonConstants.INIT_EQUIPMENT_CODE;
        }
        return equipCode;
    }

}
