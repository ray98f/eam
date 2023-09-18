package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.mapper.mea.SubmissionRecordMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.mea.SubmissionRecordService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class SubmissionRecordServiceImpl implements SubmissionRecordService {

    @Autowired
    private SubmissionRecordMapper submissionRecordMapper;

    @Autowired
    private MeaMapper meaMapper;

    @Autowired
    private BpmnService bpmnService;

    @Override
    public Page<SubmissionRecordResDTO> pageSubmissionRecord(String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionRecordMapper.pageSubmissionRecord(pageReqDTO.of(), checkNo, instrmPlanNo, recStatus, workFlowInstId);
    }

    @Override
    public SubmissionRecordResDTO getSubmissionRecordDetail(String id) {
        return submissionRecordMapper.getSubmissionRecordDetail(id);
    }

    @Override
    public void addSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) {
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String recCreator = TokenUtil.getCurrentPersonId();
        CurrentLoginUser user = TokenUtil.getCurrentPerson();
        String editDeptCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
        String recCreateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        String archiveFlag = "0";
        String recStatus = "10";
        String checkNo = submissionRecordMapper.getMaxCode();
        if (checkNo == null || "".equals(checkNo) || !("20" + checkNo.substring(2, 8)).equals(day.format(System.currentTimeMillis()))) {
            checkNo = "JJ" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            checkNo = CodeUtils.getNextCode(checkNo, 8);
        }
        submissionRecordReqDTO.setRecId(TokenUtil.getUuId());
        submissionRecordReqDTO.setCheckNo(checkNo);
        submissionRecordReqDTO.setRecCreator(recCreator);
        submissionRecordReqDTO.setRecCreateTime(recCreateTime);
        submissionRecordReqDTO.setRecStatus(recStatus);
        submissionRecordReqDTO.setArchiveFlag(archiveFlag);
        submissionRecordReqDTO.setEditDeptCode(editDeptCode);
        submissionRecordMapper.addSubmissionRecord(submissionRecordReqDTO);
    }

    @Override
    public void modifySubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) {
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(submissionRecordReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!"10".equals(res.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        submissionRecordReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        submissionRecordReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionRecordMapper.modifySubmissionRecord(submissionRecordReqDTO);
    }

    @Override
    public void deleteSubmissionRecord(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!"10".equals(res.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                submissionRecordMapper.deleteSubmissionRecordDetail(null, res.getRecId(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                submissionRecordMapper.deleteSubmissionRecord(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) throws Exception {
        // ServiceDMAM0301 submit
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(submissionRecordReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!"10".equals(res.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "提交");
        } else {
            List<SubmissionRecordDetailResDTO> result = submissionRecordMapper.listSubmissionRecordDetail(res.getRecId());
            if (result.size() == 0) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "此定检记录不存在计划明细，无法提交");
            }
            String processId = bpmnService.commit(res.getCheckNo(), BpmnFlowEnum.SUBMISSION_RECORD_SUBMIT.value(), null, null);
            if (processId == null || "-1".equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            SubmissionRecordReqDTO reqDTO = new SubmissionRecordReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus("已提交");
            reqDTO.setRecStatus("20");
            submissionRecordMapper.modifySubmissionRecord(reqDTO);
        }
    }

    @Override
    public void exportSubmissionRecord(String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "检定记录号", "检测校准单位", "检定记录状态", "附件", "备注");
        List<SubmissionRecordResDTO> checkPlanList = submissionRecordMapper.listSubmissionRecord(null, checkNo, instrmPlanNo, recStatus, workFlowInstId);
        List<Map<String, String>> list = new ArrayList<>();
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            for (SubmissionRecordResDTO resDTO : checkPlanList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("检定记录号", resDTO.getCheckNo());
                map.put("检测校准单位", resDTO.getVerifyDept());
                map.put("检定记录状态", resDTO.getRecStatus());
                map.put("附件", resDTO.getExt1());
                map.put("备注", resDTO.getVerifyNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("定检计划信息", listName, list, null, response);
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(String testRecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionRecordMapper.pageSubmissionRecordDetail(pageReqDTO.of(), testRecId);
    }

    @Override
    public SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id) {
        return submissionRecordMapper.getSubmissionRecordDetailDetail(id);
    }

    @Override
    public void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        submissionRecordDetailReqDTO.setRecId(TokenUtil.getUuId());
        submissionRecordDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        submissionRecordDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionRecordDetailReqDTO.setArchiveFlag("0");
        MeaResDTO meaResDTO = new MeaResDTO();
        meaResDTO.setLastVerifyDate(submissionRecordDetailReqDTO.getLastVerifyDate());
        meaResDTO.setNextVerifyDate(submissionRecordDetailReqDTO.getNextVerifyDate());
        meaResDTO.setEquipName(submissionRecordDetailReqDTO.getEquipName());
        meaResDTO.setCertificateNo(submissionRecordDetailReqDTO.getVerificationNo());
        meaResDTO.setMeasureBarcode(submissionRecordDetailReqDTO.getMeasureBarcode());
        meaResDTO.setVerifyPeriod(Integer.valueOf(submissionRecordDetailReqDTO.getVerifyPeriod()));
        meaResDTO.setVerifyDept(submissionRecordDetailReqDTO.getVerifyDept());
        meaResDTO.setEquipCode(submissionRecordDetailReqDTO.getEquipCode());
        meaResDTO.setRecCreateTime(submissionRecordDetailReqDTO.getRecCreateTime());
        meaResDTO.setRecCreator(submissionRecordDetailReqDTO.getRecCreator());
        meaMapper.updateone(meaResDTO);
        submissionRecordMapper.addSubmissionRecordDetail(submissionRecordDetailReqDTO);
    }

    @Override
    public void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        List<SubmissionRecordResDTO> list = submissionRecordMapper.listSubmissionRecord(submissionRecordDetailReqDTO.getTestRecId(), null, null, null, null);
        if (list.size() != 0) {
            if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!"10".equals(list.get(0).getRecStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        submissionRecordDetailReqDTO.setRecRevisor(TokenUtil.getUuId());
        submissionRecordDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionRecordMapper.modifySubmissionRecordDetail(submissionRecordDetailReqDTO);
    }

    @Override
    public void deleteSubmissionRecordDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionRecordDetailResDTO res = submissionRecordMapper.getSubmissionRecordDetailDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<SubmissionRecordResDTO> list = submissionRecordMapper.listSubmissionRecord(res.getTestRecId(), null, null, null, null);
                if (list.size() != 0) {
                    if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!"10".equals(list.get(0).getRecStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                    }
                }
                submissionRecordMapper.deleteSubmissionRecordDetail(id, null, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportSubmissionRecordDetail(String testRecId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "设备名称", "型号规格", "出厂编号", "公司编号", "检定校准单位",
                "证书编号", "送检条码号", "上次检定日期", "下次检定日期", "使用单位名称", "附件");
        List<SubmissionRecordDetailResDTO> meaInfoList = submissionRecordMapper.listSubmissionRecordDetail(testRecId);
        List<Map<String, String>> list = new ArrayList<>();
        if (meaInfoList != null && !meaInfoList.isEmpty()) {
            for (SubmissionRecordDetailResDTO resDTO : meaInfoList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("设备编码", resDTO.getEquipCode());
                map.put("设备名称", resDTO.getEquipName());
                map.put("型号规格", resDTO.getMatSpecifi());
                map.put("出厂编号", resDTO.getManufacture());
                map.put("公司编号", resDTO.getCompanyCode());
                map.put("检定校准单位", resDTO.getVerifyDept());
                map.put("证书编号", resDTO.getVerificationNo());
                map.put("送检条码号", resDTO.getMeasureBarcode());
                map.put("上次检定日期", resDTO.getLastVerifyDate());
                map.put("下次检定日期", resDTO.getNextVerifyDate());
                map.put("使用单位名称", resDTO.getUseDeptCname());
                map.put("附件", resDTO.getVerifyNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检定记录明细信息", listName, list, null, response);
    }

}
