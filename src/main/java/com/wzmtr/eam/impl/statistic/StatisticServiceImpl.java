package com.wzmtr.eam.impl.statistic;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.statistic.*;
import com.wzmtr.eam.dto.res.*;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.enums.SystemType;
import com.wzmtr.eam.mapper.equipment.GearboxChangeOilMapper;
import com.wzmtr.eam.mapper.equipment.GeneralSurveyMapper;
import com.wzmtr.eam.mapper.equipment.WheelsetLathingMapper;
import com.wzmtr.eam.mapper.statistic.*;
import com.wzmtr.eam.service.statistic.StatisticService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
    private static final List<String> ignore = Arrays.asList("NOYF", "SC", "moduleName", "contractZB", "ZB");

    @Override
    public List<FailureRateResDTO> query(FailreRateQueryReqDTO reqDTO) {
        List<FailureRateResDTO> list = new ArrayList<>();
        // 车辆系统故障率
        FailureRateResDTO vehicleRate = failureRateMapper.vehicleRate(reqDTO);
        //<!--信号系统故障率-->
        FailureRateResDTO signalRate = failureRateMapper.signalRate(reqDTO);
        // 供电系统故障率
        FailureRateResDTO powerRate = failureRateMapper.powerRate(reqDTO);
        // 屏蔽门故障率
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
        ReliabilityListResDTO reliabilityListResDTO = new ReliabilityListResDTO();
        if (reqDTO.getSystemType().contains(SystemType.TICKET)) {
            // 售票机可靠度
            List<ReliabilityResDTO> queryTicketFault = reliabilityMapper.queryTicketFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryTicketFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryTicketFaultMonth(month);
            reliabilityListResDTO.setQueryTicketFaultData(data);
            reliabilityListResDTO.setQueryTicketFault(queryTicketFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.GATE_BRAKE)) {
            // 进出站闸机可靠度
            List<ReliabilityResDTO> queryGateBrakeFault = reliabilityMapper.queryGateBrakeFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryGateBrakeFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryGateBrakeData(data);
            reliabilityListResDTO.setQueryGateBrakeMonth(month);
            reliabilityListResDTO.setQueryGateBrakeFault(queryGateBrakeFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.ESCALATOR)) {
            // 自动扶梯可靠度
            List<ReliabilityResDTO> queryEscalatorFault = reliabilityMapper.queryEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryEscalatorFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryEscalatorData(data);
            reliabilityListResDTO.setQueryEscalatorMonth(month);
            reliabilityListResDTO.setQueryEscalatorFault(queryEscalatorFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.VERTICAL_ESCALATOR)) {
            // 垂直扶梯可靠度
            List<ReliabilityResDTO> queryVerticalEscalatorFault = reliabilityMapper.queryVerticalEscalatorFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryVerticalEscalatorFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryVerticalEscalatorMonth(month);
            reliabilityListResDTO.setQueryVerticalEscalatorData(data);
            reliabilityListResDTO.setQueryVerticalEscalatorFault(queryVerticalEscalatorFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.TRAIN_PASSENGER)) {
            // 列车乘客信息系统可靠度
            List<ReliabilityResDTO> queryTrainPassengerInformationFault = reliabilityMapper.queryTrainPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryTrainPassengerInformationFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryTrainPassengerInformationMonth(month);
            reliabilityListResDTO.setQueryTrainPassengerInformationData(data);
            reliabilityListResDTO.setQueryTrainPassengerInformationFault(queryTrainPassengerInformationFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.STATION_PASSENGER)) {
            // 车站乘客信息系统可靠度
            List<ReliabilityResDTO> queryStationPassengerInformationFault = reliabilityMapper.queryStationPassengerInformationFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryStationPassengerInformationFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryStationPassengerInformationMonth(month);
            reliabilityListResDTO.setQueryStationPassengerInformationData(data);
            reliabilityListResDTO.setQueryStationPassengerInformationFault(queryStationPassengerInformationFault);
        }
        if (reqDTO.getSystemType().contains(SystemType.FIRE_FIGHTING)) {
            // 消防设备可靠度
            List<ReliabilityResDTO> queryFireFightingEquipmentFault = reliabilityMapper.queryFireFightingEquipmentFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryFireFightingEquipmentFault.forEach(a->{
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            reliabilityListResDTO.setQueryFireFightingEquipmentFaultMonth(month);
            reliabilityListResDTO.setQueryFireFightingEquipmentFaultData(data);
            reliabilityListResDTO.setQueryFireFightingEquipmentFault(queryFireFightingEquipmentFault);
        }
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

    /**
     * 一车一档部件更换记录
     */
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
    public RAMSCarResDTO query4AQYYZB() {
        // PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        List<RAMSCarResDTO> records = ramsMapper.query4AQYYZB();
        if (CollectionUtil.isEmpty(records)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        RAMSCarResDTO ramsCarResDTO = records.get(0);
        String millionMiles = ramsCarResDTO.getMillionMiles();
        String affect11 = ramsCarResDTO.getAffect11();// 1
        String affect21 = ramsCarResDTO.getAffect21();// 147
        String faultNum = ramsCarResDTO.getFaultNum();

        String affect12 = df.format(Double.parseDouble(affect11) / Double.parseDouble(millionMiles) * 4.0D);
        String affect22 = df.format(Double.parseDouble(affect21) / Double.parseDouble(millionMiles) * 4.0D);
        if (Double.parseDouble(affect12) > 1.5D) {
            ramsCarResDTO.setAffect13("未达标");
        } else {
            ramsCarResDTO.setAffect13("达标");
        }
        if (Double.parseDouble(affect22) > 4.5D) {
            ramsCarResDTO.setAffect23("未达标");
        } else {
            ramsCarResDTO.setAffect23("达标");
        }
        ramsCarResDTO.setAffect12(affect12);
        ramsCarResDTO.setAffect22(affect22);
        ramsCarResDTO.setFaultNum(faultNum);
        return ramsCarResDTO;
    }

    @Override
    public List<SystemFaultsResDTO> queryresult3(String startDate, String endDate, String sys) {
        if (sys == null) {
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
        return ramsMapper.queryFautTypeByMonthBySys(moduleIds, startDate, endDate);
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
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<RAMSResult2ResDTO> queryresult2(String startDate, String endDate) {
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
        List<RAMSResult2ResDTO> ramsResDTOS = ramsMapper.queryresult2(startDate, endDate);
        ramsResDTOS.forEach(a -> {
            double lateZ = 0.0D;
            double noServiceZ = 0.0D;
            a.setMiles(a.getMiles() == null ? "0" : a.getMiles());
            double miles = Double.parseDouble(a.getMiles()) != 0.0D ? Double.parseDouble(a.getMiles()) : 0.0D;
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
    public List<RAMSSysPerformResDTO> querySysPerform() {
        List<RAMSSysPerformResDTO> ramsResDTOS = ramsMapper.querySysPerform();
        List<RAMSResDTO> totalMilesList = ramsMapper.querytotalMiles();
        RAMSResDTO totalMiles = totalMilesList.get(0);
        System.out.println(ramsResDTOS);
        Map<String, RAMSSysPerformResDTO> map = Maps.newHashMap();
        Set<String> names = Sets.newHashSet();
        ramsResDTOS.forEach(a -> {
                    switch (a.getModuleName()) {
                        case "01":
                        case "02":
                        case "03":
                        case "04":
                            rebuildBlock4SP(a, names, "车门系统", "10000", "7000");
                            break;
                        case "12":
                            rebuildBlock4SP(a, names, "制动系统", "49500", "49500");
                            break;
                        case "13":
                            rebuildBlock4SP(a, names, "空调系统", "210000", "46000");
                            break;
                        case "14":
                            rebuildBlock4SP(a, names, "转向架", "409500", "409500");
                            break;
                        case "17":
                            rebuildBlock4SP(a, names, "PIDS", "191000", "91000");
                            break;
                        case "10":
                            rebuildBlock4SP(a, names, "网络系统", "254000", "254000");
                            break;
                        case "05":
                        case "08":
                        case "09":
                        case "21":
                            rebuildBlock4SP(a, names, "车体结构及车身内部", "204800", "204800");
                            break;
                        case "06":
                        case "07":
                            rebuildBlock4SP(a, names, "通道与车钩系统", "409500", "204800");
                            break;
                        case "18":
                        case "19":
                            rebuildBlock4SP(a, names, "牵引设备系统", "36588", "11000");
                            break;
                        case "11":
                        case "15":
                        case "16":
                        case "20":
                            rebuildBlock4SP(a, names, "辅助供电设备系统", "647749", "323825");
                            break;
                    }
                    DecimalFormat df = new DecimalFormat("#0");
                    Double MTBF_LATE;
                    if (Double.parseDouble(a.getNumLate()) == 0.0D) {
                        MTBF_LATE = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D;
                    } else {
                        MTBF_LATE = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNumLate());
                    }
                    a.setMTBF_LATE(df.format(MTBF_LATE));
                    Double MTBF_NOS;
                    if (Double.parseDouble(a.getNumNos()) == 0.0D) {
                        MTBF_NOS = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D;
                    } else {
                        MTBF_NOS = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNumNos());
                    }
                    a.setMTBF_NOS(df.format(MTBF_NOS));
                    RAMSSysPerformResDTO last = map.get(a.getModuleName());
                    if (map.containsKey(a.getModuleName())) {
                        a.setNumNos(String.valueOf(Integer.parseInt(a.getNumNos()) + Integer.parseInt(last.getNumNos())));
                        a.setMTBF_NOS(String.valueOf(Integer.parseInt(a.getMTBF_NOS()) + Integer.parseInt(last.getMTBF_NOS())));
                        map.put(a.getModuleName(), a);
                    } else {
                        map.put(a.getModuleName(), a);
                    }
                }
        );
        map.values().forEach(a -> {
            if (Double.parseDouble(a.getMTBF_LATE()) >= Double.parseDouble(a.getContractZBLATE())) {
                a.setIsDB_LATE("达标");
            } else {
                a.setIsDB_LATE("未达标");
            }
            if (Double.parseDouble(a.getMTBF_NOS()) >= Double.parseDouble(a.getContractZBNOS())) {
                a.setIsDB_NOS("达标");
            } else {
                a.setIsDB_NOS("未达标");
            }
        });
        return new ArrayList<>(map.values());
    }

    /**
     * 各系统故障情况统计
     */
    @Override
    public List<FaultConditionResDTO> queryCountFaultType() {
        List<FaultConditionResDTO> list = ramsMapper.queryCountFautType4RAMS();
        List<RAMSResDTO> ramsResDTOS = ramsMapper.querytotalMiles();
        RAMSResDTO ramsResDTO = ramsResDTOS.get(0);
        Set<String> names = Sets.newHashSet();
        Map<String, FaultConditionResDTO> map = new HashMap<>();
        for (FaultConditionResDTO a : list) {
            switch (a.getSC()) {
                case "01":
                case "02":
                case "03":
                case "04":
                    rebuildBlock(a, names, "车门系统", "9000");
                    break;
                case "12":
                    rebuildBlock(a, names, "制动系统", "43000");
                    break;
                case "13":
                    rebuildBlock(a, names, "空调系统", "85000");
                    break;
                case "14":
                    rebuildBlock(a, names, "转向架", "381000");
                    break;
                case "17":
                    rebuildBlock(a, names, "PIDS", "191000");
                    break;
                case "10":
                    rebuildBlock(a, names, "网络系统", "254000");
                    break;
                case "05":
                case "08":
                case "09":
                case "21":
                    rebuildBlock(a, names, "车体结构及车身内部", "191000");
                    break;
                case "06":
                case "07":
                    rebuildBlock(a, names, "通道与车钩系统", "381000");
                    break;
                case "18":
                case "19":
                    rebuildBlock(a, names, "牵引设备系统", "11000");
                    break;
                case "11":
                case "15":
                case "16":
                case "20":
                    rebuildBlock(a, names, "辅助供电设备系统", "38000");
                    break;
            }
            FaultConditionResDTO last = map.get(a.getModuleName());
            if (map.containsKey(a.getModuleName())) {
                a.setCRK(String.valueOf(Integer.parseInt(a.getCRK()) + Integer.parseInt(last.getCRK())));
                a.setZX(String.valueOf(Integer.parseInt(a.getZX()) + Integer.parseInt(last.getZX())));
                a.setZS(String.valueOf(Integer.parseInt(a.getZS()) + Integer.parseInt(last.getZS())));
                a.setYF(String.valueOf(Integer.parseInt(a.getYF()) + Integer.parseInt(last.getYF())));
                a.setNOYF(String.valueOf(Integer.parseInt(a.getNOYF()) + Integer.parseInt(last.getNOYF())));
                map.put(a.getModuleName(), a);
            } else {
                map.put(a.getModuleName(), a);
            }
        }
        map.values().forEach(a -> {
            DecimalFormat df = new DecimalFormat("#0");
            double ZB;
            if (Double.parseDouble(a.getNOYF()) == 0.0D) {
                ZB = Double.parseDouble(ramsResDTO.getTotalMiles()) * 4.0D / 55.0D;
            } else {
                ZB = Double.parseDouble(ramsResDTO.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNOYF());
            }
            a.setZB(df.format(ZB));
            if (ZB >= Double.parseDouble(a.getContractZB())) {
                a.setIsDB("达标");
            } else {
                a.setIsDB("未达标");
            }
        });
        return new ArrayList<>(map.values());
    }


    /**
     * RAMS 故障列表
     */
    public Page<FaultRAMSResDTO> queryRAMSFaultList(RAMSTimeReqDTO reqDTO) {
        Page<FaultRAMSResDTO> list = ramsMapper.queryRAMSFaultList(reqDTO.of(), reqDTO.getStartTime(), reqDTO.getEndTime());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        List<FaultRAMSResDTO> records = list.getRecords();
        records.forEach(a -> {
            FaultRAMSResDTO faultRAMSResDTO = ramsMapper.queryPart(a.getFaultWorkNo());
            if (null != faultRAMSResDTO) {
                String replacementName = faultRAMSResDTO.getReplacementName();
                String oldRepNo = faultRAMSResDTO.getOldRepNo();
                String newRepNo = faultRAMSResDTO.getNewRepNo();
                String operateCostTime = faultRAMSResDTO.getOperateCostTime();
                a.setReplacementName(replacementName == null ? CommonConstants.EMPTY : replacementName);
                a.setOldRepNo(oldRepNo == null ? CommonConstants.EMPTY : oldRepNo);
                a.setNewRepNo(newRepNo == null ? CommonConstants.EMPTY : newRepNo);
                a.setOperateCostTime(operateCostTime == null ? CommonConstants.EMPTY : operateCostTime);
            }
        });
        return list;
    }

    @Override
    public void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("检修作业时间", "对象号", "工单名称", "物资名称", "物资编码", "规格型号", "领用数量", "计量单位");
        List<MaterialResDTO> exportList = materialMapper.exportList(reqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (exportList != null && !exportList.isEmpty()) {
            for (MaterialResDTO resDTO : exportList) {
                Map<String, String> map = new HashMap<>();
                map.put("检修作业时间", resDTO.getRealTime());
                map.put("对象号", resDTO.getObjectName());
                map.put("工单名称", resDTO.getPlanName());
                map.put("物资名称", resDTO.getMatname());
                map.put("物资编码", resDTO.getMatcode());
                map.put("规格型号", resDTO.getSpecifi());
                map.put("领用数量", resDTO.getDeliveryNum());
                map.put("计量单位", resDTO.getUnit());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("物料统计", listName, list, null, response);
    }

    public void rebuildBlock4SP(RAMSSysPerformResDTO map, Set<String> module, String moduleName, String contractZB_LATE, String contractZB_NOS) {
        DecimalFormat df = new DecimalFormat("#0");
        String NUM_LATE = map.getNumLate();
        String NUM_NOS = map.getNumNos();
        map.setModuleName(moduleName);
        map.setContractZBLATE(contractZB_LATE);
        map.setContractZBNOS(contractZB_NOS);
        if (module.contains(moduleName)) {
            map.setNumLate(df.format(Double.parseDouble(NUM_LATE) + NUM_LATE));
            map.setNumNos(df.format(Double.parseDouble(NUM_NOS) + NUM_NOS));
        } else {
            map.setNumLate(NUM_LATE);
            map.setNumNos(NUM_NOS);
        }
    }

    public void rebuildBlock(FaultConditionResDTO map, Set<String> module, String moduleName, String contractZB) {
        DecimalFormat df = new DecimalFormat("#0");
        String ZX = map.getZX();
        String CRK = map.getCRK();
        String YF = map.getYF();
        String ZS = map.getZS();
        String NOYF = map.getNOYF();
        map.setContractZB(contractZB);
        map.setModuleName(moduleName);
        if (module.contains(moduleName)) {
            map.setZX(df.format(Double.parseDouble(ZX) + ZX));
            map.setCRK(df.format(Double.parseDouble(CRK) + CRK));
            map.setYF(df.format(Double.parseDouble(YF) + YF));
            map.setZS(df.format(Double.parseDouble(ZS) + ZS));
            map.setNOYF(df.format(Double.parseDouble(NOYF) + NOYF));
        } else {
            map.setModuleName(moduleName);
            map.setZX(ZX);
            map.setCRK(CRK);
            map.setYF(YF);
            map.setZS(ZS);
            map.setNOYF(NOYF);
        }
    }
}


