package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.dto.res.fault.ExcelFaultFollowResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowDispatchUserResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.entity.SysUser;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultFollowMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.fault.FaultFollowService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 故障管理-故障跟踪(新)
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@Service
@Slf4j
public class FaultFollowServiceImpl implements FaultFollowService {
    @Resource
    private UserAccountService userAccountService;
    @Autowired
    private FaultFollowMapper faultFollowMapper;
    @Autowired
    private DictionariesMapper dictMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<FaultFollowResDTO> page(String followNo, String faultWorkNo,
                                        String followStatus, PageReqDTO pageReqDTO) {
        //获取用户当前角色
        List<UserRoleResDTO> userRoles = userAccountService.getUserRolesById(TokenUtils.getCurrentPersonId());
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<FaultFollowResDTO> page;
        if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            page = faultFollowMapper.page(pageReqDTO.of(), followNo, faultWorkNo, followStatus, null, null);
        } else {
            page = faultFollowMapper.page(pageReqDTO.of(), followNo, faultWorkNo, followStatus,
                    TokenUtils.getCurrentPersonId(), TokenUtils.getCurrentPerson().getOfficeId());
        }
        List<FaultFollowResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (FaultFollowResDTO res : list) {
                // admin 运维管理部 车辆部显示强制关闭按钮
                if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())
                        || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_004))
                        || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_005))) {
                    res.setIfShowClose(CommonConstants.ONE_STRING);
                } else {
                    res.setIfShowClose(CommonConstants.ZERO_STRING);
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public FaultFollowResDTO detail(String id) {
        FaultFollowResDTO res = faultFollowMapper.detail(id, null);
        if (StringUtils.isNotNull(res)) {
            List<FaultFollowReportResDTO> reportList = faultFollowMapper.getReport(res.getFollowNo(), "1");
            if (StringUtils.isNotEmpty(reportList)) {
                for (FaultFollowReportResDTO report : reportList) {
                    if (StringUtils.isNotEmpty(report.getDocId())) {
                        report.setDocFile(fileMapper.selectFileInfo(Arrays.asList(report.getDocId().split(","))));
                    }
                }
            }
            res.setReportList(reportList);
            //获取用户当前角色
            List<UserRoleResDTO> userRoles = userAccountService.getUserRolesById(TokenUtils.getCurrentPersonId());
            // admin 运维管理部 车辆部显示强制关闭按钮
            if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())
                    || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_004))
                    || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_005))) {
                res.setIfShowClose(CommonConstants.ONE_STRING);
            } else {
                res.setIfShowClose(CommonConstants.ZERO_STRING);
            }
        }
        return res;
    }

    @Override
    public List<SysUser> listLeader(String faultWorkNo) {
        FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
        if (StringUtils.isNotNull(faultOrder) && StringUtils.isNotEmpty(faultOrder.getRepairRespUserId())) {
            SysOffice office = userAccountMapper.getUserOrg(faultOrder.getRepairRespUserId());
            if (StringUtils.isNotNull(office)) {
                return organizationMapper.listUser(office.getId(), office.getNames().contains(CommonConstants.ZC) ?
                        CommonConstants.DM_012 : CommonConstants.DM_051);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void add(FaultFollowReqDTO req) {
        // 生成跟踪单号
        String maxCode = faultFollowMapper.selectMaxCode();
        req.setFollowNo(CodeUtils.getNextCode(maxCode, "GT"));
        req.setRecId(TokenUtils.getUuId());
        req.setFollowUserId(TokenUtils.getCurrentPersonId());
        req.setFollowUserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setFollowTime(DateUtils.getCurrentTime());
        req.setRecCreator(TokenUtils.getCurrentPersonId());
        req.setRecCreateTime(DateUtils.getCurrentTime());
        faultFollowMapper.add(req);
        overTodoService.insertTodo("跟踪工单待派工", req.getRecId(), req.getFollowNo(), req.getFollowLeaderId(),
                "跟踪工单派工", "followDispatch", TokenUtils.getCurrentPersonId(),
                CommonConstants.FAULT_FOLLOW_REPORT);
    }

    @Override
    public void modify(FaultFollowReqDTO req) {
        req.setFollowTime(DateUtils.getCurrentTime());
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modify(req);
    }

    @Override
    public FaultFollowDispatchUserResDTO listDispatchUser(String followNo) {
        FaultFollowResDTO faultFollow = faultFollowMapper.detail(null, followNo);
        if (StringUtils.isNull(faultFollow)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        SysOffice office = userAccountMapper.getUserOrg(faultFollow.getFollowLeaderId());
        FaultFollowDispatchUserResDTO res = new FaultFollowDispatchUserResDTO();
        if (StringUtils.isNotNull(office)) {
            res.setWorkerOrgId(office.getId());
            res.setWorkerOrgName(office.getName());
            res.setWorkerList(organizationMapper.listUser(office.getId(), null));
        }
        return res;
    }

    @Override
    public void dispatch(FaultFollowReqDTO req) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && !req.getFollowLeaderId().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "你不是这条故障跟踪工单的跟踪工班长，无法进行派工！");
        }
        req.setDispatchTime(DateUtils.getCurrentTime());
        req.setFollowStatus(CommonConstants.TWENTY_STRING);
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.dispatch(req);
        // 相关待办修改为已办
        overTodoService.overTodo(req.getRecId(), req.getFollowNo() + "的跟踪工单已派工", CommonConstants.ONE_STRING);
        overTodoService.insertTodo("跟踪工单待填写报告", req.getRecId(), req.getFollowNo(), req.getFollowLeaderId(),
                "跟踪工单填写报告", "followAddReport", TokenUtils.getCurrentPersonId(),
                CommonConstants.FAULT_FOLLOW_REPORT);
    }

    @Override
    public void close(FaultFollowReqDTO req) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && !req.getFollowLeaderId().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "你不是这条故障跟踪工单的跟踪工班长，无法关闭工单！");
        }
        req.setFollowCloserId(TokenUtils.getCurrentPersonId());
        req.setFollowCloserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setFollowCloseTime(DateUtils.getCurrentTime());
        req.setFollowStatus(CommonConstants.FIFTY_STRING);
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.close(req);
    }

    @Override
    public void export(FaultFollowExportReqDTO req, HttpServletResponse response) throws IOException {
        List<FaultFollowResDTO> faultFollowList = faultFollowMapper.export(req);
        if (StringUtils.isNotEmpty(faultFollowList)) {
            List<ExcelFaultFollowResDTO> list = new ArrayList<>();
            for (FaultFollowResDTO resDTO : faultFollowList) {
                ExcelFaultFollowResDTO res = new ExcelFaultFollowResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setFollowStartEndTime(resDTO.getFollowStartDate() + " ~ " + resDTO.getFollowEndDate());
                Dictionaries dictionaries = dictMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_FAULT_FOLLOW_STATUS, resDTO.getFollowStatus());
                if (StringUtils.isNotNull(dictionaries)) {
                    res.setFollowStatus(dictionaries.getItemCname());
                } else {
                    res.setFollowStatus("");
                }
                list.add(res);
            }
            EasyExcelUtils.export(response, "故障跟踪工单信息", list);
        }
    }

    @Override
    public List<FaultFollowReportResDTO> listReport(String followNo, String type) {
        List<FaultFollowReportResDTO> list = faultFollowMapper.getReport(followNo, type);
        if (StringUtils.isNotEmpty(list)) {
            for (FaultFollowReportResDTO res : list) {
                if (StringUtils.isNotEmpty(res.getDocId())) {
                    res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
                }
            }
        }
        return list;
    }

    @Override
    public FaultFollowReportResDTO getReportDetail(String id) {
        FaultFollowReportResDTO res = faultFollowMapper.getReportDetail(id);
        if (StringUtils.isNotNull(res) && StringUtils.isNotEmpty(res.getDocId())) {
            res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
        }
        return res;
    }

    @Override
    public void addReport(FaultFollowReportReqDTO req) throws ParseException {
        FaultFollowResDTO follow = faultFollowMapper.detail(null, req.getFollowNo());
        if (StringUtils.isNotNull(follow) && !follow.getDispatchUserId().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "你不是这条故障跟踪工单派发的填写人，无法填写报告！");
        }
        if (StringUtils.isEmpty(req.getRecId())) {
            // 报告判断
            long step = checkReport(req.getFollowNo());
            req.setStep(step);
            req.setReportUserId(TokenUtils.getCurrentPersonId());
            req.setReportUserName(TokenUtils.getCurrentPerson().getPersonName());
            req.setReportTime(DateUtils.getCurrentTime());
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(), req.getFollowNo() + "的跟踪工单报告已填写", CommonConstants.ONE_STRING);
        } else {
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(), req.getFollowNo() + "的跟踪工单" + req.getStep() + "阶段报告已重新提交", CommonConstants.ONE_STRING);
        }
        req.setRecId(TokenUtils.getUuId());
        req.setExamineStatus(CommonConstants.ZERO_STRING);
        req.setRecCreator(TokenUtils.getCurrentPersonId());
        req.setRecCreateTime(DateUtils.getCurrentTime());
        faultFollowMapper.addReport(req);
        FaultFollowReqDTO faultFollow = new FaultFollowReqDTO();
        faultFollow.setFollowNo(req.getFollowNo());
        faultFollow.setFollowStatus(CommonConstants.THIRTY_STRING);
        faultFollow.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultFollow.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modify(faultFollow);
        // 审核待办发送
        FaultFollowResDTO followRes = faultFollowMapper.detail(null, req.getFollowNo());
        if (StringUtils.isNotNull(followRes)) {
            overTodoService.insertTodo("跟踪工单报告待审核", req.getRecId(), req.getFollowNo(), followRes.getFollowLeaderId(),
                    "跟踪工单报告工班长审核", "followReportExamine", TokenUtils.getCurrentPersonId(),
                    CommonConstants.FAULT_FOLLOW_REPORT);
        }
    }

    /**
     * 跟踪报告判断
     * @param followNo 跟踪工单编号
     * @return 跟踪报告阶段
     * @throws ParseException 异常
     */
    private long checkReport(String followNo) throws ParseException {
        // 获取跟踪工单详情
        long step;
        // 获取跟踪工单中上一次报告信息
        FaultFollowReportResDTO lastRes = faultFollowMapper.getLastReportByFollowNo(followNo);
        if (StringUtils.isNotNull(lastRes) && CommonConstants.ZERO_STRING.equals(lastRes.getExamineStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前跟踪工单中仍存在未审核的跟踪报告，无法填写新的跟踪报告！");
        }
        // 获取当前日期在跟踪工单第几跟踪周期
        step = getFollowDays(followNo, CommonConstants.ONE_STRING);
        if (step == 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前时间不在此故障跟踪工单的起止时间范围内，无法填写跟踪报告！");
        } else {
            Integer result = faultFollowMapper.checkReportStep(followNo, step);
            if (result != 0) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "本周期的跟踪报告已提交，无法再次提交！");
            }
        }
        return step;
    }

    /**
     * 获取当前日期在跟踪工单第几跟踪周期
     * @param followNo 跟踪工单号
     * @param type     类型 1新增时 2审核时
     * @return 跟踪周期数
     * @throws ParseException 异常
     */
    private long getFollowDays(String followNo, String type) throws ParseException {
        long step = 0L;
        FaultFollowResDTO follow = faultFollowMapper.detail(null, followNo);
        if (StringUtils.isNull(follow)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        // 根据开始时间结束时间获取指定时间周期的所有日期
        List<String> days = DateUtils.getAllTimesWithinRange(follow.getFollowStartDate(),
                follow.getFollowEndDate(), follow.getFollowCycle());
        days.add(follow.getFollowEndDate());
        for (int i = 1; i < days.size(); i++) {
            if (DateUtils.getDateBetweenContainStartExcludeEnd(DateUtils.getDate(), days.get(i - 1), days.get(i))) {
                step = i;
                break;
            }
        }
        // 当类型为审核时，如果当前周期为最后一个周期，返回0
        if (CommonConstants.TWO_STRING.equals(type) && step == days.size()) {
            step = 0L;
        }
        return step;
    }

    @Override
    public void examineReport(FaultFollowReportReqDTO req) throws ParseException {
        FaultFollowResDTO follow = faultFollowMapper.detail(null, req.getFollowNo());
        if (StringUtils.isNotNull(follow) && !follow.getFollowLeaderId().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "你不是这条故障跟踪工单的跟踪工班长，无法审核该数据！");
        }
        req.setExamineUserId(TokenUtils.getCurrentPersonId());
        req.setExamineUserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setExamineTime(DateUtils.getCurrentTime());
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.examineReport(req);
        FaultFollowReqDTO faultFollow = new FaultFollowReqDTO();
        faultFollow.setFollowNo(req.getFollowNo());
        if (CommonConstants.TWO_STRING.equals(req.getExamineStatus())) {
            faultFollow.setFollowStatus(CommonConstants.FORTY_STRING);
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(),
                    req.getFollowNo() + "的跟踪工单" + req.getStep() + "报告工班长驳回：" + req.getExamineOpinion(), CommonConstants.ONE_STRING);
            // 驳回时新增待办
            overTodoService.insertTodo("跟踪工单报告被驳回，需重新提交", req.getRecId(), req.getFollowNo(), req.getReportUserId(),
                    "跟踪工单报告提交", "followReportSubmit", TokenUtils.getCurrentPersonId(), CommonConstants.FAULT_FOLLOW_REPORT);
        } else {
            faultFollow.setFollowStatus(CommonConstants.THIRTY_FIVE_STRING);
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(),
                    req.getFollowNo() + "的跟踪工单" + req.getStep() + "阶段报告工班长通过：" + req.getExamineOpinion(), CommonConstants.ONE_STRING);
            // 新增专业工程师待办
            overTodoService.insertTodo("跟踪工单报告待审核", req.getRecId(), req.getFollowNo(), follow.getFollowUserId(),
                    "跟踪工单报告专业工程师审核", "followReportEngineerExamine", TokenUtils.getCurrentPersonId(),
                    CommonConstants.FAULT_FOLLOW_REPORT);
        }
        faultFollow.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultFollow.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modify(faultFollow);
    }

    @Override
    public void engineerExamineReport(FaultFollowReportReqDTO req) throws ParseException {
        FaultFollowResDTO follow = faultFollowMapper.detail(null, req.getFollowNo());
        if (StringUtils.isNotNull(follow)) {
            if (!CommonConstants.THIRTY_FIVE_STRING.equals(follow.getFollowStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "故障跟踪工单未处于专业工程师审核阶段，无法审核！");
            }
            if (!follow.getFollowUserId().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "你不是发起这条故障跟踪工单的专业工程师，无法审核该数据！");
            }
        }
        req.setFollowExamineUserId(TokenUtils.getCurrentPersonId());
        req.setFollowExamineUserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setFollowExamineTime(DateUtils.getCurrentTime());
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.engineerExamineReport(req);
        FaultFollowReqDTO faultFollow = new FaultFollowReqDTO();
        faultFollow.setFollowNo(req.getFollowNo());
        if (CommonConstants.TWO_STRING.equals(req.getFollowExamineStatus())) {
            faultFollow.setFollowStatus(CommonConstants.FORTY_STRING);
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(),
                    req.getFollowNo() + "的跟踪工单" + req.getStep() + "报告专业工程师驳回：" + req.getExamineOpinion(), CommonConstants.ONE_STRING);
            // 驳回时新增待办
            overTodoService.insertTodo("跟踪工单报告被驳回，需重新提交", req.getRecId(), req.getFollowNo(), req.getReportUserId(),
                    "跟踪工单报告提交", "followReportSubmit", TokenUtils.getCurrentPersonId(), CommonConstants.FAULT_FOLLOW_REPORT);
        } else {
            // 获取当前日期在跟踪工单第几跟踪周期
            long step = getFollowDays(req.getFollowNo(), CommonConstants.TWO_STRING);
            // 当前时间不在周期内或最后一个周期时，审核通过自动关闭工单
            if (step == 0) {
                faultFollow.setFollowStatus(CommonConstants.FIFTY_STRING);
            } else {
                faultFollow.setFollowStatus(CommonConstants.TWENTY_STRING);
            }
            // 相关待办修改为已办
            overTodoService.overTodo(req.getRecId(),
                    req.getFollowNo() + "的跟踪工单" + req.getStep() + "阶段报告专业工程师通过：" + req.getExamineOpinion(), CommonConstants.ONE_STRING);
        }
        faultFollow.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultFollow.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modify(faultFollow);
    }

}
