package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.FaultTrackBO;
import com.wzmtr.eam.bizobject.export.FaultTrackExportBO;
import com.wzmtr.eam.bizobject.FaultTrackWorkBO;
import com.wzmtr.eam.constant.Cols;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dataobject.FaultTrackWorkDO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultTrackSaveReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackWorkMapper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    @Override
    public Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackQueryResDTO> list = faultTrackMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getFaultWorkNo(), reqDTO.getLineCode(), reqDTO.getMajorCode(), reqDTO.getObjectCode(), reqDTO.getPositionCode(), reqDTO.getSystemCode(), reqDTO.getObjectName(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO) {
        FaultInfoDO faultInfo = faultTrackMapper.faultDetail(reqDTO);
        FaultDetailResDTO faultOrder = faultTrackMapper.faultOrderDetail(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (faultInfo == null) {
            return faultOrder;
        }
        return assemblyResDTO(faultInfo, faultOrder);
    }

    private FaultDetailResDTO assemblyResDTO(FaultInfoDO faultInfo, FaultDetailResDTO faultOrder) {
        FaultDetailResDTO res = BeanUtils.copy(faultInfo, faultOrder);
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
            res.setMaintenance("true".equals(faultInfo.getExt4()) ? Boolean.TRUE : Boolean.FALSE);
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
        if (CollectionUtil.isNotEmpty(reqDTO.getIds())) {
            List<String> ids = reqDTO.getIds();
            ids.forEach(a -> {
                TrackQueryResDTO bo = new TrackQueryResDTO();
                bo.setFaultTrackNo(a);
                bo.setRecStatus("99");
                bo.setRecRevisor(TokenUtil.getCurrentPersonId());
                bo.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                bo.setExt1("");
                faultTrackMapper.cancellGenZ(bo);
            });
        }
        // faultTrackNo
    }

    @Override
    public void export(TrackQueryReqDTO reqDTO, HttpServletResponse response) {
        List<TrackQueryResDTO> res = faultTrackMapper.query(reqDTO);
        List<FaultTrackExportBO> exportList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(res)) {
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
            throw new CommonException(ErrorCode.NORMAL_ERROR);
        }
    }

    @Override
    public void save(FaultTrackSaveReqDTO req) {
        String faultNo = req.getFaultNo();
        String faultWorkNo = req.getFaultWorkNo();
        Assert.isTrue(StringUtils.isNotEmpty(faultNo) && StringUtils.isNotEmpty(faultWorkNo), ErrorCode.PARAM_ERROR);
        FaultTrackBO faultTrackBO = req.toFaultTrackBO(req);
        FaultTrackWorkBO faultTrackWorkBO = req.toFaultTrackWorkBO(req);

        FaultTrackDO exist = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq(Cols.FAULT_NO, faultNo));
        TrackQueryServiceImpl proxy = (TrackQueryServiceImpl) AopContext.currentProxy();
        proxy._save(exist, faultTrackBO, faultTrackWorkBO, faultNo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void _save(FaultTrackDO exist, FaultTrackBO faultTrackBO, FaultTrackWorkBO faultTrackWorkBO, String faultNo) {
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
            faultTrackWorkBO.setRecCreator(TokenUtil.getCurrentPersonId());
            faultTrackWorkBO.setRecCreateTime(DateUtil.getDate());
            faultTrackWorkBO.setRecId(UUID.randomUUID().toString());
            faultTrackWorkBO.setDispatchUserId(CommonConstants.BLANK);
            faultTrackWorkBO.setDispatchTime(CommonConstants.BLANK);
            faultTrackWorkBO.setRecStatus("10");
            faultTrackWorkMapper.insert(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class));
            // 待办逻辑处理
            TrackQueryResDTO trackRes = faultTrackMapper.detail(FaultBaseNoReqDTO.builder().faultNo(faultNo).faultTrackNo(faultTrackNo).faultWorkNo(faultWorkNo).build());
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.vehicleSpecialty", "01");
            List<String> cos = Arrays.asList(dictionaries.getItemEname().split(CommonConstants.COMMA));
            if (cos.contains(trackRes.getMajorCode())) {
                dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "04");
            } else {
                dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "03");
            }
            overTodoService.insertTodoWithUserGroupAndOrg("【" + trackRes.getMajorName() + "】故障管理流程", faultTrackWorkBO.getRecId(), faultWorkNo, "DM_007", dictionaries.getItemCname(), "故障跟踪派工", "DMFM0011", "EAM", "10");
            return;
        }
        // 更新两张表
        faultTrackMapper.update(BeanUtils.convert(faultTrackBO, FaultTrackDO.class), new UpdateWrapper<FaultTrackDO>().eq(Cols.FAULT_NO, faultNo).eq(Cols.FAULT_TRACK_NO, exist.getFaultTrackNo()));
        faultTrackWorkMapper.update(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class), new UpdateWrapper<FaultTrackWorkDO>().eq(Cols.FAULT_TRACK_NO, exist.getFaultTrackNo()));
    }


    @Deprecated
    public void _buildTrackWork(FaultTrackWorkBO faultTrackWorkBO, FaultTrackBO faultTrackBO) {
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
            faultTrackWorkBO.setRecCreator(TokenUtil.getCurrentPersonId());
            faultTrackWorkBO.setRecCreateTime(DateUtil.getDate());
            faultTrackWorkBO.setRecId(UUID.randomUUID().toString());
            faultTrackWorkBO.setDispatchUserId(CommonConstants.BLANK);
            faultTrackWorkBO.setDispatchTime(CommonConstants.BLANK);
            // 待派工
            faultTrackWorkBO.setRecStatus("10");
            faultTrackWorkMapper.insert(BeanUtils.convert(faultTrackWorkBO, FaultTrackWorkDO.class));
            TrackQueryResDTO dmfm09 = faultTrackMapper.detail(FaultBaseNoReqDTO.builder().faultNo(faultNo).faultTrackNo(faultTrackNo).faultWorkNo(faultWorkNo).build());
            String majorCode = dmfm09.getMajorCode();
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.vehicleSpecialty", "01");
            List<String> cos = Arrays.asList(dictionaries.getItemEname().split(CommonConstants.COMMA));
            String zcStepOrg;
            if (cos.contains(majorCode)) {
                Dictionaries matchControl = dictService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "04");
                zcStepOrg = matchControl.getItemCname();
            } else {
                Dictionaries matchControl = dictService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "03");
                zcStepOrg = matchControl.getItemCname();
            }
            overTodoService.insertTodoWithUserGroupAndOrg("【" + dmfm09.getMajorName() + "】故障管理流程", faultTrackWorkBO.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障跟踪派工", "DMFM0011", "EAM", "10");
        } catch (Exception e) {
            log.error("save error", e);
        }
    }


    @Override
    public TrackQueryResDTO trackDetail(FaultBaseNoReqDTO reqDTO) {
        // faultTrackNo //faultNo
        return faultTrackMapper.detail(reqDTO);
    }

}
