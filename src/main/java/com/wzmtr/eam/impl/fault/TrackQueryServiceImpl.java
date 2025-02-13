package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.FaultTrackBO;
import com.wzmtr.eam.bizobject.FaultTrackWorkBO;
import com.wzmtr.eam.bizobject.export.FaultTrackExportBO;
import com.wzmtr.eam.constant.ColsConstants;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dataobject.FaultTrackWorkDO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultTrackSaveReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultFlowResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackWorkMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
@Slf4j
public class TrackQueryServiceImpl implements TrackQueryService {
    @Autowired
    private FaultTrackMapper faultTrackMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private IDictionariesService dictService;
    @Autowired
    private FaultTrackWorkMapper faultTrackWorkMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackQueryResDTO> list = faultTrackMapper.query(reqDTO.of(), reqDTO);
        List<TrackQueryResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> {
            if (StringUtils.isNotEmpty(a.getDocId())) {
                a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(CommonConstants.COMMA))));
            }
        });
        return list;
    }

    @Override
    public FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO) {
        FaultInfoDO faultInfo = faultTrackMapper.faultDetail(reqDTO);
        if (StringUtils.isNull(faultInfo)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        FaultDetailResDTO faultOrder = faultTrackMapper.faultOrderDetail(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), faultInfo.getMajorCode());
        if (StringUtils.isNull(faultOrder)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        FaultDetailResDTO faultDetail = assemblyResDTO(faultInfo, faultOrder);
        List<FaultFlowResDTO> faultFlows = faultTrackMapper.faultFlowDetail(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (StringUtils.isNotEmpty(faultFlows)) {
            faultDetail.setFlows(faultFlows);
        }
        if (StringUtils.isNotEmpty(faultDetail.getFinishPosition2Code())) {
            Dictionaries dict = dictService.queryOneByItemCodeAndCodesetCode(
                    CommonConstants.AT_STATION_POS2, faultDetail.getFinishPosition2Code());
            if (StringUtils.isNotNull(dict)) {
                faultDetail.setFinishPosition2Name(dict.getItemCname());
            }
        }
        // 待阅（实际为代办）更新为已办
        overTodoService.overTodo(faultOrder.getFaultOrderRecId(), "已查看", CommonConstants.TWO_STRING);
        return faultDetail;
    }

    private FaultDetailResDTO assemblyResDTO(FaultInfoDO faultInfo, FaultDetailResDTO faultOrder) {
        FaultDetailResDTO res = BeanUtils.copy(faultInfo, faultOrder);
        res.setFaultInfoRecId(faultInfo.getRecId());
        if (StringUtils.isNotEmpty(res.getLineCode())) {
            LineCode name = LineCode.getByCode(res.getLineCode());
            if (null != name) {
                res.setLineName(name.getDesc());
            }
        }
        if (StringUtils.isNotEmpty(res.getOrderStatus())) {
            res.setFaultStatus(res.getOrderStatus());
        }
        if (StringUtils.isNotEmpty(faultInfo.getTrainTag())) {
            res.setTraintag(faultInfo.getTrainTag());
        }
        if (StringUtils.isNotEmpty(faultInfo.getExt4())) {
            res.setMaintenance("true".equalsIgnoreCase(faultInfo.getExt4()) ? Boolean.TRUE : Boolean.FALSE);
        }
        if (StringUtils.isNotEmpty(res.getRespDeptCode())) {
            res.setRespDeptName(organizationMapper.getNamesById(res.getRespDeptCode()));
        }
        if (StringUtils.isNotEmpty(res.getRepairDeptCode())) {
            res.setRepairDeptName(organizationMapper.getNamesById(res.getRepairDeptCode()));
        }
        if (StringUtils.isNotEmpty(res.getFillinDeptCode())) {
            res.setFillinDeptName(organizationMapper.getNamesById(res.getFillinDeptCode()));
        }
        return res;
    }

    @Override
    public void cancellGenZ(BaseIdsEntity reqDTO) {
        if (StringUtils.isNotEmpty(reqDTO.getIds())) {
            List<String> ids = reqDTO.getIds();
            ids.forEach(a -> {
                TrackQueryResDTO bo = new TrackQueryResDTO();
                bo.setFaultTrackNo(a);
                bo.setRecStatus("99");
                bo.setRecRevisor(TokenUtils.getCurrentPersonId());
                bo.setRecReviseTime(DateUtils.getCurrentTime());
                bo.setExt1(CommonConstants.EMPTY);
                faultTrackMapper.cancellGenZ(bo);
            });
        }
        // faultTrackNo
    }

    @Override
    public void export(TrackQueryReqDTO reqDTO, HttpServletResponse response) {
        List<TrackQueryResDTO> res = faultTrackMapper.export(reqDTO);
        List<FaultTrackExportBO> exportList = new ArrayList<>();
        if (StringUtils.isNotEmpty(res)) {
            res.forEach(resDTO -> {
                FaultTrackExportBO exportBO = BeanUtils.convert(resDTO, FaultTrackExportBO.class);
                Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.faultTrackStatus", resDTO.getRecStatus());
                exportBO.setTrackDDL(resDTO.getTrackPeriod().toString());
                exportBO.setTrackCyc(resDTO.getTrackCycle().toString());
                exportBO.setTrackStatus(dictionaries.getItemCname());
                exportList.add(exportBO);
            });
        }
        try {
            EasyExcelUtils.export(response, "跟踪查询信息", exportList);
        } catch (Exception e) {
            log.error("导出失败！", e);
            throw new CommonException(ErrorCode.EXPORT_ERROR);
        }
    }

    @Override
    public void save(FaultTrackSaveReqDTO req) {
        String faultNo = req.getFaultNo();
        String faultWorkNo = req.getFaultWorkNo();
        Assert.isTrue(StringUtils.isNotEmpty(faultNo) && StringUtils.isNotEmpty(faultWorkNo), ErrorCode.PARAM_ERROR);
        FaultTrackBO faultTrackBO = req.toFaultTrackBO(req);
        FaultTrackWorkBO faultTrackWorkBO = req.toFaultTrackWorkBO(req);

        FaultTrackDO exist = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq(ColsConstants.FAULT_NO, faultNo));
        TrackQueryServiceImpl proxy = (TrackQueryServiceImpl) AopContext.currentProxy();
        proxy.save(exist, faultTrackBO, faultTrackWorkBO, faultNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(FaultTrackDO exist, FaultTrackBO faultTrackBO, FaultTrackWorkBO faultTrackWorkBO, String faultNo) {
        // 根据faultNo判断是否存在跟踪单 不存在则插入，存在即更新
        if (null == exist) {
            // 生成跟踪单
            String maxCode = faultTrackMapper.selectMaxCode();
            String nextCode = CodeUtils.getNextCode(maxCode, "GT");
            faultTrackBO.setFaultTrackNo(nextCode);
            faultTrackMapper.insert(BeanUtils.convert(faultTrackBO, FaultTrackDO.class));
            // 生成跟踪工单
            String faultTrackNo = faultTrackBO.getFaultTrackNo();
            String faultWorkNo = faultTrackBO.getFaultWorkNo();
            faultTrackWorkBO.setFaultTrackNo(faultTrackNo);
            String maxCodeFaultTrackWorkNo = faultTrackWorkMapper.selectMaxCode();
            String nextFaultTrackWorkNo = CodeUtils.getNextCode(maxCodeFaultTrackWorkNo, "GTW");
            faultTrackWorkBO.setFaultTrackWorkNo(nextFaultTrackWorkNo);
            faultTrackWorkBO.setRecCreator(TokenUtils.getCurrentPersonId());
            faultTrackWorkBO.setRecCreateTime(DateUtils.getDate());
            faultTrackWorkBO.setRecId(UUID.randomUUID().toString());
            faultTrackWorkBO.setDispatchUserId(CommonConstants.BLANK);
            faultTrackWorkBO.setDispatchTime(CommonConstants.BLANK);
            faultTrackWorkBO.setRecStatus("10");
            faultTrackWorkMapper.insert(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class));
            // 待办逻辑处理
            TrackQueryResDTO trackRes = faultTrackMapper.detail(FaultBaseNoReqDTO.builder().faultNo(faultNo).faultTrackNo(faultTrackNo).faultWorkNo(faultWorkNo).build());
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode(
                    CommonConstants.DM_VEHICLE_SPECIALTY_CODE, CommonConstants.ZERO_ONE_STRING);
            List<String> cos = Arrays.asList(dictionaries.getItemEname().split(CommonConstants.COMMA));
            if (cos.contains(trackRes.getMajorCode())) {
                dictionaries = dictService.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE,
                        CommonConstants.ZERO_FOUR_STRING);
            } else {
                dictionaries = dictService.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE,
                        CommonConstants.ZERO_THREE_STRING);
            }
            overTodoService.insertTodoWithUserRoleAndOrg("【" + trackRes.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                    faultTrackWorkBO.getRecId(), faultWorkNo, CommonConstants.DM_007, dictionaries.getItemCname(),
                    "故障跟踪派工", "DMFM0011", "EAM", "10", BpmnFlowEnum.FAULT_TRACK.value());
            return;
        }
        // 更新两张表
        faultTrackMapper.update(BeanUtils.convert(faultTrackBO, FaultTrackDO.class),
                new UpdateWrapper<FaultTrackDO>()
                        .eq(ColsConstants.FAULT_NO, faultNo)
                        .eq(ColsConstants.FAULT_TRACK_NO, exist.getFaultTrackNo()));
        faultTrackWorkMapper.update(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class),
                new UpdateWrapper<FaultTrackWorkDO>()
                        .eq(ColsConstants.FAULT_TRACK_NO, exist.getFaultTrackNo()));
    }


    @Deprecated
    public void buildTrackWork(FaultTrackWorkBO faultTrackWorkBO, FaultTrackBO faultTrackBO) {
        try {
            String faultNo = faultTrackBO.getFaultNo();
            String faultTrackNo = faultTrackBO.getFaultTrackNo();
            String faultWorkNo = faultTrackBO.getFaultWorkNo();
            // todo 原逻辑,由于结合现系统设计无法进入if语句块，先注释了
            // Date nowDate = new Date();
            // String tackStartDate = faultTrackBO.getTrackStartDate();
            // String tackEndDate = faultTrackBO.getTrackEndDate();
            // SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            // int trackCycle = faultTrackBO.getTrackCycle();
            // Date startDate = dateTimeFormat.parse(tackStartDate);
            // Date endDate1 = dateTimeFormat.parse(tackEndDate);
            // // 计算当前日期与跟踪开始日期之间的天数差
            // int startDays = (int) ((nowDate.getTime() - startDate.getTime()) / 86400000L);
            // // 计算endDate与当前日期之间的天数差
            // int endDays = (int) ((endDate1.getTime() - nowDate.getTime()) / 86400000L);
            // if (startDays >= 0 && endDays >= 0) {
            //     //当前日期的天数差和 - 1是否能被跟踪周期整除。
            //     if ((startDays - 1) % trackCycle == 0) {
            faultTrackWorkBO.setFaultTrackNo(faultTrackNo);
            String maxCode = faultTrackWorkMapper.selectMaxCode();
            String nextCode = CodeUtils.getNextCode(maxCode, "GTW");
            faultTrackWorkBO.setFaultTrackWorkNo(nextCode);
            faultTrackWorkBO.setRecCreator(TokenUtils.getCurrentPersonId());
            faultTrackWorkBO.setRecCreateTime(DateUtils.getDate());
            faultTrackWorkBO.setRecId(UUID.randomUUID().toString());
            faultTrackWorkBO.setDispatchUserId(CommonConstants.BLANK);
            faultTrackWorkBO.setDispatchTime(CommonConstants.BLANK);
            // 待派工
            faultTrackWorkBO.setRecStatus("10");
            faultTrackWorkMapper.insert(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class));
            TrackQueryResDTO dmfm09 = faultTrackMapper.detail(FaultBaseNoReqDTO.builder().faultNo(faultNo).faultTrackNo(faultTrackNo).faultWorkNo(faultWorkNo).build());
            String majorCode = dmfm09.getMajorCode();
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode(
                    CommonConstants.DM_VEHICLE_SPECIALTY_CODE, CommonConstants.ZERO_ONE_STRING);
            List<String> cos = Arrays.asList(dictionaries.getItemEname().split(CommonConstants.COMMA));
            String zcStepOrg;
            Dictionaries matchControl;
            if (cos.contains(majorCode)) {
                matchControl = dictService.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE,
                        CommonConstants.ZERO_FOUR_STRING);
            } else {
                matchControl = dictService.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE,
                        CommonConstants.ZERO_THREE_STRING);
            }
            zcStepOrg = matchControl.getItemCname();
            overTodoService.insertTodoWithUserRoleAndOrg("【" + dmfm09.getMajorName() + CommonConstants.FAULT_CONTENT_END, faultTrackWorkBO.getRecId(), faultWorkNo, CommonConstants.DM_007, zcStepOrg, "故障跟踪派工", "DMFM0011", "EAM", "10",BpmnFlowEnum.FAULT_TRACK.value());
        } catch (Exception e) {
            log.error("save error", e);
        }
    }


    @Override
    public TrackQueryResDTO trackDetail(FaultBaseNoReqDTO reqDTO) {
        return faultTrackMapper.detail(reqDTO);
    }

}
