package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.util.BeanUtils;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.*;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.TransferResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelTransferResDTO;
import com.wzmtr.eam.entity.*;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentPartMapper;
import com.wzmtr.eam.mapper.equipment.PartFaultMapper;
import com.wzmtr.eam.mapper.equipment.TransferMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.equipment.TransferService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author frp
 */
@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    public static final String S = "S";

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Autowired
    private EquipmentPartMapper equipmentPartMapper;

    @Autowired
    private PartFaultMapper partFaultMapper;

    @Autowired
    private OverTodoService overTodoService;

    @Override
    public Page<TransferResDTO> pageTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                                             String majorCode, String orderNo, String orderName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        String eamProcessStatus1 = null;
        String eamProcessStatus2 = null;
        if (StringUtils.isNotEmpty(eamProcessStatus)) {
            if (CommonConstants.TWENTY_STRING.equals(eamProcessStatus)) {
                eamProcessStatus1 = "20";
            } else if (CommonConstants.THIRTY_STRING.equals(eamProcessStatus)) {
                eamProcessStatus = "20";
                eamProcessStatus2 = "30";
            }
        }
        return transferMapper.pageTransfer(pageReqDTO.of(), transferNo, itemCode, itemName, position1Code, eamProcessStatus,
                eamProcessStatus1, eamProcessStatus2, majorCode, orderNo, orderName);
    }

    @Override
    public TransferResDTO getTransferDetail(String id) {
        return transferMapper.getTransferDetail(id);
    }

    @Override
    public void exportTransfer(String transferNo, String itemCode, String itemName, String position1Code, String eamProcessStatus,
                               String majorCode, String orderNo, String orderName, HttpServletResponse response) throws IOException {
        List<TransferResDTO> transferResDTOList = transferMapper.listTransfer(transferNo, itemCode, itemName, position1Code, eamProcessStatus,
                majorCode, orderNo, orderName);
        if (transferResDTOList != null && !transferResDTOList.isEmpty()) {
            List<ExcelTransferResDTO> list = new ArrayList<>();
            for (TransferResDTO resDTO : transferResDTOList) {
                ExcelTransferResDTO res = new ExcelTransferResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setQuantity(String.valueOf(resDTO.getQuantity()));
                res.setTaxPrice(String.valueOf(resDTO.getTaxPrice()));
                res.setPreTaxPrice(String.valueOf(resDTO.getPreTaxPrice()));
                res.setTaxRate(String.valueOf(resDTO.getTaxRate()));
                res.setAssurancePeriod(String.valueOf(resDTO.getAssurancePeriod()));
                res.setLifeYears(String.valueOf(resDTO.getLifeYears()));
                res.setUseCounter(String.valueOf(resDTO.getUseCounter()));
                res.setOrderAmt(String.valueOf(resDTO.getOrderAmt()));
                res.setOwnerSupplyQuantity(String.valueOf(resDTO.getOwnerSupplyQuantity()));
                res.setOwnerSupplyPrice(String.valueOf(resDTO.getOwnerSupplyPrice()));
                res.setOwnerSupplyTaxPrice(String.valueOf(resDTO.getOwnerSupplyTaxPrice()));
                res.setInstallAmt(String.valueOf(resDTO.getInstallAmt()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备移交-建设工程信息", list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void encodingTransfer(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                TransferResDTO transferResDTO = transferMapper.getTransferDetail(id);
                if (Objects.isNull(transferResDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                Pattern pattern = RegularUtils.getNumberPattern();
                boolean matches = pattern.matcher(transferResDTO.getQuantity().toString()).matches();
                if (!matches) {
                    throw new CommonException(ErrorCode.TRANSFER_QUANTITY_ERROR, transferResDTO.getTransferNo());
                }
                if (CommonConstants.TWENTY_STRING.equals(transferResDTO.getEamProcessStatus())) {
                    throw new CommonException(ErrorCode.TRANSFER_HAS_HANDLE, transferResDTO.getTransferNo());
                }
                CurrentLoginUser user = TokenUtil.getCurrentPerson();
                for (int i = 0; i < transferResDTO.getQuantity(); i++) {
                    String unitNo = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(1)) + 1);
                    String equipCode = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(4)) + 1);
                    UnitCodeReqDTO unitCodeReqDTO = new UnitCodeReqDTO();
                    unitCodeReqDTO.setRecId(TokenUtil.getUuId());
                    unitCodeReqDTO.setUnitNo(unitNo);
                    unitCodeReqDTO.setRecCreator(user.getPersonId());
                    unitCodeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    unitCodeReqDTO.setDevNo(equipCode);
                    unitCodeReqDTO.setBatchNo(" ");
                    unitCodeReqDTO.setAssetNo(" ");
                    unitCodeReqDTO.setSupplierId(user.getOfficeAreaId());
                    unitCodeReqDTO.setSupplierName(user.getNames());
                    unitCodeReqDTO.setOrderNo(transferResDTO.getOrderNo());
                    unitCodeReqDTO.setOrderName(transferResDTO.getOrderName());
                    unitCodeReqDTO.setProCode(" ");
                    unitCodeReqDTO.setProName(" ");
                    unitCodeReqDTO.setMatSpecifi(transferResDTO.getMatSpecifi());
                    unitCodeReqDTO.setBrand(transferResDTO.getBrand());
                    equipmentMapper.insertUnitCode(unitCodeReqDTO);
                    insertEquipment(transferResDTO, unitNo, user);
                    transferResDTO.setEamProcessStatus("20");
                    transferMapper.updateTransfer(transferResDTO);
                }
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public Page<EquipmentResDTO> pageSplitTransfer(String sourceRecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
        if (sourceRecId == null) {
            sourceRecId = "flag";
        }
        equipmentReqDTO.setSourceRecId(sourceRecId);
        List<EquipmentResDTO> list = equipmentMapper.siftEquipment(equipmentReqDTO);
        return new Page<EquipmentResDTO>().setRecords(list);
    }

    @Override
    public EquipmentResDTO getSplitTransferDetail(String id) {
        return equipmentMapper.getSplitTransferDetail(id);
    }

    @Override
    public void saveSplitTransfer(EquipmentResDTO equipmentResDTO) {
        if (Objects.isNull(equipmentResDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!CommonConstants.TEN_STRING.equals(equipmentResDTO.getApprovalStatus())) {
            throw new CommonException(ErrorCode.TRANSFER_SPLIT_ERROR);
        }
        updateTransfer(equipmentResDTO);
    }

    @Override
    public void submitSplitTransfer(TransferSplitReqDTO transferSplitReqDTO) throws Exception {
        // ServiceDMDM0103 submit
        if (transferSplitReqDTO.getEquipmentList() != null && !transferSplitReqDTO.getEquipmentList().isEmpty()) {
            for (EquipmentResDTO resDTO : transferSplitReqDTO.getEquipmentList()) {
                if (StringUtils.isBlank(resDTO.getStartUseDate()) || StringUtils.isBlank(resDTO.getSystemCode()) || StringUtils.isBlank(resDTO.getEquipTypeCode())) {
                    throw new CommonException(ErrorCode.REQUIRED_NULL, "设备" + resDTO.getEquipCode());
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getApprovalStatus())) {
                    throw new CommonException(ErrorCode.TRANSFER_SPLIT_ERROR);
                }
            }
            List<Map<String, String>> list = new ArrayList<>();
            for (int j = 0; j < transferSplitReqDTO.getEquipmentList().size(); j++) {
                EquipmentResDTO resDTO = transferSplitReqDTO.getEquipmentList().get(j);
                String sourceRecId = resDTO.getSourceRecId();
                if (StringUtils.isNotEmpty(sourceRecId.trim())) {
                    EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
                    equipmentReqDTO.setSourceRecId(sourceRecId);
                    equipmentReqDTO.setApprovalStatus("10");
                    List<EquipmentResDTO> queryState = equipmentMapper.siftEquipment(equipmentReqDTO);
                    if (queryState == null || queryState.size() <= 0) {
                        overTodoService.overTodo(sourceRecId, "");
                    }
                    resDTO.setApprovalStatus("30");
                    buildPart(resDTO);
                    updateTransfer(resDTO);
                }
                Map<String, String> map = new HashMap<>();
                if (transferSplitReqDTO.getType() == 1) {
                    map.put("recId", resDTO.getRecId());
                    map.put("equipmentId", resDTO.getEquipCode());
                    map.put("equipmentCname", resDTO.getEquipName());
                    map.put("quantityActual", resDTO.getQuantity().toString());
                    map.put("remark", resDTO.getRemark());
                    map.put("recId2", resDTO.getSourceRecId());
                    map.put("ext1", resDTO.getStartUseDate());
                } else if (transferSplitReqDTO.getType() == 2) {
//                    if (j == 0) {
//                        Map<String, String> query16Map = new HashMap<>();
//                        query16Map.put("recId", resDTO.getSourceRecId());
//                        query16 = this.dao.query("DMDM16.query", query16Map, 0, -999999);
//                    }
//                    map.put("unitCode", dmdm01.getEquipCode());
//                    map.put("assetCname", dmdm01.getEquipName());
//                    map.put("stockUnit", dmdm01.getMeasureUnit());
//                    map.put("costPrice", String.valueOf(((DMDM16)query16.get(0)).getCostPrice()));
//                    map.put("costTotal", String.valueOf(((DMDM16)query16.get(0)).getCostTotal()));
//                    map.put("mangGrit", ((DMDM16)query16.get(0)).getMgtFineness());
//                    map.put("assetNature", ((DMDM16)query16.get(0)).getAssetNature());
//                    map.putAll(dmdm01.toMap());
                }
                list.add(map);
            }
            if (list.size() > 0) {
                if (transferSplitReqDTO.getType() == CommonConstants.ONE) {
                    getOsbSend(list);
                } else if (transferSplitReqDTO.getType() == CommonConstants.TWO) {
                    // todo ServiceDMDM0103 sendOSBbase
                    String url = "";
                    sendOsbBase(list, "EAM-MT-01", url);
                }
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportSplitTransfer(String sourceRecId, HttpServletResponse response) throws IOException {
        EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
        if (sourceRecId == null) {
            sourceRecId = "flag";
        }
        equipmentReqDTO.setSourceRecId(sourceRecId);
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.siftEquipment(equipmentReqDTO);
        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
            List<ExcelEquipmentResDTO> list = new ArrayList<>();
            for (EquipmentResDTO resDTO : equipmentResDTOList) {
                ExcelEquipmentResDTO res = new ExcelEquipmentResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setQuantity(String.valueOf(resDTO.getQuantity()));
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备拆分信息", list);
        }
    }

    /**
     * 新增设备台账
     * @param transferResDTO 设备移交类对象
     * @param unitNo 合一码
     * @param user token解析用户
     */
    public void insertEquipment(TransferResDTO transferResDTO, String unitNo, CurrentLoginUser user) {
        EquipmentReqDTO equipmentReqDTO = new EquipmentReqDTO();
        BeanUtils.copyProperties(transferResDTO, equipmentReqDTO);
        equipmentReqDTO.setSystemCode(" ");
        equipmentReqDTO.setSystemName(" ");
        equipmentReqDTO.setEquipTypeCode(" ");
        equipmentReqDTO.setEquipTypeName(" ");
        equipmentReqDTO.setUseLineNo(transferResDTO.getLineNo());
        equipmentReqDTO.setUseLineName(transferResDTO.getLineName());
        equipmentReqDTO.setUseSegNo(transferResDTO.getLineSubNo());
        equipmentReqDTO.setUseSegName(transferResDTO.getLineSubName());
        equipmentReqDTO.setEquipCode(unitNo);
        List<EquipmentCategoryResDTO> listSys = equipmentCategoryMapper.getChildEquipmentCategory(transferResDTO.getMajorCode());
        if (listSys != null && listSys.size() == 1) {
            equipmentReqDTO.setSystemCode(listSys.get(0).getNodeCode());
            equipmentReqDTO.setSystemName(listSys.get(0).getNodeName());
            List<EquipmentCategoryResDTO> listEquipType = equipmentCategoryMapper.getChildEquipmentCategory(equipmentReqDTO.getSystemCode());
            if (listEquipType != null && listEquipType.size() == 1) {
                equipmentReqDTO.setEquipTypeCode(listEquipType.get(0).getNodeCode());
                equipmentReqDTO.setEquipTypeName(listEquipType.get(0).getNodeName());
            }
        }
        equipmentReqDTO.setRecId(TokenUtil.getUuId());
        equipmentReqDTO.setEquipName(transferResDTO.getItemName());
        equipmentReqDTO.setApprovalStatus("10");
        equipmentReqDTO.setSpecialEquipFlag("10");
        equipmentReqDTO.setSourceAppNo(transferResDTO.getTransferNo());
        equipmentReqDTO.setSourceSubNo(transferResDTO.getItemCode());
        equipmentReqDTO.setSourceRecId(transferResDTO.getRecId());
        equipmentReqDTO.setRecCreator(user.getPersonId());
        equipmentReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentReqDTO.setQuantity(new BigDecimal("1"));
        equipmentReqDTO.setCompanyCode(user.getCompanyAreaId() == null ? user.getCompanyId() : user.getCompanyAreaId());
        equipmentReqDTO.setCompanyName(user.getCompanyName());
        equipmentReqDTO.setDeptCode(user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId());
        equipmentReqDTO.setDeptName(user.getOfficeName());
        if (transferResDTO.getSupplierId() != null && S.equals(transferResDTO.getItemCode().substring(0, 1))) {
            equipmentReqDTO.setBomType(transferResDTO.getItemCode());
        }
        equipmentReqDTO.setExt5(transferResDTO.getVendorCode());
        equipmentReqDTO.setEquipStatus(" ");
        equipmentReqDTO.setSourceKind(" ");
        equipmentReqDTO.setStartUseDate(" ");
        equipmentReqDTO.setInAccountTime(" ");
        equipmentReqDTO.setInstallDealer(" ");
        equipmentReqDTO.setDocId(" ");
        equipmentReqDTO.setOriginLineNo(" ");
        equipmentReqDTO.setOriginLineName(" ");
        equipmentReqDTO.setOriginSegNo(" ");
        equipmentReqDTO.setOriginSegName(" ");
        equipmentReqDTO.setRecStatus(" ");
        equipmentMapper.insertEquipment(equipmentReqDTO);
    }

    public void updateTransfer(EquipmentResDTO resDTO) {
        resDTO.setSpecialEquipFlag("10");
        List<EquipmentCategoryResDTO> msgs = equipmentCategoryMapper.listEquipmentCategory(null, resDTO.getEquipTypeCode(), null);
        if (msgs != null && msgs.size() > 0 && CommonConstants.TWENTY_STRING.equals(msgs.get(0).getExt1().trim())) {
            resDTO.setSpecialEquipFlag("20");
        }
        resDTO.setMajorName(equipmentCategoryMapper.getIndexByIndex(resDTO.getMajorCode(), null, null).getNodeName());
        resDTO.setSystemName(equipmentCategoryMapper.getIndexByIndex(resDTO.getSystemCode(), null, null).getNodeName());
        resDTO.setEquipTypeName(equipmentCategoryMapper.getIndexByIndex(resDTO.getEquipTypeCode(), null, null).getNodeName());
        EquipmentReqDTO equipmentReqDTO = new EquipmentReqDTO();
        BeanUtils.copyProperties(resDTO, equipmentReqDTO);
        equipmentReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        equipmentReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        equipmentMapper.updateEquipment(equipmentReqDTO);
    }

    public void buildPart(EquipmentResDTO resDTO) {
        List<Bom> boms = transferMapper.queryBomTree(resDTO.getBomType());
        if (boms != null && boms.size() > 0) {
            for (Bom bom : boms) {
                if (bom.getQuantity() != null) {
                    for (int i = 0; i < bom.getQuantity().intValue(); i++) {
                        EquipmentPartReqDTO equipmentPartReqDTO = new EquipmentPartReqDTO();
                        equipmentPartReqDTO.setRecId(TokenUtil.getUuId());
                        equipmentPartReqDTO.setEquipCode(resDTO.getEquipCode());
                        equipmentPartReqDTO.setEquipName(resDTO.getEquipName());
                        equipmentPartReqDTO.setPartCode(CodeUtils.getNextCode(equipmentPartMapper.getMaxPartCode(), 1));
                        equipmentPartReqDTO.setPartName(bom.getCname());
                        equipmentPartReqDTO.setBomEname(bom.getEname());
                        equipmentPartReqDTO.setInAccountTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                        equipmentPartReqDTO.setDeleteFlag(" ");
                        equipmentPartReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                        equipmentPartReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                        equipmentPartReqDTO.setRecStatus("0");
                        equipmentPartReqDTO.setEquipStatus("10");
                        equipmentPartReqDTO.setQuantity(new BigDecimal("1"));
                        equipmentPartMapper.insertEquipmentPart(equipmentPartReqDTO);
                        PartFaultReqDTO partFaultReqDTO = new PartFaultReqDTO();
                        BeanUtils.copyProperties(equipmentPartReqDTO, partFaultReqDTO);
                        partFaultReqDTO.setRecId(TokenUtil.getUuId());
                        partFaultReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                        partFaultReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                        partFaultReqDTO.setOperateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                        partFaultReqDTO.setLogType("10");
                        partFaultReqDTO.setRemark("设备移交的部件！");
                        partFaultMapper.insertPartFault(partFaultReqDTO);
                    }
                }
            }
        }
    }

    /**
     * 推送数据至OSB
     * ServiceDMDM0103 getOSBsend
     * @param list 推送的数据集合
     * @throws Exception
     */
    public void getOsbSend(List<Map<String, String>> list) throws Exception {
//        String methodName = "setData";
//        String method = "S_AT_09";
//        String systemName = "DM";
//        String url = ServiceDMDM4AT.path;
//        String json = JSONArray.fromObject(list).toString();
//        CurrentLoginUser user = TokenUtil.getCurrentPerson();
//        OSBGetMessage osbGetMessage = new OSBGetMessage();
//        String orgCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
//        String result = osbGetMessage.getMessage(method, user.getPersonId(), orgCode, systemName, json, url, methodName, "set", "Data", 0);
//        JSONObject jsonObject = JSONObject.fromObject(result);
//        if (!"0".equals(jsonObject.get("state"))) {
//            throw new Exception("接口调用失败!");
//        }
    }

    public void sendOsbBase(List<Map<String, String>> list, String method, String url) throws Exception {
//        String methodName = "setData";
//        String systemName = "DM";
//        String json = JSONArray.fromObject(list).toString();
//        String user = UserSession.getLoginName();
//        OSBGetMessage osbGetMessage = new OSBGetMessage();
//        String orgCode = UserFactory.getUserAllInfo().getUserInfo(UserSession.getLoginName()).getUserCoInfo().getOrgCode();
//        String result = osbGetMessage.getMessage(method, user, orgCode, systemName, json, url, methodName, "set", "Data", 0);
//        JSONObject jsonObject = JSONObject.fromObject(result);
//        if (!"0".equals(jsonObject.get("state"))) {
//            throw new Exception("接口调用失败!");
//        }
    }

}
