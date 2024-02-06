package com.wzmtr.eam.impl.equipment;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.equipment.UnitCodeReqDTO;
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
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.equipment.EquipmentService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.QrUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            boolean carBool = (CommonConstants.LINE_CODE_ONE.equals(parentNodeRecId) && REGION_CODE_ES1.equals(recId) && CommonConstants.LINE_CODE_ONE.equals(lineCode)) ||
                            (CommonConstants.LINE_CODE_TWO.equals(parentNodeRecId) && REGION_CODE_ES2.equals(recId) && CommonConstants.LINE_CODE_TWO.equals(lineCode));
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
            } else if (StringUtils.isNotEmpty(parentNodeRecId) && !REGION_CODE_ES1.equals(parentNodeRecId) && !REGION_CODE_ES2.equals(parentNodeRecId)) {
                res.setEquipment(equipmentMapper.listEquipmentCategory(equipmentCategoryCode, lineCode, recId, regionCode));
            }
        }
        return res;
    }

    @Override
    public Page<EquipmentResDTO> pageEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                               String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        if (StringUtils.isNotEmpty(position1Code) && position1Code.contains(ES)) {
            majorCode = "07";
            position1Code = null;
        }
        return equipmentMapper.pageEquipment(pageReqDTO.of(), equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture);
    }

    @Override
    public EquipmentResDTO getEquipmentDetail(String id) {
        return equipmentMapper.getEquipmentDetail(id);
    }

    @Override
    public void importEquipment(MultipartFile file) {
        List<ExcelEquipmentReqDTO> list = EasyExcelUtils.read(file, ExcelEquipmentReqDTO.class);
        List<EquipmentReqDTO> temp = new ArrayList<>();
        for (ExcelEquipmentReqDTO reqDTO : list) {
            EquipmentReqDTO req = new EquipmentReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setUseLineNo(Objects.isNull(reqDTO.getUseLineName()) ? "" : "S1线".equals(reqDTO.getUseLineName()) ? "01" : "02");
            req.setUseSegNo(Objects.isNull(reqDTO.getUseSegName()) ? "" : "一期".equals(reqDTO.getUseSegName()) ? "01" : "二期".equals(reqDTO.getUseSegName()) ? "二期" : "三期");
            req.setSpecialEquipFlag(Objects.isNull(reqDTO.getSpecialEquipFlag()) ? "" : "否".equals(reqDTO.getSpecialEquipFlag()) ? "10" : "20");
            req.setRecId(TokenUtil.getUuId());
            req.setApprovalStatus("30");
            req.setQuantity(new BigDecimal("1"));
            req.setRecCreator(TokenUtil.getCurrentPersonId());
            req.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            CurrentLoginUser user = TokenUtil.getCurrentPerson();
            req.setCompanyCode(user.getCompanyAreaId());
            req.setCompanyName(user.getCompanyName());
            req.setDeptCode(user.getOfficeAreaId());
            req.setDeptName(user.getOfficeName());
            String unitNo = insertUnitCode(req, user);
            req.setEquipCode(unitNo);
            temp.add(req);
        }
        if (!temp.isEmpty()) {
            equipmentMapper.importEquipment(temp);
        }
    }

    @Override
    public void exportEquipment(List<String> ids, HttpServletResponse response) throws IOException {
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.listEquipment(ids);
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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listOverhaul(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<FaultDetailResDTO> listFault(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listFault(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<PartReplaceResDTO> listPartReplace(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listPartReplace(pageReqDTO.of(), equipCode);
    }

    private String insertUnitCode(EquipmentReqDTO req, CurrentLoginUser user) {
        String unitNo = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(1)) + 1);
        String equipCode = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(4)) + 1);
        UnitCodeReqDTO unitCodeReqDTO = new UnitCodeReqDTO();
        unitCodeReqDTO.setRecId(TokenUtil.getUuId());
        unitCodeReqDTO.setUnitNo(unitNo);
        unitCodeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        unitCodeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        unitCodeReqDTO.setDevNo(equipCode);
        unitCodeReqDTO.setBatchNo("");
        unitCodeReqDTO.setAssetNo("");
        unitCodeReqDTO.setProCode("");
        unitCodeReqDTO.setProName("");
        unitCodeReqDTO.setOrderNo(req.getOrderNo());
        unitCodeReqDTO.setOrderName(req.getOrderName());
        unitCodeReqDTO.setSupplierId(user.getOfficeAreaId());
        unitCodeReqDTO.setSupplierName(user.getNames());
        unitCodeReqDTO.setMatSpecifi(req.getMatSpecifi());
        unitCodeReqDTO.setBrand(req.getBrand());
        equipmentMapper.insertUnitCode(unitCodeReqDTO);
        return unitNo;
    }

}
