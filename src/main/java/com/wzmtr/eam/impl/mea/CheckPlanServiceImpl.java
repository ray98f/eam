package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.CheckPlanReqDTO;
import com.wzmtr.eam.dto.res.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.mea.CheckPlanMapper;
import com.wzmtr.eam.service.mea.CheckPlanService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class CheckPlanServiceImpl implements CheckPlanService {

    @Autowired
    private CheckPlanMapper checkPlanMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<CheckPlanResDTO> page = checkPlanMapper.pageCheckPlan(pageReqDTO.of(), checkPlanListReqDTO);
        List<CheckPlanResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (CheckPlanResDTO res : list) {
                res.setEditDeptCode(organizationMapper.getNamesById(res.getEditDeptCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public CheckPlanResDTO getCheckPlanDetail(String id) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        res.setEditDeptCode(organizationMapper.getNamesById(res.getEditDeptCode()));
        return res;
    }

    @Override
    public void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String recCreator = TokenUtil.getCurrentPersonId();
        CurrentLoginUser user = TokenUtil.getCurrentPerson();
        String userName = user.getPersonName();
        String editDeptCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
        String recCreateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        String archiveFlag = "0";
        String instrmPlanNo = checkPlanMapper.getMaxCode();
        if (instrmPlanNo == null || "".equals(instrmPlanNo) || !("20" + instrmPlanNo.substring(2, 8)).equals(day.format(System.currentTimeMillis()))) {
            instrmPlanNo = "JP" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            instrmPlanNo = CodeUtils.getNextCode(instrmPlanNo, 8);
        }
        checkPlanReqDTO.setRecId(TokenUtil.getUuId());
        checkPlanReqDTO.setInstrmPlanNo(instrmPlanNo);
        checkPlanReqDTO.setPlanStatus("10");
        checkPlanReqDTO.setRecCreator(recCreator);
        checkPlanReqDTO.setPlanCreaterNo(recCreator);
        checkPlanReqDTO.setPlanCreaterName(userName);
        checkPlanReqDTO.setRecCreateTime(recCreateTime);
        checkPlanReqDTO.setArchiveFlag(archiveFlag);
        checkPlanReqDTO.setEditDeptCode(editDeptCode);
        if ("".equals(checkPlanReqDTO.getPlanPeriodMark())) {
            checkPlanReqDTO.setPlanPeriodMark(" ");
        }
        if ("".equals(checkPlanReqDTO.getPlanCreateTime())) {
            checkPlanReqDTO.setPlanCreateTime(" ");
        }
        checkPlanMapper.addCheckPlan(checkPlanReqDTO);
    }

    @Override
    public void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前操作人非记录创建者");
        }
        if (!"10".equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
        checkPlanListReqDTO.setInstrmPlanType(checkPlanReqDTO.getInstrmPlanType());
        checkPlanListReqDTO.setPlanPeriodMark(checkPlanReqDTO.getPlanPeriodMark());
        checkPlanListReqDTO.setEditDeptCode(checkPlanReqDTO.getEditDeptCode());
        List<CheckPlanResDTO> planList = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        if (planList != null && planList.size() != 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该定检计划已存在");
        }
        checkPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        checkPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        checkPlanMapper.modifyCheckPlan(checkPlanReqDTO);
    }

    @Override
    public void deleteCheckPlan(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "当前操作人非记录创建者");
                }
                if (!"10".equals(res.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                checkPlanMapper.deleteCheckPlanDetail(res.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    // todo 删除工作流
                }
                checkPlanMapper.deleteCheckPlan(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前操作人非记录创建者");
        }
        List<MeaInfoResDTO> result = checkPlanMapper.listInfo(null, res.getInstrmPlanNo());
        if (result.size() == 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "此定检计划不存在计划明细，无法提交");
        }
        if (!"10".equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        } else {
            // todo 工作流 ServiceDMAM0201 submit
        }
    }

    @Override
    public void exportCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "定检计划号", "年月", "编制部门", "计划人", "计划状态", "备注");
        List<CheckPlanResDTO> checkPlanList = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            for (CheckPlanResDTO resDTO : checkPlanList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("定检计划号", resDTO.getInstrmPlanNo());
                map.put("年月", resDTO.getPlanPeriodMark());
                map.put("编制部门", organizationMapper.getNamesById(resDTO.getEditDeptCode()));
                map.put("计划人", resDTO.getPlanCreaterName());
                map.put("计划状态", resDTO.getPlanStatus());
                map.put("备注", resDTO.getPlanNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("定检计划信息", listName, list, null, response);
    }

}
