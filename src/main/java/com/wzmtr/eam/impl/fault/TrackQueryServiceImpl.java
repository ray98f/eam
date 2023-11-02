package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultOrderMapper;
import com.wzmtr.eam.mapper.fault.TrackQueryMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
public class TrackQueryServiceImpl implements TrackQueryService {
    @Autowired
    private TrackQueryMapper trackQueryMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private FaultOrderMapper faultOrderMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private IDictionariesService dictService;

    @Override
    public Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackQueryResDTO> list = trackQueryMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getFaultWorkNo(), reqDTO.getLineCode(), reqDTO.getMajorCode(), reqDTO.getObjectCode(), reqDTO.getPositionCode(), reqDTO.getSystemCode(), reqDTO.getObjectName(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO) {
        FaultInfoDO faultInfo = trackQueryMapper.faultDetail(reqDTO);
        FaultDetailResDTO faultOrder = trackQueryMapper.faultOrderDetail(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (faultInfo == null) {
            return faultOrder;
        }
        return assemblyResDTO(faultInfo, faultOrder);
    }

    private FaultDetailResDTO assemblyResDTO(FaultInfoDO faultInfo, FaultDetailResDTO faultOrder) {
        FaultDetailResDTO res = __BeanUtil.copy(faultInfo, faultOrder);
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
            res.setMaintenance(faultInfo.getExt4().equals("true") ? Boolean.TRUE : Boolean.FALSE);
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
                trackQueryMapper.cancellGenZ(bo);
            });
        }
        // faultTrackNo
    }

    @Override
    public void export(TrackQueryReqDTO reqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("跟踪单号", "故障编号", "对象名称", "故障现象", "故障原因", "故障处理", "转跟踪人员", "转跟踪时间", "跟踪期限", "跟踪周期", "跟踪结果", "跟踪状态");
        List<TrackQueryResDTO> res = trackQueryMapper.query(reqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(res)) {
            for (TrackQueryResDTO resDTO : res) {
                Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.faultTrackStatus", resDTO.getRecStatus());
                Map<String, String> map = new HashMap<>();
                map.put("跟踪单号", resDTO.getFaultTrackNo());
                map.put("故障编号", resDTO.getFaultNo());
                map.put("对象名称", resDTO.getObjectName());
                map.put("线路编码", resDTO.getLineCode());
                map.put("故障现象", resDTO.getFaultDetail());
                map.put("故障原因", resDTO.getFaultReasonDetail());
                map.put("故障处理", resDTO.getFaultActionDetail());
                map.put("转跟踪人员", resDTO.getTrackUserName());
                map.put("转跟踪时间", resDTO.getTrackTime());
                map.put("跟踪期限", resDTO.getTrackPeriod().toString());
                map.put("跟踪周期", resDTO.getTrackCycle().toString());
                map.put("跟踪结果", resDTO.getTrackResult());
                map.put("跟踪状态", dictionaries.getItemCname());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("跟踪查询信息", listName, list, null, response);
    }

    @Override
    public TrackQueryResDTO trackDetail(SidEntity reqDTO) {
        // faultTrackNo
        return trackQueryMapper.detail(reqDTO.getId());
    }


}
