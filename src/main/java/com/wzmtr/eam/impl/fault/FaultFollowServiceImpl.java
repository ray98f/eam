package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.fault.FaultFollowExportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReportReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFollowReqDTO;
import com.wzmtr.eam.dto.res.fault.ExcelFaultFollowResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowReportResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFollowResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.PersonMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultFollowMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.fault.FaultFollowService;
import com.wzmtr.eam.shiro.model.Person;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private FaultFollowMapper faultFollowMapper;
    @Autowired
    private OrgMajorMapper orgMajorMapper;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private DictionariesMapper dictMapper;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<FaultFollowResDTO> page(String followNo, String faultWorkNo,
                                        String followStatus, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultFollowMapper.page(pageReqDTO.of(), followNo, faultWorkNo, followStatus, TokenUtils.getCurrentPersonId());
    }

    @Override
    public FaultFollowResDTO detail(String id) {
        FaultFollowResDTO res = faultFollowMapper.detail(id, null);
        if (StringUtils.isNotNull(res)) {
            List<FaultFollowReportResDTO> reportList = faultFollowMapper.getReport(res.getFollowNo());
            if (StringUtils.isNotEmpty(reportList)) {
                for (FaultFollowReportResDTO report : reportList) {
                    if (StringUtils.isNotEmpty(report.getDocId())) {
                        report.setDocFile(fileMapper.selectFileInfo(Arrays.asList(report.getDocId().split(","))));
                    }
                }
            }
            res.setReportList(reportList);
        }
        return res;
    }

    @Override
    public List<Person> listLeader(String majorCode, String positionCode) {
        // 专业和位置查维修部门
        String names = orgMajorMapper.getOrgNamesByStationAndMajor(positionCode, majorCode);
        if (StringUtils.isNotNull(names)) {
            return personMapper.listLeader(majorCode, positionCode,
                    names.startsWith(CommonConstants.ZC) ? CommonConstants.DM_012 : CommonConstants.DM_051);
        } else {
            return new ArrayList<>();
        }
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
    }

    @Override
    public void modify(FaultFollowReqDTO req) {
        req.setFollowTime(DateUtils.getCurrentTime());
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modify(req);
    }

    @Override
    public void close(FaultFollowReqDTO req) {
        req.setFollowCloserId(TokenUtils.getCurrentPersonId());
        req.setFollowCloserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setFollowCloseTime(DateUtils.getCurrentTime());
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
    public List<FaultFollowReportResDTO> listReport(String followNo) {
        List<FaultFollowReportResDTO> list = faultFollowMapper.getReport(followNo);
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
        if (StringUtils.isNotEmpty(res.getDocId())) {
            res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
        }
        return res;
    }

    @Override
    public void addReport(FaultFollowReportReqDTO req) throws ParseException {
        // 报告判断
        long step = checkReport(req.getFollowNo());
        req.setStep(step);
        req.setRecId(TokenUtils.getUuId());
        req.setReportUserId(TokenUtils.getCurrentPersonId());
        req.setReportUserName(TokenUtils.getCurrentPerson().getPersonName());
        req.setReportTime(DateUtils.getCurrentTime());
        req.setRecCreator(TokenUtils.getCurrentPersonId());
        req.setRecCreateTime(DateUtils.getCurrentTime());
        req.setExamineStatus(CommonConstants.ZERO_STRING);
        faultFollowMapper.addReport(req);
        FaultFollowReqDTO faultFollow = new FaultFollowReqDTO();
        faultFollow.setFollowNo(req.getFollowNo());
        faultFollow.setFollowStatus(CommonConstants.THIRTY_STRING);
        faultFollowMapper.modify(faultFollow);
        // todo 新增待办
    }

    @Override
    public void modifyReport(FaultFollowReportReqDTO req) {
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        faultFollowMapper.modifyReport(req);
    }

    /**
     * 跟踪报告判断
     * @param followNo 跟踪工单编号
     * @return 跟踪报告阶段
     * @throws ParseException 异常
     */
    private long checkReport(String followNo) throws ParseException {
        // 获取跟踪工单详情
        long step = 0L;
        FaultFollowResDTO follow = faultFollowMapper.detail(null, followNo);
        if (StringUtils.isNull(follow)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        // 获取跟踪工单中上一次报告信息
        FaultFollowReportResDTO lastRes = faultFollowMapper.getLastReportByFollowNo(followNo);
        if (StringUtils.isNotNull(lastRes)) {
            if (CommonConstants.ZERO_STRING.equals(lastRes.getExamineStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "当前跟踪工单中仍存在未审核的跟踪报告，无法填写新的跟踪报告！");
            } else {
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
                Integer result = faultFollowMapper.checkReportStep(followNo, step);
                if (result != 0) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "本周期的跟踪报告已提交，无法再次提交！");
                }
            }
        } else {
            step = 1L;
        }
        return step;
    }

    @Override
    public void examineReport(FaultFollowReportReqDTO req) {
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
            // todo 驳回时新增待办
        } else {
            faultFollow.setFollowStatus(CommonConstants.TWENTY_STRING);
        }
        faultFollowMapper.modify(faultFollow);
    }

}
