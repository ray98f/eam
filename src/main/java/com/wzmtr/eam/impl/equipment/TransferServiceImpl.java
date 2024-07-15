package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.util.BeanUtils;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentPartReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentSiftReqDTO;
import com.wzmtr.eam.dto.req.equipment.PartFaultReqDTO;
import com.wzmtr.eam.dto.req.equipment.TransferExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.TransferSplitReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.TransferResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelTransferResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Bom;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentPartMapper;
import com.wzmtr.eam.mapper.equipment.PartFaultMapper;
import com.wzmtr.eam.mapper.equipment.TransferMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.equipment.TransferService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.RegularUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public Page<TransferResDTO> pageTransfer(String transferNo, String itemCode, String itemName,
                                             String position1Code, String eamProcessStatus,
                                             String majorCode, String orderNo, String orderName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        return transferMapper.pageTransfer(pageReqDTO.of(), transferNo, itemCode, itemName, position1Code,
                eamProcessStatus, eamProcessStatus1, eamProcessStatus2, majorCode, orderNo, orderName);
    }

    @Override
    public TransferResDTO getTransferDetail(String id) {
        return transferMapper.getTransferDetail(id);
    }

    @Override
    public void exportTransfer(TransferExportReqDTO transferExportReqDTO, HttpServletResponse response) throws IOException {
        List<TransferResDTO> transferResDTOList = transferMapper.listTransfer(transferExportReqDTO);
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
                if (!RegularUtils.isValidNumber(transferResDTO.getQuantity().toString())) {
                    throw new CommonException(ErrorCode.TRANSFER_QUANTITY_ERROR, transferResDTO.getTransferNo());
                }
                if (CommonConstants.TWENTY_STRING.equals(transferResDTO.getEamProcessStatus())) {
                    throw new CommonException(ErrorCode.TRANSFER_HAS_HANDLE, transferResDTO.getTransferNo());
                }
                CurrentLoginUser user = TokenUtils.getCurrentPerson();
                for (int i = 0; i < transferResDTO.getQuantity(); i++) {
                    String maxCode = equipmentMapper.getMaxCode();
                    String equipCode;
                    if (StringUtils.isNotEmpty(maxCode)) {
                        equipCode = CodeUtils.getNextCode(maxCode, 2);
                    } else {
                        equipCode = CommonConstants.INIT_EQUIPMENT_CODE;
                    }
                    insertEquipment(transferResDTO, equipCode, user);
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
        EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
        if (sourceRecId == null) {
            sourceRecId = "flag";
        }
        equipmentReqDTO.setSourceRecId(sourceRecId);
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.pageSiftEquipment(pageReqDTO.of(), equipmentReqDTO);
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
        if (equipmentResDTO.getUseLineName().contains(CommonConstants.S1)) {
            equipmentResDTO.setUseLineNo(CommonConstants.LINE_CODE_ONE);
        } else {
            equipmentResDTO.setUseLineNo(CommonConstants.LINE_CODE_TWO);
        }
        updateTransfer(equipmentResDTO);
    }

    @Override
    public void submitSplitTransfer(TransferSplitReqDTO transferSplitReqDTO) throws Exception {
        // ServiceDMDM0103 submit
        if (transferSplitReqDTO.getEquipmentList() != null && !transferSplitReqDTO.getEquipmentList().isEmpty()) {
            for (EquipmentResDTO resDTO : transferSplitReqDTO.getEquipmentList()) {
                if (org.apache.commons.lang3.StringUtils.isBlank(resDTO.getStartUseDate())
                        || org.apache.commons.lang3.StringUtils.isBlank(resDTO.getSystemCode())
                        || org.apache.commons.lang3.StringUtils.isBlank(resDTO.getEquipTypeCode())) {
                    throw new CommonException(ErrorCode.REQUIRED_NULL, "设备" + resDTO.getEquipCode());
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getApprovalStatus())) {
                    throw new CommonException(ErrorCode.TRANSFER_SPLIT_ERROR);
                }
            }
            List<Map<String, String>> list = new ArrayList<>();
            for (EquipmentResDTO resDTO : transferSplitReqDTO.getEquipmentList()) {
                String sourceRecId = resDTO.getSourceRecId();
                if (StringUtils.isNotEmpty(sourceRecId.trim())) {
                    EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
                    equipmentReqDTO.setSourceRecId(sourceRecId);
                    equipmentReqDTO.setApprovalStatus("10");
                    List<EquipmentResDTO> queryState = equipmentMapper.siftEquipment(equipmentReqDTO);
                    if (StringUtils.isEmpty(queryState)) {
                        overTodoService.overTodo(sourceRecId, "", CommonConstants.ONE_STRING);
                    }
                    resDTO.setApprovalStatus("30");
                    buildPart(resDTO);
                    updateTransfer(resDTO);
                }
                Map<String, String> map = getMap(transferSplitReqDTO, resDTO);
                list.add(map);
            }
            if (StringUtils.isNotEmpty(list)) {
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

    /**
     * 设备拆分map拼装
     * @param transferSplitReqDTO 设备拆分信息
     * @param equipmentResDTO 设备信息
     * @return map
     */
    @NotNull
    private static Map<String, String> getMap(TransferSplitReqDTO transferSplitReqDTO, EquipmentResDTO equipmentResDTO) {
        Map<String, String> map = new HashMap<>();
        if (transferSplitReqDTO.getType() == 1) {
            map.put("recId", equipmentResDTO.getRecId());
            map.put("equipmentId", equipmentResDTO.getEquipCode());
            map.put("equipmentCname", equipmentResDTO.getEquipName());
            map.put("quantityActual", equipmentResDTO.getQuantity().toString());
            map.put("remark", equipmentResDTO.getRemark());
            map.put("recId2", equipmentResDTO.getSourceRecId());
            map.put("ext1", equipmentResDTO.getStartUseDate());
        } else if (transferSplitReqDTO.getType() == CommonConstants.TWO) {
//            if (j == 0) {
//                Map<String, String> query16Map = new HashMap<>();
//                query16Map.put("recId", equipmentResDTO.getSourceRecId());
//                query16 = this.dao.query("DMDM16.query", query16Map, 0, -999999);
//            }
//            map.put("unitCode", equipmentResDTO.getEquipCode());
//            map.put("assetCname", equipmentResDTO.getEquipName());
//            map.put("stockUnit", equipmentResDTO.getMeasureUnit());
//            map.put("costPrice", String.valueOf(((DMDM16)query16.get(0)).getCostPrice()));
//            map.put("costTotal", String.valueOf(((DMDM16)query16.get(0)).getCostTotal()));
//            map.put("mangGrit", ((DMDM16)query16.get(0)).getMgtFineness());
//            map.put("assetNature", ((DMDM16)query16.get(0)).getAssetNature());
//            map.putAll(dmdm01.toMap());
        }
        return map;
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
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                res.setSpecialEquipFlag(CommonConstants.TEN_STRING.equals(resDTO.getSpecialEquipFlag()) ? "非特殊设备" : "特殊设备");
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备拆分信息", list);
        }
    }

    /**
     * 新增设备台账
     * @param transferResDTO 设备移交类对象
     * @param equipCode 合一码
     * @param user token解析用户
     */
    public void insertEquipment(TransferResDTO transferResDTO, String equipCode, CurrentLoginUser user) {
        EquipmentReqDTO equipmentReqDTO = new EquipmentReqDTO();
        BeanUtils.copyProperties(transferResDTO, equipmentReqDTO);
        equipmentReqDTO.setSystemCode(CommonConstants.BLANK);
        equipmentReqDTO.setSystemName(CommonConstants.BLANK);
        equipmentReqDTO.setEquipTypeCode(CommonConstants.BLANK);
        equipmentReqDTO.setEquipTypeName(CommonConstants.BLANK);
        equipmentReqDTO.setUseLineNo(transferResDTO.getLineNo());
        equipmentReqDTO.setUseLineName(transferResDTO.getLineName());
        equipmentReqDTO.setUseSegNo(transferResDTO.getLineSubNo());
        equipmentReqDTO.setUseSegName(transferResDTO.getLineSubName());
        equipmentReqDTO.setEquipCode(equipCode);
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
        equipmentReqDTO.setRecId(TokenUtils.getUuId());
        equipmentReqDTO.setEquipName(transferResDTO.getItemName());
        equipmentReqDTO.setApprovalStatus("10");
        equipmentReqDTO.setSpecialEquipFlag("10");
        equipmentReqDTO.setOtherEquipFlag("10");
        equipmentReqDTO.setSourceAppNo(transferResDTO.getTransferNo());
        equipmentReqDTO.setSourceSubNo(transferResDTO.getItemCode());
        equipmentReqDTO.setSourceRecId(transferResDTO.getRecId());
        equipmentReqDTO.setRecCreator(user.getPersonId());
        equipmentReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        equipmentReqDTO.setQuantity(new BigDecimal("1"));
        equipmentReqDTO.setCompanyCode(user.getCompanyAreaId() == null ? user.getCompanyId() : user.getCompanyAreaId());
        equipmentReqDTO.setCompanyName(user.getCompanyName());
        equipmentReqDTO.setDeptCode(user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId());
        equipmentReqDTO.setDeptName(user.getOfficeName());
        if (transferResDTO.getSupplierId() != null && S.equals(transferResDTO.getItemCode().substring(0, 1))) {
            equipmentReqDTO.setBomType(transferResDTO.getItemCode());
        }
        equipmentReqDTO.setExt5(transferResDTO.getVendorCode());
        equipmentReqDTO.setEquipStatus(CommonConstants.BLANK);
        equipmentReqDTO.setSourceKind(CommonConstants.BLANK);
        equipmentReqDTO.setStartUseDate(CommonConstants.BLANK);
        equipmentReqDTO.setInAccountTime(CommonConstants.BLANK);
        equipmentReqDTO.setInstallDealer(CommonConstants.BLANK);
        equipmentReqDTO.setDocId(CommonConstants.BLANK);
        equipmentReqDTO.setOriginLineNo(" ");
        equipmentReqDTO.setOriginLineName(" ");
        equipmentReqDTO.setOriginSegNo(" ");
        equipmentReqDTO.setOriginSegName(" ");
        equipmentReqDTO.setRecStatus(" ");
        equipmentMapper.insertEquipment(equipmentReqDTO);
    }

    public void updateTransfer(EquipmentResDTO resDTO) {
        if (StringUtils.isEmpty(resDTO.getMajorCode())
                || StringUtils.isEmpty(resDTO.getSystemCode())
                || StringUtils.isEmpty(resDTO.getEquipTypeCode())) {
            throw new CommonException(ErrorCode.REQUIRED_NULL, "设备拆分" + resDTO.getEquipCode());
        }
        resDTO.setSpecialEquipFlag("10");
        List<EquipmentCategoryResDTO> msgList = equipmentCategoryMapper.listEquipmentCategory(null,
                resDTO.getEquipTypeCode(), null);
        if (StringUtils.isNotEmpty(msgList) && CommonConstants.TWENTY_STRING.equals(msgList.get(0).getExt1().trim())) {
            resDTO.setSpecialEquipFlag("20");
        }
        resDTO.setMajorName(equipmentCategoryMapper.getIndexByIndex(resDTO.getMajorCode(), null, null).getNodeName());
        resDTO.setSystemName(equipmentCategoryMapper.getIndexByIndex(resDTO.getSystemCode(), null, null).getNodeName());
        resDTO.setEquipTypeName(equipmentCategoryMapper.getIndexByIndex(resDTO.getEquipTypeCode(), null, null).getNodeName());
        EquipmentReqDTO equipmentReqDTO = new EquipmentReqDTO();
        BeanUtils.copyProperties(resDTO, equipmentReqDTO);
        equipmentReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        equipmentReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        equipmentMapper.updateEquipment(equipmentReqDTO);
    }

    public void buildPart(EquipmentResDTO resDTO) {
        if (StringUtils.isNotEmpty(resDTO.getBomType())) {
            List<Bom> bomList = transferMapper.queryBomTree(resDTO.getBomType());
            if (StringUtils.isNotEmpty(bomList)) {
                for (Bom bom : bomList) {
                    if (bom.getQuantity() != null) {
                        for (int i = 0; i < bom.getQuantity().intValue(); i++) {
                            EquipmentPartReqDTO equipmentPartReqDTO = new EquipmentPartReqDTO();
                            equipmentPartReqDTO.setRecId(TokenUtils.getUuId());
                            equipmentPartReqDTO.setEquipCode(resDTO.getEquipCode());
                            equipmentPartReqDTO.setEquipName(resDTO.getEquipName());
                            equipmentPartReqDTO.setPartCode(CodeUtils.getNextCode(equipmentPartMapper.getMaxPartCode(), 1));
                            equipmentPartReqDTO.setPartName(bom.getCname());
                            equipmentPartReqDTO.setBomEname(bom.getEname());
                            equipmentPartReqDTO.setInAccountTime(DateUtils.getCurrentTime());
                            equipmentPartReqDTO.setDeleteFlag(" ");
                            equipmentPartReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                            equipmentPartReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                            equipmentPartReqDTO.setRecStatus("0");
                            equipmentPartReqDTO.setEquipStatus("10");
                            equipmentPartReqDTO.setQuantity(new BigDecimal("1"));
                            equipmentPartMapper.insertEquipmentPart(equipmentPartReqDTO);
                            PartFaultReqDTO partFaultReqDTO = new PartFaultReqDTO();
                            BeanUtils.copyProperties(equipmentPartReqDTO, partFaultReqDTO);
                            partFaultReqDTO.setRecId(TokenUtils.getUuId());
                            partFaultReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                            partFaultReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                            partFaultReqDTO.setOperateTime(DateUtils.getCurrentTime());
                            partFaultReqDTO.setLogType("10");
                            partFaultReqDTO.setRemark("设备移交的部件！");
                            partFaultMapper.insertPartFault(partFaultReqDTO);
                        }
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
//        CurrentLoginUser user = TokenUtils.getCurrentPerson();
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
