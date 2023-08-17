package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.SubmissionResDTO;
import com.wzmtr.eam.dto.res.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.mea.SubmissionMapper;
import com.wzmtr.eam.service.mea.SubmissionService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
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
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Override
    public Page<SubmissionResDTO> pageSubmission(SubmissionListReqDTO submissionListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionMapper.pageSubmission(pageReqDTO.of(), submissionListReqDTO);
    }

    @Override
    public SubmissionResDTO getSubmissionDetail(String id) {
        return submissionMapper.getSubmissionDetail(id);
    }

    @Override
    public void addSubmission(SubmissionReqDTO submissionReqDTO) {
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String sendVerifyNo = submissionMapper.getMaxCode();
        if (sendVerifyNo == null || "".equals(sendVerifyNo) || !("20" + sendVerifyNo.substring(2, 8)).equals(day.format(System.currentTimeMillis()))) {
            sendVerifyNo = "JW" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            sendVerifyNo = CodeUtils.getNextCode(sendVerifyNo, 8);
        }
        submissionReqDTO.setRecId(TokenUtil.getUuId());
        submissionReqDTO.setSendVerifyNo(sendVerifyNo);
        submissionReqDTO.setSendVerifyStatus("10");
        submissionReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        submissionReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionReqDTO.setArchiveFlag("0");
        submissionMapper.addSubmission(submissionReqDTO);
    }

    @Override
    public void modifySubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前操作人非记录创建者");
        }
        if (!"10".equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        submissionReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        submissionReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionMapper.modifySubmission(submissionReqDTO);
    }

    @Override
    public void deleteSubmission(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionResDTO res = submissionMapper.getSubmissionDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "当前操作人非记录创建者");
                }
                if (!"10".equals(res.getSendVerifyStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                submissionMapper.deleteSubmissionDetail(res.getSendVerifyNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    // todo 删除工作流
                }
                submissionMapper.deleteSubmission(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitSubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!"10".equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "提交");
        } else {
            List<SubmissionDetailResDTO> result = submissionMapper.listSubmissionDetail(res.getSendVerifyNo());
            if (result.size() == 0) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "此送检单不存在计划明细，无法提交");
            }
            // todo 工作流 ServiceDMAM0101 submit
        }
    }

    @Override
    public void exportSubmission(SubmissionListReqDTO checkPlanListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "送检单号", "送检委托人", "送检委托人电话", "送检接收人", "送检接收人电话",
                "送检日期", "返送人", "返送人电话", "返还日期", "返还接收人", "返还接收人电话", "送检单状态");
        List<SubmissionResDTO> checkPlanList = submissionMapper.listSubmission(checkPlanListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            for (SubmissionResDTO resDTO : checkPlanList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("送检单号", resDTO.getSendVerifyNo());
                map.put("送检委托人", resDTO.getSendConsignerName());
                map.put("送检委托人电话", resDTO.getSendConsignerTele());
                map.put("送检接收人", resDTO.getSendReceiverName());
                map.put("送检接收人电话", resDTO.getSendReceiverTele());
                map.put("送检日期", resDTO.getSendVerifyDate());
                map.put("返送人", resDTO.getBackReturnName());
                map.put("返送人电话", resDTO.getBackReturnTele());
                map.put("返还日期", resDTO.getVerifyBackDate());
                map.put("返还接收人", resDTO.getBackReceiverName());
                map.put("返还接收人电话", resDTO.getBackConsignerTele());
                map.put("送检单状态", resDTO.getSendVerifyStatus());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("送检单信息", listName, list, null, response);
    }

}
