package com.wzmtr.eam.impl.specialEquip;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.specialEquip.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.DetectionReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelDetectionDetailResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelDetectionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.specialEquip.DetectionMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.specialEquip.DetectionService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    private DetectionMapper detectionMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Override
    public Page<DetectionResDTO> pageDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DetectionResDTO> page = detectionMapper.pageDetection(pageReqDTO.of(), checkNo, sendVerifyNo, editDeptCode, recStatus);
        List<DetectionResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DetectionResDTO resDTO : list) {
                resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public DetectionResDTO getDetectionDetail(String id) {
        DetectionResDTO resDTO = detectionMapper.getDetectionDetail(id);
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
        return resDTO;
    }

    @Override
    public void addDetection(DetectionReqDTO detectionReqDTO) {
        SimpleDateFormat min = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        detectionReqDTO.setRecId(TokenUtil.getUuId());
        detectionReqDTO.setArchiveFlag("0");
        detectionReqDTO.setRecStatus("10");
        detectionReqDTO.setEditDeptCode(TokenUtil.getCurrentPerson().getOfficeAreaId() == null ? " " : TokenUtil.getCurrentPerson().getOfficeAreaId());
        detectionReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        detectionReqDTO.setRecCreateTime(min.format(System.currentTimeMillis()));
        String checkNo = detectionMapper.getMaxCode();
        if (StringUtils.isEmpty(checkNo) || !(CommonConstants.TWENTY_STRING + checkNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(day.format(System.currentTimeMillis()))) {
            checkNo = "TJ" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            checkNo = CodeUtils.getNextCode(checkNo, 8);
        }
        detectionReqDTO.setCheckNo(checkNo);
        detectionMapper.addDetection(detectionReqDTO);
    }

    @Override
    public void modifyDetection(DetectionReqDTO detectionReqDTO) {
        DetectionResDTO resDTO = detectionMapper.getDetectionDetail(detectionReqDTO.getRecId());
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (!detectionReqDTO.getAssetKindCode().equals(resDTO.getAssetKindCode())) {
            if (detectionMapper.hasDetail(detectionReqDTO.getRecId()).size() != 0) {
                throw new CommonException(ErrorCode.PLAN_HAS_DETAIL);
            }
        }
        detectionReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        detectionReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.modifyDetection(detectionReqDTO);
    }

    @Override
    public void deleteDetection(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionResDTO resDTO = detectionMapper.getDetectionDetail(id);
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionMapper.deleteDetectionDetail(resDTO.getRecId(), null, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                detectionMapper.deleteDetection(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitDetection(DetectionReqDTO detectionReqDTO) throws Exception {
        // ServiceDMSE0301
        DetectionResDTO res = detectionMapper.getDetectionDetail(detectionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<DetectionDetailResDTO> result = detectionMapper.listDetectionDetail(res.getRecId());
        if (CollectionUtil.isEmpty(result)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "此检测单不存在检测明细，无法提交");
        }
        // todo 源代码逻辑，存在无效字段判断。先注释了
        // for (DetectionDetailResDTO temp : result) {
        //     if (StringUtil.isBlank(temp.getVerifyResult()) || StringUtil.isBlank(temp.getVerifyDate()) ||
        //             StringUtil.isBlank(temp.getVerifyValidityDate())) {
        //         throw new CommonException(ErrorCode.NORMAL_ERROR, "存在检测记录明细检测结果或检测日期或检测有效期为空，无法提交");
        //     }
        // }
        if (!CommonConstants.TEN_STRING.equals(res.getRecStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "非编辑状态不可提交");
        } else {
            String processId = bpmnService.commit(res.getCheckNo(), BpmnFlowEnum.DETECTION_SUBMIT.value(), null, null, detectionReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            DetectionReqDTO reqDTO = new DetectionReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.DETECTION_SUBMIT.value(), null));
            reqDTO.setRecStatus("20");
            reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
            reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            detectionMapper.modifyDetection(reqDTO);
        }
    }

    @Override
    public void examineDetection(DetectionReqDTO detectionReqDTO) {
        DetectionResDTO res = detectionMapper.getDetectionDetail(detectionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionReqDTO reqDTO = new DetectionReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        if (detectionReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(reqDTO.getRecStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            String processId = res.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.DETECTION_SUBMIT.value()).contains(reqDTO.getWorkFlowInstStatus())) {
                bpmnService.agree(taskId, detectionReqDTO.getExamineReqDTO().getOpinion(), String.join(",", detectionReqDTO.getExamineReqDTO().getUserIds()), "{\"id\":\"" + res.getCheckNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus(bpmnService.getNextNodeId(BpmnFlowEnum.DETECTION_SUBMIT.value(), reqDTO.getWorkFlowInstStatus()));
                reqDTO.setRecStatus("20");
            } else {
                bpmnService.agree(taskId, detectionReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getCheckNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus("已完成");
                reqDTO.setRecStatus("30");
                List<DetectionDetailResDTO> list = detectionMapper.queryMsg(reqDTO.getRecId());
                if (list != null && !list.isEmpty()) {
                    for (DetectionDetailResDTO detectionDetail : list) {
                        detectionMapper.updateEquip(detectionDetail);
                    }
                }
            }
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(reqDTO.getRecStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, detectionReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId("");
                reqDTO.setWorkFlowInstStatus("");
                reqDTO.setRecStatus("10");
            }
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.modifyDetection(reqDTO);
    }

    @Override
    public void exportDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, HttpServletResponse response) throws IOException {
        List<DetectionResDTO> detectionResDTOList = detectionMapper.listDetection(null, checkNo, sendVerifyNo, editDeptCode, recStatus);
        if (detectionResDTOList != null && !detectionResDTOList.isEmpty()) {
            List<ExcelDetectionResDTO> list = new ArrayList<>();
            for (DetectionResDTO resDTO : detectionResDTOList) {
                ExcelDetectionResDTO res = ExcelDetectionResDTO.builder()
                        .checkNo(resDTO.getCheckNo())
                        .assetKindCode(CommonConstants.TEN_STRING.equals(resDTO.getAssetKindCode()) ? "电梯" : CommonConstants.TWENTY_STRING.equals(resDTO.getAssetKindCode()) ? "起重机" : CommonConstants.THIRTY_STRING.equals(resDTO.getAssetKindCode()) ? "场（厂）内专用机动车辆" : "压力容器")
                        .manageOrg(organizationMapper.getOrgById(resDTO.getManageOrg()))
                        .secOrg(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()))
                        .editDeptCode(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()))
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getRecStatus()) ? "审核中" : "审核通过")
                        .verifyNote(resDTO.getVerifyNote())
                        .build();
                list.add(res);
            }
            EasyExcelUtils.export(response, "检测记录信息", list);
        }
    }

    @Override
    public Page<DetectionDetailResDTO> pageDetectionDetail(String testRecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return detectionMapper.pageDetectionDetail(pageReqDTO.of(), testRecId);
    }

    @Override
    public DetectionDetailResDTO getDetectionDetailDetail(String id) {
        return detectionMapper.getDetectionDetailDetail(id);
    }

    @Override
    public void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        List<DetectionResDTO> list = detectionMapper.listDetection(detectionDetailReqDTO.getTestRecId(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (sdf.parse(detectionDetailReqDTO.getVerifyValidityDate()).before(sdf.parse(detectionDetailReqDTO.getVerifyDate()))) {
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        detectionDetailReqDTO.setRecId(TokenUtil.getUuId());
        detectionDetailReqDTO.setArchiveFlag("0");
        detectionDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        detectionDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.addDetectionDetail(detectionDetailReqDTO);
    }

    @Override
    public void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        List<DetectionResDTO> list = detectionMapper.listDetection(detectionDetailReqDTO.getTestRecId(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (sdf.parse(detectionDetailReqDTO.getVerifyValidityDate()).before(sdf.parse(detectionDetailReqDTO.getVerifyDate()))) {
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        detectionDetailReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        detectionDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.modifyDetectionDetail(detectionDetailReqDTO);
    }

    @Override
    public void deleteDetectionDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionDetailResDTO detailResDTO = detectionMapper.getDetectionDetailDetail(id);
                if (Objects.isNull(detailResDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<DetectionResDTO> list = detectionMapper.listDetection(detailResDTO.getTestRecId(), null, null, null, null);
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                DetectionResDTO resDTO = list.get(0);
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionMapper.deleteDetectionDetail(null, id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportDetectionDetail(String testRecId, HttpServletResponse response) throws IOException {
        List<DetectionDetailResDTO> detectionPlanDetailResDTOList = detectionMapper.listDetectionDetail(testRecId);
        if (detectionPlanDetailResDTOList != null && !detectionPlanDetailResDTOList.isEmpty()) {
            List<ExcelDetectionDetailResDTO> list = new ArrayList<>();
            for (DetectionDetailResDTO resDTO : detectionPlanDetailResDTOList) {
                ExcelDetectionDetailResDTO res = new ExcelDetectionDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检测计划明细信息", list);
        }
    }

}
