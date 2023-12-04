package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.util.BeanUtils;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.*;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.TransferResDTO;
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
        if (eamProcessStatus != null && !"".equals(eamProcessStatus)) {
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
                               String majorCode, String orderNo, String orderName, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "移交单号", "合同清单明细号", "合同清单明细名称", "线别代码", "线别名称", "线段代码", "线段名称",
                "专业代码", "专业名称", "项目编号", "项目名称", "合同编号", "合同名称", "供应商编号", "供应商名称", "型号规格", "品牌/生产厂家", "其他特征参数",
                "图号/国标号/厂家零件号", "单位", "数量（默认值0）", "是组合件", "是隐蔽工程", "税后单价（默认值0）", "税前单价（默认值0）", "税率（默认值0）",
                "有技术资料", "工程质保期（天）（默认值0）", "备注", "使用年限N3（默认值0）", "使用次数N8（默认值0）", "生产厂家", "供货商", "位置1代码",
                "位置1", "位置2代码", "位置2", "位置3", "位置补充说明", "合同单价（默认值0）", "结构形式", "结构净高（默认值0）", "是否使用甲供料",
                "甲供料数量（默认值0）", "甲供料单位", "甲供料价格（默认值0）", "甲供料含税价格（默认值0）", "甲供料合同", "安装费（默认值0）",
                "审核意见", "状态（新增、监理审核、业主审核、专工审核、监理驳回、业主驳回、专工驳回、完成）", "合同清单主键", "创建者", "创建时间",
                "修改者", "修改时间", "删除者", "删除时间", "删除标志", "归档标记", "扩展字段1", "扩展字段2", "扩展字段3", "扩展字段4", "扩展字段5",
                "扩展字段6", "是否进设备台账（0：是；1：否）", "EAM处理状态");
        List<TransferResDTO> transferResDTOList = transferMapper.listTransfer(transferNo, itemCode, itemName, position1Code, eamProcessStatus,
                majorCode, orderNo, orderName);
        List<Map<String, String>> list = new ArrayList<>();
        if (transferResDTOList != null && !transferResDTOList.isEmpty()) {
            for (TransferResDTO resDTO : transferResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("移交单号", resDTO.getTransferNo());
                map.put("合同清单明细号", resDTO.getItemCode());
                map.put("合同清单明细名称", resDTO.getItemName());
                map.put("线别代码", resDTO.getLineNo());
                map.put("线别名称", resDTO.getLineName());
                map.put("线段代码", resDTO.getLineSubNo());
                map.put("线段名称", resDTO.getLineSubName());
                map.put("专业代码", resDTO.getMajorCode());
                map.put("专业名称", resDTO.getMajorName());
                map.put("项目编号", resDTO.getProCode());
                map.put("项目名称", resDTO.getProName());
                map.put("合同编号", resDTO.getOrderNo());
                map.put("合同名称", resDTO.getOrderName());
                map.put("供应商编号", resDTO.getSupplierId());
                map.put("供应商名称", resDTO.getSupplierName());
                map.put("型号规格", resDTO.getMatSpecifi());
                map.put("品牌/生产厂家", resDTO.getBrand());
                map.put("其他特征参数", resDTO.getOtherFeature());
                map.put("图号/国标号/厂家零件号", resDTO.getAppendixNo());
                map.put("单位", resDTO.getStockUnit());
                map.put("数量（默认值0）", String.valueOf(resDTO.getQuantity()));
                map.put("是组合件", resDTO.getIsBom());
                map.put("是隐蔽工程", resDTO.getIsInvisible());
                map.put("税后单价（默认值0）", String.valueOf(resDTO.getTaxPrice()));
                map.put("税前单价（默认值0）", String.valueOf(resDTO.getPreTaxPrice()));
                map.put("税率（默认值0）", String.valueOf(resDTO.getTaxRate()));
                map.put("有技术资料", resDTO.getIsDoc());
                map.put("工程质保期（天）（默认值0）", String.valueOf(resDTO.getAssurancePeriod()));
                map.put("备注", resDTO.getRemark());
                map.put("使用年限N3（默认值0）", String.valueOf(resDTO.getLifeYears()));
                map.put("使用次数N8（默认值0）", String.valueOf(resDTO.getUseCounter()));
                map.put("生产厂家", resDTO.getManufacture());
                map.put("供货商", resDTO.getVendorCode());
                map.put("位置1代码", resDTO.getPosition1Code());
                map.put("位置1", resDTO.getPosition1Name());
                map.put("位置2代码", resDTO.getPosition2Code());
                map.put("位置2", resDTO.getPosition2Name());
                map.put("位置3", resDTO.getPosition3());
                map.put("位置补充说明", resDTO.getPositionRemark());
                map.put("合同单价（默认值0）", String.valueOf(resDTO.getOrderAmt()));
                map.put("结构形式", resDTO.getConstructureForm());
                map.put("结构净高（默认值0）", resDTO.getNetHeight());
                map.put("是否使用甲供料", resDTO.getIsOwnerSupply());
                map.put("甲供料数量（默认值0）", String.valueOf(resDTO.getOwnerSupplyQuantity()));
                map.put("甲供料单位", resDTO.getOwnerSupplyUnit());
                map.put("甲供料价格（默认值0）", String.valueOf(resDTO.getOwnerSupplyPrice()));
                map.put("甲供料含税价格（默认值0）", String.valueOf(resDTO.getOwnerSupplyTaxPrice()));
                map.put("甲供料合同", resDTO.getOwnerSupplyOrder());
                map.put("安装费（默认值0）", String.valueOf(resDTO.getInstallAmt()));
                map.put("审核意见", resDTO.getAuditMsg());
                map.put("状态（新增、监理审核、业主审核、专工审核、监理驳回、业主驳回、专工驳回、完成）", resDTO.getStatus());
                map.put("合同清单主键", resDTO.getRecId2());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                map.put("修改者", resDTO.getRecRevisor());
                map.put("修改时间", resDTO.getRecReviseTime());
                map.put("删除者", resDTO.getRecDeletor());
                map.put("删除时间", resDTO.getRecDeleteTime());
                map.put("删除标志", resDTO.getDeleteFlag());
                map.put("归档标记", resDTO.getArchiveFlag());
                map.put("扩展字段1", resDTO.getExt1());
                map.put("扩展字段2", resDTO.getExt2());
                map.put("扩展字段3", resDTO.getExt3());
                map.put("扩展字段4", resDTO.getExt4());
                map.put("扩展字段5", resDTO.getExt5());
                map.put("扩展字段6", resDTO.getExt6());
                map.put("是否进设备台账（0：是；1：否）", resDTO.getIsEquipment());
                map.put("EAM处理状态", resDTO.getEamProcessStatus());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备移交-建设工程信息", listName, list, null, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void encodingTransfer(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
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
                    unitCodeReqDTO.setSupplierId(user.getOfficeId());
                    unitCodeReqDTO.setSupplierName(user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId());
                    unitCodeReqDTO.setOrderNo(transferResDTO.getOrderNo());
                    unitCodeReqDTO.setOrderName(transferResDTO.getOrderName());
                    unitCodeReqDTO.setProCode(" ");
                    unitCodeReqDTO.setProName(" ");
                    unitCodeReqDTO.setMatSpecifi(transferResDTO.getMatSpecifi());
                    unitCodeReqDTO.setBrand(transferResDTO.getBrand());
                    equipmentMapper.insertUnitCode(unitCodeReqDTO);
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
                    if (transferResDTO.getSupplierId() != null && "S".equals(transferResDTO.getItemCode().substring(0, 1))) {
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
                if (sourceRecId != null && "".equals(sourceRecId.trim())) {
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
                if (transferSplitReqDTO.getType() == 1) {
                    getOsbSend(list);
                } else if (transferSplitReqDTO.getType() == 2) {
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
    public void exportSplitTransfer(String sourceRecId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "公司代码", "公司名称", "部门代码", "部门名称", "设备编码", "设备名称", "专业代码", "专业名称",
                "系统代码", "系统名称", "设备分类代码", "设备分类名称", "特种设备标识", "BOM类型", "生产厂家", "合同号", "合同名称", "型号规格", "品牌",
                "出厂日期", "出厂编号", "开始使用日期", "数量", "进设备台帐时间", "设备状态", "来源单号", "来源明细号", "来源记录编号", "安装单位", "附件编号",
                "来源线别代码", "来源线别名称", "来源线段代码", "来源线段名称", "应用线别代码", "应用线别", "应用线段代码", "应用线段", "位置一", "位置一名称",
                "位置二", "位置二名称", "位置三", "位置补充说明", "走行里程", "备注", "审批状态", "特种设备检测日期", "特种设备检测有效期", "创建者", "创建时间",
                "修改者", "修改时间", "删除者", "删除时间", "删除标志", "归档标记", "记录状态");
        EquipmentSiftReqDTO equipmentReqDTO = new EquipmentSiftReqDTO();
        if (sourceRecId == null) {
            sourceRecId = "flag";
        }
        equipmentReqDTO.setSourceRecId(sourceRecId);
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.siftEquipment(equipmentReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
            for (EquipmentResDTO equipmentResDTO : equipmentResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", equipmentResDTO.getRecId());
                map.put("公司代码", equipmentResDTO.getCompanyCode());
                map.put("公司名称", equipmentResDTO.getCompanyName());
                map.put("部门代码", equipmentResDTO.getDeptCode());
                map.put("部门名称", equipmentResDTO.getDeptName());
                map.put("设备编码", equipmentResDTO.getEquipCode());
                map.put("设备名称", equipmentResDTO.getEquipName());
                map.put("专业代码", equipmentResDTO.getMajorCode());
                map.put("专业名称", equipmentResDTO.getMajorName());
                map.put("系统代码", equipmentResDTO.getSystemCode());
                map.put("系统名称", equipmentResDTO.getSystemName());
                map.put("设备分类代码", equipmentResDTO.getEquipTypeCode());
                map.put("设备分类名称", equipmentResDTO.getEquipTypeName());
                map.put("特种设备标识", equipmentResDTO.getSpecialEquipFlag());
                map.put("BOM类型", equipmentResDTO.getBomType());
                map.put("生产厂家", equipmentResDTO.getManufacture());
                map.put("合同号", equipmentResDTO.getOrderNo());
                map.put("合同名称", equipmentResDTO.getOrderName());
                map.put("型号规格", equipmentResDTO.getMatSpecifi());
                map.put("品牌", equipmentResDTO.getBrand());
                map.put("出厂日期", equipmentResDTO.getManufactureDate());
                map.put("出厂编号", equipmentResDTO.getManufactureNo());
                map.put("开始使用日期", equipmentResDTO.getStartUseDate());
                map.put("数量", String.valueOf(equipmentResDTO.getQuantity()));
                map.put("进设备台帐时间", equipmentResDTO.getManufactureNo());
                map.put("设备状态", equipmentResDTO.getEquipStatus());
                map.put("来源单号", equipmentResDTO.getSourceAppNo());
                map.put("来源明细号", equipmentResDTO.getSourceSubNo());
                map.put("来源记录编号", equipmentResDTO.getSourceRecId());
                map.put("安装单位", equipmentResDTO.getInstallDealer());
                map.put("附件编号", equipmentResDTO.getDocId());
                map.put("来源线别代码", equipmentResDTO.getOriginLineNo());
                map.put("来源线别名称", equipmentResDTO.getOriginLineName());
                map.put("来源线段代码", equipmentResDTO.getOriginSegNo());
                map.put("来源线段名称", equipmentResDTO.getOriginSegName());
                map.put("应用线别代码", equipmentResDTO.getUseLineNo());
                map.put("应用线别", equipmentResDTO.getUseLineName());
                map.put("应用线段代码", equipmentResDTO.getUseSegNo());
                map.put("应用线段", equipmentResDTO.getUseSegName());
                map.put("位置一", equipmentResDTO.getPosition1Code());
                map.put("位置一名称", equipmentResDTO.getPosition1Name());
                map.put("位置二", equipmentResDTO.getPosition2Code());
                map.put("位置二名称", equipmentResDTO.getPosition2Name());
                map.put("位置三", equipmentResDTO.getPosition3());
                map.put("位置补充说明", equipmentResDTO.getPositionRemark());
                map.put("走行里程", String.valueOf(equipmentResDTO.getTotalMiles()));
                map.put("备注", equipmentResDTO.getRemark());
                map.put("审批状态", equipmentResDTO.getApprovalStatus());
                map.put("特种设备检测日期", equipmentResDTO.getVerifyDate());
                map.put("特种设备检测有效期", equipmentResDTO.getVerifyValidityDate());
                map.put("创建者", equipmentResDTO.getRecCreator());
                map.put("创建时间", equipmentResDTO.getRecCreateTime());
                map.put("修改者", equipmentResDTO.getRecRevisor());
                map.put("修改时间", equipmentResDTO.getRecReviseTime());
                map.put("删除者", equipmentResDTO.getRecDeletor());
                map.put("删除时间", equipmentResDTO.getRecDeleteTime());
                map.put("删除标志", equipmentResDTO.getDeleteFlag());
                map.put("归档标记", equipmentResDTO.getArchiveFlag());
                map.put("记录状态", equipmentResDTO.getRecStatus());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备拆分信息", listName, list, null, response);
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

    // ServiceDMDM0103 getOSBsend
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
