package com.wzmtr.eam.impl.statistic;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.mapper.equipment.GearboxChangeOilMapper;
import com.wzmtr.eam.mapper.equipment.GeneralSurveyMapper;
import com.wzmtr.eam.mapper.equipment.WheelsetLathingMapper;
import com.wzmtr.eam.mapper.statistic.*;
import com.wzmtr.eam.service.statistic.StatisticService;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:01
 */
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private CarFaultMapper carFaultMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private FailureRateMapper failureRateMapper;
    @Autowired
    private ReliabilityMapper reliabilityMapper;
    @Autowired
    private OneCarOneGearMapper oneCarOneGearMapper;
    @Autowired
    private GearboxChangeOilMapper gearboxChangeOilMapper;
    @Autowired
    private WheelsetLathingMapper wheelsetLathingMapper;
    @Autowired
    private GeneralSurveyMapper generalSurveyMapper;
    @Autowired
    private RAMSMapper ramsMapper;

    @Override
    public List<FailureRateResDTO> query(FailreRateQueryReqDTO reqDTO) {
        List<FailureRateResDTO> list = new ArrayList<>();
        FailureRateResDTO vehicleRate = failureRateMapper.vehicleRate(reqDTO);
        FailureRateResDTO signalRate = failureRateMapper.signalRate(reqDTO);
        FailureRateResDTO powerRate = failureRateMapper.powerRate(reqDTO);
        FailureRateResDTO PSDrate = failureRateMapper.PSDrate(reqDTO);
        list.add(vehicleRate);
        list.add(signalRate);
        list.add(powerRate);
        list.add(PSDrate);
        return list;
    }

    @Override
    public Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<MaterialResDTO> page = materialMapper.query(reqDTO.of(), reqDTO.getPlanName(), reqDTO.getMatName(), reqDTO.getStartTime(), reqDTO.getEndTime(), reqDTO.getTrainNo());
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return new Page<>();
        }
        return page;
    }

    @Override
    public Page<CarFaultQueryResDTO> query(CarFaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        if (StringUtils.isEmpty(reqDTO.getStartTime()) || StringUtils.isEmpty(reqDTO.getEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String starDate = calendar.get(Calendar.YEAR) + "-01-01";
            Date parse = null;
            try {
                parse = sdf.parse(starDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            calendar.setTime(parse);
            reqDTO.setStartTime(sdf.format(calendar.getTime()));
            reqDTO.setEndTime(sdf.format(new Date()));
        }
        // todo 不懂 这里可能有字符串处理
        // if (inInfo.get("inqu_status-0-objectCode") != null && !inInfo.get("inqu_status-0-objectCode").toString().trim().equals("")) {
        //     /* 39 */         String objectCode = inInfo.get("inqu_status-0-objectCode").toString().substring(2, inInfo.get("inqu_status-0-objectCode").toString().length() - 2);
        //     /* 40 */         objectCode = ("\"" + objectCode + "\"").replace("\"", "'");
        //     /* 41 */         map.put("objectCode", objectCode);
        //     /*    */       }
        Page<CarFaultQueryResDTO> query = carFaultMapper.query(reqDTO.of(), reqDTO.getObjectCode(), reqDTO.getEndTime(), reqDTO.getStartTime());
        if (CollectionUtil.isEmpty(query.getRecords())) {
            return new Page<>();
        }
        return query;
    }

    @Override
    public List<ReliabilityResDTO> reliabilityQuery(FailreRateQueryReqDTO reqDTO) {
        List<ReliabilityResDTO> reliabilityResDTOS = new ArrayList<>();
        // 售票机可靠度
        ReliabilityResDTO queryTicketFault = reliabilityMapper.queryTicketFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 进出站闸机可靠度
        ReliabilityResDTO queryGateBrakeFault = reliabilityMapper.queryGateBrakeFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 自动扶梯可靠度
        ReliabilityResDTO queryEscalatorFault = reliabilityMapper.queryEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 垂直扶梯可靠度
        ReliabilityResDTO queryVerticalEscalatorFault = reliabilityMapper.queryVerticalEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 列车乘客信息系统可靠度
        ReliabilityResDTO queryTrainPassengerInformationFault = reliabilityMapper.queryTrainPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 车站乘客信息系统可靠度
        ReliabilityResDTO queryStationPassengerInformationFault = reliabilityMapper.queryStationPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 消防设备可靠度
        ReliabilityResDTO queryFireFightingEquipmentFault = reliabilityMapper.queryFireFightingEquipmentFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        reliabilityResDTOS.add(queryTicketFault);
        reliabilityResDTOS.add(queryTrainPassengerInformationFault);
        reliabilityResDTOS.add(queryFireFightingEquipmentFault);
        reliabilityResDTOS.add(queryStationPassengerInformationFault);
        reliabilityResDTOS.add(queryVerticalEscalatorFault);
        reliabilityResDTOS.add(queryEscalatorFault);
        reliabilityResDTOS.add(queryGateBrakeFault);
        if (CollectionUtil.isEmpty(reliabilityResDTOS)) {
            return new ArrayList<>();
        }
        return reliabilityResDTOS;
    }

    @Override
    public OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearQueryReqDTO reqDTO) {
        OneCarOneGearResDTO query = oneCarOneGearMapper.query(reqDTO.getEndTime(), reqDTO.getStartTime(), reqDTO.getEquipName());
        OneCarOneGearResDTO summary = oneCarOneGearMapper.querySummary(reqDTO.getEndTime(), reqDTO.getStartTime(), reqDTO.getEquipName());
        if (query != null) {
            summary.setStartUseDate(query.getStartUseDate());
            summary.setManufactureDate(query.getManufactureDate());
            summary.setTrainNo(query.getTrainNo());
        }
        return summary;
    }

    @Override
    public Page<InspectionJobListResDTO> querydmer3(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.querydmer3(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER4(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER4(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER5(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER5(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<FaultDetailResDTO> queryFMHistory(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryFMHistory(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<TrackQueryResDTO> queryDMFM21(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryDMFM21(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<PartReplaceResDTO> querydmdm20(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.querydmdm20(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<GearboxChangeOilResDTO> pageGearboxChangeOil(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return gearboxChangeOilMapper.pageGearboxChangeOil(reqDTO.of(), reqDTO.getEquipName());
    }

    /**
     * 轮对镟修记录
     */
    @Override
    public Page<WheelsetLathingResDTO> pageWheelsetLathing(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return wheelsetLathingMapper.pageWheelsetLathing(reqDTO.of(), reqDTO.getEquipName());
    }

    /**
     * 普查与技改
     */
    @Override
    public Page<GeneralSurveyResDTO> pageGeneralSurvey(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return generalSurveyMapper.pageGeneralSurvey(reqDTO.of(), reqDTO.getEquipName());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER2(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER2(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER1(OneCarOneGearQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER1(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public RAMSResDTO query4AQYYZB(RAMSTimeReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        List<RAMSResDTO> records = ramsMapper.query4AQYYZB(reqDTO.of(), reqDTO.getStartTime(), reqDTO.getEndTime());
        if (CollectionUtil.isEmpty(records)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        RAMSResDTO ramsResDTO = records.get(0);
        String affect11 = ramsResDTO.getAffect11();
        String millionMiles = ramsResDTO.getMillionMiles();
        String affect21 = ramsResDTO.getAffect21();
        String affect12 = df.format(Double.parseDouble(affect11) / Double.parseDouble(millionMiles) * 4.0D);
        String affect22 = df.format(Double.parseDouble(affect21) / Double.parseDouble(millionMiles) * 4.0D);
        if (Double.parseDouble(affect12) > 1.5D) {
            ramsResDTO.setAffect11("未达标");
        } else {
            ramsResDTO.setAffect11("达标");
        }
        if (Double.parseDouble(affect22) > (4.5D)) {
            ramsResDTO.setAffect21Performance("未达标");
        } else {
            ramsResDTO.setAffect21Performance("达标");
        }
        return ramsResDTO;
    }

    @Override
    public List<SystemFaultsResDTO> systemFault(String startDate, String endDate, String sys) {
        if (StringUtils.isEmpty(sys)) {
            sys = "01";
        }
        if (StringUtils.isNotEmpty(startDate)) {
            startDate = startDate.substring(0, 7);
        } else {
            startDate = _getLastMouths(11);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endDate = endDate.substring(0, 7);
        } else {
            endDate = _getLastMouths(0);
        }
        Set<String> moduleIds = new HashSet<>();
        switch (sys) {
            case "02":
                moduleIds.add("12");
                break;
            case "03":
                moduleIds.add("13");
                break;
            case "04":
                moduleIds.add("14");
                break;
            case "05":
                moduleIds.add("17");
                break;
            case "06":
                moduleIds.add("10");
                break;
            case "07":
                moduleIds.add("05");
                moduleIds.add("08");
                moduleIds.add("09");
                moduleIds.add("21");
                break;
            case "08":
                moduleIds.add("06");
                moduleIds.add("07");
                break;
            case "09":
                moduleIds.add("18");
                moduleIds.add("19");
                break;
            case "10":
                moduleIds.add("11");
                moduleIds.add("15");
                moduleIds.add("16");
                moduleIds.add("20");
                break;
            case "01":
            default:
                moduleIds.add("01");
                moduleIds.add("02");
                moduleIds.add("03");
                moduleIds.add("04");
                break;
        }

        List<SystemFaultsResDTO> systemFaultsResDTOS = ramsMapper.queryFautTypeByMonthBySys(moduleIds, startDate, endDate);
        systemFaultsResDTOS.forEach(a -> a.setCompliance(a.getNoyfsum() - a.getMiles() > 0));
        return systemFaultsResDTOS;
    }

    private static String _getLastMouths(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }
}


