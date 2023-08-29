package com.wzmtr.eam.impl.statistic;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bo.StatisticBO;
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
    public CarFaultQueryResDTO query(CarFaultQueryReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getStartTime()) && StringUtils.isEmpty(reqDTO.getEndTime())) {
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
        List<CarFaultQueryResDTO> query = carFaultMapper.query(reqDTO.getObjectCode(), reqDTO.getEndTime(), reqDTO.getStartTime());
        if (CollectionUtil.isEmpty(query)) {
            return new CarFaultQueryResDTO();
        }
        return getCarFaultQueryResDTO(query);
    }

    private static CarFaultQueryResDTO getCarFaultQueryResDTO(List<CarFaultQueryResDTO> query) {
        TreeSet<String> titleData = new TreeSet<>();
        TreeSet<String> monthData = new TreeSet<>();
        Map<String, List<Integer>> tableData1 = new HashMap<>();
        query.forEach(a -> {
            titleData.add(a.getObjectName());
            monthData.add(a.getFillinTime());
        });
        for (String title : titleData) {
            List<Integer> endData = new ArrayList<>();
            for (String date1 : monthData) {
                int flag = 0;
                for (CarFaultQueryResDTO res : query) {
                    if (title.equals(res.getObjectName()) && date1.equals(res.getFillinTime())) {
                        endData.add(res.getFaultCount());
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    endData.add(0);
                }
            }
            tableData1.put(title, endData);
        }
        List<Map<String, Object>> tableData = new ArrayList<>();
        for (String title : titleData) {
            Map<String, Object> map1 = new HashMap<>();
            for (String table : tableData1.keySet()) {
                if (title.equals(table)) {
                    map1.put("name", title);
                    map1.put("type", "line");
                    map1.put("tiled", "总量");
                    map1.put("data", tableData1.get(title));
                    tableData.add(map1);
                }
            }
        }
        CarFaultQueryResDTO carFaultQueryResDTO = new CarFaultQueryResDTO();
        carFaultQueryResDTO.setTableData2(tableData);
        carFaultQueryResDTO.setMonthData(monthData);
        carFaultQueryResDTO.setTitleData(titleData);
        return carFaultQueryResDTO;
    }

    @Deprecated
    private static CarFaultQueryResDTO getCarFaultQueryResDTO2(List<CarFaultQueryResDTO> query) {
        TreeSet<String> titleData = new TreeSet<>();
        TreeSet<String> monthData = new TreeSet<>();
        Map<String, List<Integer>> faultCountMap = new HashMap<>();
        query.forEach(carFault -> {
            String objectName = carFault.getObjectName();
            int faultCount = carFault.getFaultCount();
            if (faultCountMap.containsKey(objectName)) {
                List<Integer> faultCountList = faultCountMap.get(objectName);
                faultCountList.add(faultCount);
            } else {
                List<Integer> faultCountList = new ArrayList<>();
                faultCountList.add(faultCount);
                faultCountMap.put(objectName, faultCountList);
            }
            monthData.add(carFault.getFillinTime());
            titleData.add(carFault.getObjectName());
        });
        List<StatisticBO> statisticBOS = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : faultCountMap.entrySet()) {
            StatisticBO statisticBO = new StatisticBO();
            String objName = entry.getKey();
            List<Integer> faultCountList = entry.getValue();
            statisticBO.setCount(faultCountList);
            statisticBO.setObjName(objName);
            statisticBOS.add(statisticBO);
        }
        CarFaultQueryResDTO carFaultQueryResDTO = new CarFaultQueryResDTO();
        carFaultQueryResDTO.setMonthData(monthData);
        carFaultQueryResDTO.setTitleData(titleData);
        carFaultQueryResDTO.setTableData(statisticBOS);
        return carFaultQueryResDTO;
    }

    @Override
    public ReliabilityListResDTO reliabilityQuery(FailreRateQueryReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getStartTime()) && StringUtils.isEmpty(reqDTO.getEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
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
            calendar.add(Calendar.YEAR, 1);
            reqDTO.setEndTime(sdf.format(calendar.getTime()));
        }
        // 售票机可靠度
        List<ReliabilityResDTO> queryTicketFault = reliabilityMapper.queryTicketFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 进出站闸机可靠度
        List<ReliabilityResDTO> queryGateBrakeFault = reliabilityMapper.queryGateBrakeFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 自动扶梯可靠度
        List<ReliabilityResDTO> queryEscalatorFault = reliabilityMapper.queryEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 垂直扶梯可靠度
        List<ReliabilityResDTO> queryVerticalEscalatorFault = reliabilityMapper.queryVerticalEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 列车乘客信息系统可靠度
        List<ReliabilityResDTO> queryTrainPassengerInformationFault = reliabilityMapper.queryTrainPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 车站乘客信息系统可靠度
        List<ReliabilityResDTO> queryStationPassengerInformationFault = reliabilityMapper.queryStationPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        // 消防设备可靠度
        List<ReliabilityResDTO> queryFireFightingEquipmentFault = reliabilityMapper.queryFireFightingEquipmentFault(reqDTO.getEndTime(), reqDTO.getStartTime());
        ReliabilityListResDTO reliabilityListResDTO = new ReliabilityListResDTO();
        reliabilityListResDTO.setQueryTicketFault(queryTicketFault);
        reliabilityListResDTO.setQueryGateBrakeFault(queryGateBrakeFault);
        reliabilityListResDTO.setQueryEscalatorFault(queryEscalatorFault);
        reliabilityListResDTO.setQueryVerticalEscalatorFault(queryVerticalEscalatorFault);
        reliabilityListResDTO.setQueryTrainPassengerInformationFault(queryTrainPassengerInformationFault);
        reliabilityListResDTO.setQueryStationPassengerInformationFault(queryStationPassengerInformationFault);
        reliabilityListResDTO.setQueryFireFightingEquipmentFault(queryFireFightingEquipmentFault);
        return reliabilityListResDTO;
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
        // PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        List<RAMSResDTO> records = ramsMapper.query4AQYYZB();
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
    public List<SystemFaultsResDTO> queryresult3(String startDate, String endDate, String sys) {
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

    /**
     * 故障影响统计
     */
    public List<RAMSResDTO> queryresult2(String startDate, String endDate) {
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
        DecimalFormat df = new DecimalFormat("#0.00");
        List<RAMSResDTO> ramsResDTOS = ramsMapper.queryresult2(startDate, endDate);
        ramsResDTOS.forEach(a -> {
            double lateZ = 0.0D;
            double noServiceZ = 0.0D;
            double miles = Double.parseDouble(a.getMiles());
            if (Double.parseDouble(a.getLate()) == 0.0D) {
                lateZ = Double.parseDouble("0");
            } else {
                lateZ = Double.parseDouble(a.getLate()) * 1000000.0D / miles / 4.0D;
            }
            if (Double.parseDouble(a.getNoService()) == 0.0D) {
                noServiceZ = Double.parseDouble("0");
            } else {
                noServiceZ = Double.parseDouble(a.getNoService()) * 1000000.0D / miles / 4.0D;
            }
            a.setLate(df.format(lateZ));
            a.setNoService(df.format(noServiceZ));
        });
        return ramsResDTOS;
    }

    @Override
    public List<RAMSResDTO> querySysPerform() {
        List<RAMSResDTO> ramsResDTOS = ramsMapper.querySysPerform();
        System.out.println(ramsResDTOS);
        ramsResDTOS.forEach(a -> {
            switch (a.getModuleName()) {
                case "01":
                case "02":
                case "03":
                case "04":
                    rebuildBlock4SP(a, 0, "车门系统", "10000", "7000");
                    break;
                case "12":
                    rebuildBlock4SP(a, 1, "制动系统", "49500", "49500");
                    break;
                case "13":
                    rebuildBlock4SP(a, 2, "空调系统", "210000", "46000");
                    break;
                case "14":
                    rebuildBlock4SP(a, 3, "转向架", "409500", "409500");
                    break;
                case "17":
                    rebuildBlock4SP(a, 4, "PIDS", "191000", "91000");
                    break;
                case "10":
                    rebuildBlock4SP(a, 5, "网络系统", "254000", "254000");
                    break;
                case "05":
                case "08":
                case "09":
                case "21":
                    rebuildBlock4SP(a, 6, "车体结构及车身内部", "204800", "204800");
                    break;
                case "06":
                case "07":
                    rebuildBlock4SP(a, 7, "通道与车钩系统", "409500", "204800");
                    break;
                case "18":
                case "19":
                    rebuildBlock4SP(a, 8, "牵引设备系统", "36588", "11000");
                    break;
                case "11":
                case "15":
                case "16":
                case "20":
                    rebuildBlock4SP(a, 9, "辅助供电设备系统", "647749", "323825");
                    break;
            }
        });
        return ramsResDTOS;
    }

    public void rebuildBlock4SP(RAMSResDTO map, int rowNo, String moduleName, String contractZB_LATE, String contractZB_NOS) {
        DecimalFormat df = new DecimalFormat("#0");
        String NUM_LATE = map.getNumLate();
        String NUM_NOS = map.getNumNos();
        if (rowNo > 0) {
            map.setModuleName(moduleName);
            map.setNumLate(df.format(Double.parseDouble(NUM_LATE)));
            map.setNumNos(df.format(Double.parseDouble(NUM_NOS)));
        } else {
            map.setModuleName(moduleName);
            map.setNumLate(NUM_LATE);
            map.setNumNos(NUM_NOS);
            map.setContractZBLATE(contractZB_LATE);
            map.setContractZBNOS(contractZB_NOS);
        }
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#0");
        System.out.println(df.format(Double.parseDouble("0") + 1));
    }
}


