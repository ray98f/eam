package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.FaultTrackBO;
import com.wzmtr.eam.constant.Cols;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackMapper;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.*;
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
public class TrackQueryServiceImpl implements TrackQueryService {
    @Autowired
    private FaultTrackMapper faultTrackMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private IDictionariesService dictService;

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
        List<String> listName = Arrays.asList("跟踪单号", "故障编号", "对象名称", "故障现象", "故障原因", "故障处理", "转跟踪人员", "转跟踪时间", "跟踪期限", "跟踪周期", "跟踪结果", "跟踪状态");
        List<TrackQueryResDTO> res = faultTrackMapper.query(reqDTO);
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
    @Transactional(rollbackFor = Exception.class)
    public void save(FaultTrackBO bo) {
        FaultTrackDO faultTrackDO = __BeanUtil.convert(bo, FaultTrackDO.class);
        String faultNo = bo.getFaultNo();
        if (StringUtils.isEmpty(faultNo)) {
            return;
        }
        FaultTrackDO faultTrackDO1 = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq(Cols.FAULT_NO, faultNo));
        // 根据faultNo判断 不存在则插入，存在即更新
        if (null == faultTrackDO1) {
            String maxCode = faultTrackMapper.selectMaxCode();
            faultTrackDO.setFaultTrackNo(CodeUtils.getNextCode(maxCode, "GT"));
            // 生成跟踪单
            faultTrackMapper.insert(faultTrackDO);
            // 生成跟踪工单
            // _buildTrackWork();
            return;
        }
        //更新fault_track表
        faultTrackMapper.update(faultTrackDO, new UpdateWrapper<FaultTrackDO>().eq(Cols.FAULT_NO, faultNo).eq(Cols.FAULT_TRACK_NO, faultTrackDO1.getFaultTrackNo()));
    }


    // public void _buildTrackWork() {
    //     String tackStartDate = dmfm21.getTrackStartDate();
    //     String tackEndDate = dmfm21.getTrackEndDate();
    //     int trackCycle = dmfm21.getTrackCycle().intValue();
    //     SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    //     Date startDate = dateTimeFormat.parse(tackStartDate);
    //     Date endDate1 = dateTimeFormat.parse(tackEndDate);
    //     int startDays = (int) ((nowDate.getTime() - startDate.getTime()) / 86400000L);
    //     int endDays = (int) ((endDate1.getTime() - nowDate.getTime()) / 86400000L);
    //     if (startDays >= 0 && endDays >= 0) {
    //         int day = (startDays - 1) % trackCycle;
    //         if (day == 0) {
    //             // String seqTypeId = "EAM_FTRACK_WORK_NO";
    //             // String[] args = {"s1", "s2", "s3"};
    //             // String faultTrackWorkNo = SequenceGenerator.getNextSequence(seqTypeId, args);
    //             DMFM22 dmfm22 = new DMFM22();
    //             dmfm22.setFaultTrackNo(dmfm21.getFaultTrackNo());
    //             dmfm22.setFaultTrackWorkNo(faultTrackWorkNo);
    //             dmfm22.setRecCreator(dmfm21.getRecCreator());
    //             dmfm22.setRecCreateTime(dateTimeFormat.format(new Date()));
    //             dmfm22.setRecId(UUID.randomUUID().toString());
    //             dmfm22.setRecStatus("10");
    //             this.dao.insert("DMFM22.insert", dmfm22);
    //             String currentUser = "EAM系统";
    //             String faultWorkNo = dmfm21.getFaultWorkNo();
    //             String faultNo = dmfm21.getFaultNo();
    //             String faultTrackNo = dmfm21.getFaultTrackNo();
    //             Map<Object, Object> map = new HashMap<>();
    //             map.put("faultNo", faultNo);
    //             map.put("faultWorkNo", faultWorkNo);
    //             map.put("faultTrackNo", faultTrackNo);
    //             List<Map> list = this.dao.query("DMFM09.query", map, 0, -999999);
    //             Map dmfm09 = list.get(0);
    //             String lineCode = dmfm09.get("lineCode").toString();
    //             String majorCode = (String) dmfm09.get("majorCode");
    //             String majorName = dmfm09.get("majorName").toString();
    //             String content = "";
    //             if (cos.contains(majorCode)) {
    //                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "04", "1");
    //                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm22.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障跟踪派工", "DMFM0011", currentUser, majorCode, lineCode, "10", content);
    //             } else {
    //                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
    //                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm22.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障跟踪派工", "DMFM0011", currentUser, majorCode, lineCode, "10", content);
    //             }
    //         }
    //     }
    // }


    @Override
    public TrackQueryResDTO trackDetail(FaultBaseNoReqDTO reqDTO) {
        // faultTrackNo //faultNo
        return faultTrackMapper.detail(reqDTO);
    }

}
