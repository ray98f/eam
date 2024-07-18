package com.wzmtr.eam.impl.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.DoorFaultReqDTO;
import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialListReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearReqDTO;
import com.wzmtr.eam.dto.req.statistic.RamsTimeReqDTO;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateDetailResDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateResDTO;
import com.wzmtr.eam.dto.res.statistic.FaultConditionResDTO;
import com.wzmtr.eam.dto.res.statistic.FaultRamsResDTO;
import com.wzmtr.eam.dto.res.statistic.InspectionJobListResDTO;
import com.wzmtr.eam.dto.res.statistic.MaterialResDTO;
import com.wzmtr.eam.dto.res.statistic.OneCarOneGearResDTO;
import com.wzmtr.eam.dto.res.statistic.RamsCarResDTO;
import com.wzmtr.eam.dto.res.statistic.RamsResDTO;
import com.wzmtr.eam.dto.res.statistic.RamsResult2ResDTO;
import com.wzmtr.eam.dto.res.statistic.RamsSysPerformResDTO;
import com.wzmtr.eam.dto.res.statistic.RamsTrainReliabilityResDTO;
import com.wzmtr.eam.dto.res.statistic.ReliabilityDetailResDTO;
import com.wzmtr.eam.dto.res.statistic.ReliabilityListResDTO;
import com.wzmtr.eam.dto.res.statistic.ReliabilityResDTO;
import com.wzmtr.eam.dto.res.statistic.SubjectFaultResDTO;
import com.wzmtr.eam.dto.res.statistic.SystemFaultsResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelFaultDetailResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelFmFaultDetailResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelInspectionJobListResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelMaterialResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelRamsSysPerformResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelStatisticGearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelStatisticGeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelStatisticPartReplaceResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelStatisticWheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.statistic.excel.ExcelTrackQueryResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.OperateDailySystem;
import com.wzmtr.eam.enums.RateIndex;
import com.wzmtr.eam.enums.SystemType;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulOrderMapper;
import com.wzmtr.eam.mapper.statistic.CarFaultMapper;
import com.wzmtr.eam.mapper.statistic.FailureRateMapper;
import com.wzmtr.eam.mapper.statistic.MaterialMapper;
import com.wzmtr.eam.mapper.statistic.OneCarOneGearMapper;
import com.wzmtr.eam.mapper.statistic.RamsMapper;
import com.wzmtr.eam.mapper.statistic.ReliabilityMapper;
import com.wzmtr.eam.service.statistic.StatisticService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StreamUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:01
 */
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    public static final Double ZERO = 0.0D;
    public static final Double ONE_POINT_FIVE = 1.5D;
    public static final Double FOUR_POINT_FIVE = 4.5D;
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
    private OverhaulOrderMapper overhaulOrderMapper;
    @Autowired
    private RamsMapper ramsMapper;
    @Autowired
    private FaultExportComponent exportComponent;
    @Autowired
    private DictionariesMapper dictionariesMapper;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Override
    public void addDoorFault(DoorFaultReqDTO req) {
        if (req.getFaultNum() > req.getActionNum()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "故障次数不能大于动作次数");
        }
        Integer result = failureRateMapper.selectDoorFaultIsExist(req);
        if (result > CommonConstants.ZERO) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        req.setRecId(TokenUtils.getUuId());
        req.setRecCreator(TokenUtils.getCurrentPersonId());
        req.setRecCreateTime(DateUtils.getCurrentTime());
        failureRateMapper.addDoorFault(req);
    }

    @Override
    public void modifyDoorFault(DoorFaultReqDTO req) {
        if (req.getFaultNum() > req.getActionNum()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "故障次数不能大于动作次数");
        }
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        failureRateMapper.modifyDoorFault(req);
    }

    @Override
    public FailureRateDetailResDTO failureRateQuery(FailreRateQueryReqDTO reqDTO) {
        // todo 结构后期优化
        FailureRateDetailResDTO failureRateDetailResDTO = new FailureRateDetailResDTO();
        if (StringUtils.isEmpty(reqDTO.getIndex())) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        if (reqDTO.getIndex().contains(RateIndex.VEHICLE_RATE)) {
            // 车辆系统故障率
            List<FailureRateResDTO> vehicleRate = failureRateMapper.vehicleRate(reqDTO);
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            vehicleRate.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(RateIndex.VEHICLE_RATE.getDesc());
            failureRateDetailResDTO.setVehicleRate(reliabilityDetailResDTO);
        }
        if (reqDTO.getIndex().contains(RateIndex.SIGNAL_RATE)) {
            //<!--信号系统故障率-->
            List<FailureRateResDTO> signalRate = failureRateMapper.signalRate(reqDTO);
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            signalRate.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(RateIndex.SIGNAL_RATE.getDesc());
            failureRateDetailResDTO.setSignalRate(reliabilityDetailResDTO);
        }
        if (reqDTO.getIndex().contains(RateIndex.POWER_RATE)) {
            //<!--供电系统故障率-->
            List<FailureRateResDTO> powerRate = failureRateMapper.powerRate(reqDTO);
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            powerRate.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(RateIndex.POWER_RATE.getDesc());
            failureRateDetailResDTO.setPowerRate(reliabilityDetailResDTO);
        }
        //没做 产品没要求 2024 04 01
        if (reqDTO.getIndex().contains(RateIndex.PSD_RATE)) {
            //<!--站台门-->
            List<FailureRateResDTO> psDrate = failureRateMapper.PSDrate(reqDTO);
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            psDrate.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(RateIndex.PSD_RATE.getDesc());
            failureRateDetailResDTO.setPSDrate(reliabilityDetailResDTO);
        }
        if (reqDTO.getIndex().contains(RateIndex.EXITING_RATE)) {
            // 退出正线运营故障率
            List<FailureRateResDTO> exitingRate = failureRateMapper.exitingRate(reqDTO);
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            exitingRate.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(RateIndex.EXITING_RATE.getDesc());
            failureRateDetailResDTO.setExitingRate(reliabilityDetailResDTO);
        }
        return failureRateDetailResDTO;
    }

    @Override
    public Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        String[] cos = reqDTO.getPlanName().split(CommonConstants.COMMA);
        List<String> planNameList = new ArrayList<>();
        for (String c : cos) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.OrderType", c);
            if (!Objects.isNull(dictionaries)) {
                planNameList.addAll(overhaulOrderMapper.queryPlan(dictionaries.getItemCname()));
            }
        }
        planNameList = planNameList.stream().distinct().filter(Objects::nonNull).collect(toList());
        return new Page<>();
//        Page<MaterialResDTO> page = materialMapper.query(reqDTO.of(), planNameList, reqDTO.getMatName(), reqDTO.getStartTime(), reqDTO.getEndTime(), reqDTO.getTrainNo());
//        if (StringUtils.isEmpty(page.getRecords())) {
//            return new Page<>();
//        }
//        return page;
    }

    @Override
    public CarFaultQueryResDTO query(CarFaultQueryReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getStartTime()) && StringUtils.isEmpty(reqDTO.getEndTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String starDate = calendar.get(Calendar.YEAR) + "-01-01";
            Date parse;
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
        if (StringUtils.isEmpty(query)) {
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
            List<Integer> endData = getEndDataList(query, title, monthData);
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

    /**
     * 获取结束时间列表
     * @param query 列表
     * @param title 标题
     * @param monthData 月份数据
     * @return 结束时间列表
     */
    @NotNull
    private static List<Integer> getEndDataList(List<CarFaultQueryResDTO> query, String title, TreeSet<String> monthData) {
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
        return endData;
    }

    @Override
    public ReliabilityListResDTO reliabilityQuery(FailreRateQueryReqDTO reqDTO) {
        // todo 代码结构过于冗余，后期优化。
//        if (StringUtils.isEmpty(reqDTO.getStartTime()) && StringUtils.isEmpty(reqDTO.getEndTime())) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            String starDate = calendar.get(Calendar.YEAR) + "-01-01";
//            Date parse = null;
//            try {
//                parse = sdf.parse(starDate);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//            calendar.setTime(parse);
//            reqDTO.setStartTime(sdf.format(calendar.getTime()));
//            calendar.add(Calendar.YEAR, 1);
//            reqDTO.setEndTime(sdf.format(calendar.getTime()));
//        }
        List<String> months = DateUtils.getMonthBetweenDate(reqDTO.getStartTime(), reqDTO.getEndTime());
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.TICKET.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryTicketFault(reliabilityDetailResDTO);
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.GATE_BRAKE.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryGateBrakeFault(reliabilityDetailResDTO);
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.ESCALATOR.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryEscalatorFault(reliabilityDetailResDTO);
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.VERTICAL_ESCALATOR.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryVerticalEscalatorFault(reliabilityDetailResDTO);
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.TRAIN_PASSENGER.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryTrainPassengerInformationFault(reliabilityDetailResDTO);
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
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.STATION_PASSENGER.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryStationPassengerInformationFault(reliabilityDetailResDTO);
        }
        if (reqDTO.getSystemType().contains(SystemType.FIRE_FIGHTING)) {
            // 消防设备可靠度
            List<ReliabilityResDTO> queryFireFightingEquipmentFault = reliabilityMapper.queryFireFightingEquipmentFault(reqDTO.getEndTime(), reqDTO.getStartTime());
            List<String> data = Lists.newArrayList();
            List<String> month = Lists.newArrayList();
            queryFireFightingEquipmentFault.forEach(a -> {
                data.add(a.getPrecent());
                month.add(a.getYearMonth());
            });
            ReliabilityDetailResDTO reliabilityDetailResDTO = new ReliabilityDetailResDTO();
            reliabilityDetailResDTO.setMonth(month);
            reliabilityDetailResDTO.setData(data);
            reliabilityDetailResDTO.setName(SystemType.FIRE_FIGHTING.getDesc());
            buildResMonth(reliabilityDetailResDTO, months);
            reliabilityListResDTO.setQueryFireFightingEquipmentFault(reliabilityDetailResDTO);
        }
        return reliabilityListResDTO;
    }

    /**
     * 填充统计对象中不存在的月份数据
     *
     * @param res 统计对象
     */
    private void buildResMonth(ReliabilityDetailResDTO res, List<String> months) {
        if (StringUtils.isNull(res) || res.getMonth().isEmpty()) {
            res.setMonth(months);
            res.setData(Stream.generate(() -> "100").limit(res.getMonth().size()).collect(toList()));
        }
    }

    @Override
    public OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearReqDTO reqDTO) {
        List<OneCarOneGearResDTO> query = oneCarOneGearMapper.query(reqDTO.getEquipName());
        OneCarOneGearResDTO summary = oneCarOneGearMapper.querySummary(
                reqDTO.getEndTime() + " 23:59:59", reqDTO.getStartTime() + " 00:00:00",
                reqDTO.getEndTime(), reqDTO.getStartTime(), reqDTO.getEquipName());
        if (query != null) {
            summary.setStartUseDate(query.get(0).getStartUseDate());
            summary.setManufactureDate(query.get(0).getManufactureDate());
            summary.setTrainNo(query.get(0).getTrainNo());
        }
        return summary;
    }

    @Override
    public Page<InspectionJobListResDTO> querydmer3(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.querydmer3(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER4(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER4(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER5(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER5(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public void queryER5Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        List<InspectionJobListResDTO> inspectionJobListRes = oneCarOneGearMapper.queryER5(equipName, startTime, endTime);
        List<ExcelInspectionJobListResDTO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(inspectionJobListRes)) {
            for (InspectionJobListResDTO resDTO : inspectionJobListRes) {
                ExcelInspectionJobListResDTO res = new ExcelInspectionJobListResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "二级修(360天)", list);
        }
    }

    @Override
    public Page<FaultDetailResDTO> queryFMHistory(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryFMHistory(reqDTO.of(), reqDTO.getEquipName(),
                reqDTO.getStartTime() + " 00:00:00", reqDTO.getEndTime() + " 23:59:59");
    }

    @Override
    public void queryFMHistoryExport(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        List<FaultDetailResDTO> faultDetailResDTOS = oneCarOneGearMapper.queryFMHistory(equipName,
                startTime + " 00:00:00", endTime + " 23:59:59");
        List<ExcelFmFaultDetailResDTO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(faultDetailResDTOS)) {
            for (FaultDetailResDTO resDTO : faultDetailResDTOS) {
                ExcelFmFaultDetailResDTO res = new ExcelFmFaultDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setRepairTime(String.valueOf(resDTO.getRepairTime()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "故障列表", list);
        }
    }

    @Override
    public Page<TrackQueryResDTO> queryFaultFollow(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryFaultFollow(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    /**
     * 一车一档部件更换记录
     */
    @Override
    public Page<PartReplaceResDTO> querydmdm20(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.querydmdm20(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public void querydmdm20Export(String equipName, String startTime, String endTime, HttpServletResponse response) throws IOException {
        List<PartReplaceResDTO> partReplaceList = oneCarOneGearMapper.querydmdm20(equipName, startTime, endTime);
        if (StringUtils.isNotEmpty(partReplaceList)) {
            List<ExcelStatisticPartReplaceResDTO> list = new ArrayList<>();
            for (PartReplaceResDTO resDTO : partReplaceList) {
                ExcelStatisticPartReplaceResDTO res = new ExcelStatisticPartReplaceResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "部件更换", list);
        }
    }


    @Override
    public Page<GearboxChangeOilResDTO> pageGearboxChangeOil(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.listGearboxChangeOil(reqDTO.of(), reqDTO);
    }

    @Override
    public void pageGearboxChangeOilExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<GearboxChangeOilResDTO> gearboxChangeOilList = oneCarOneGearMapper.listGearboxChangeOil(reqDTO);
        if (StringUtils.isNotEmpty(gearboxChangeOilList)) {
            List<ExcelStatisticGearboxChangeOilResDTO> list = new ArrayList<>();
            for (GearboxChangeOilResDTO resDTO : gearboxChangeOilList) {
                ExcelStatisticGearboxChangeOilResDTO res = new ExcelStatisticGearboxChangeOilResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "齿轮箱换油", list);
        }
    }

    @Override
    public void querydmer3Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        List<InspectionJobListResDTO> resList = oneCarOneGearMapper.querydmer3(equipName, startTime, endTime);
        if (StringUtils.isNotEmpty(resList)) {
            List<ExcelInspectionJobListResDTO> list = new ArrayList<>();
            for (InspectionJobListResDTO resDTO : resList) {
                ExcelInspectionJobListResDTO res = new ExcelInspectionJobListResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "2级修90天包", list);
        }
    }

    @Override
    public void queryER4Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        // 2级修180天
        List<InspectionJobListResDTO> resList = oneCarOneGearMapper.queryER4(equipName, startTime, endTime);
        if (StringUtils.isNotEmpty(resList)) {
            List<InspectionJobListResDTO> list = new ArrayList<>();
            for (InspectionJobListResDTO resDTO : resList) {
                InspectionJobListResDTO res = new InspectionJobListResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "2级修180天", list);
        }
    }

    @Override
    public void queryER1Export(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        List<InspectionJobListResDTO> resList = oneCarOneGearMapper.queryER1(equipName, startTime, endTime);
        if (StringUtils.isNotEmpty(resList)) {
            List<InspectionJobListResDTO> list = new ArrayList<>();
            for (InspectionJobListResDTO resDTO : resList) {
                InspectionJobListResDTO res = new InspectionJobListResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "1级修", list);
        }
    }

    @Override
    public void faultListExport(FaultQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<FaultDetailResDTO> faultDetailList = faultQueryMapper.getByIds(reqDTO);
        List<Dictionaries> orderStatus = dictionariesMapper.list("dm.faultStatus", null, null);
        Map<String, Dictionaries> stringDictionariesMap = StreamUtils.toMap(orderStatus, Dictionaries::getItemCode);
        if (StringUtils.isNotEmpty(faultDetailList)) {
            List<ExcelFaultDetailResDTO> list = new ArrayList<>();
            for (FaultDetailResDTO resDTO : faultDetailList) {
                ExcelFaultDetailResDTO res = new ExcelFaultDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                String orderStatus1 = res.getOrderStatus();
                if (stringDictionariesMap.containsKey(orderStatus1)){
                    res.setOrderStatus(stringDictionariesMap.get(orderStatus1).getItemCname());
                }
                list.add(res);
            }
            EasyExcelUtils.export(response, "故障统计报表列表信息", list);
        }
    }

    @Override
    public void faultExport(FaultQueryDetailReqDTO reqDTO,HttpServletResponse response) {
        exportComponent.exportByTemplate(reqDTO,response);
    }

    /**
     * 轮对镟修记录
     */
    @Override
    public Page<WheelsetLathingResDTO> pageWheelsetLathing(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.listWheelsetLathing(reqDTO.of(), reqDTO);
    }

    @Override
    public void pageWheelsetLathingExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<WheelsetLathingResDTO> wheelsetLathingList = oneCarOneGearMapper.listWheelsetLathing(reqDTO);
        if (StringUtils.isNotEmpty(wheelsetLathingList)) {
            List<ExcelStatisticWheelsetLathingResDTO> list = new ArrayList<>();
            for (WheelsetLathingResDTO resDTO : wheelsetLathingList) {
                ExcelStatisticWheelsetLathingResDTO res = new ExcelStatisticWheelsetLathingResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "轮对镟修记录", list);
        }
    }

    /**
     * 普查与技改
     */
    @Override
    public Page<GeneralSurveyResDTO> pageGeneralSurvey(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.listGeneralSurvey(reqDTO.of(), reqDTO);
    }

    @Override
    public void pageGeneralSurveyExport(OneCarOneGearQueryReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<GeneralSurveyResDTO> generalSurveyList = oneCarOneGearMapper.listGeneralSurvey(reqDTO);
        if (StringUtils.isNotEmpty(generalSurveyList)) {
            List<ExcelStatisticGeneralSurveyResDTO> list = new ArrayList<>();
            for (GeneralSurveyResDTO resDTO : generalSurveyList) {
                ExcelStatisticGeneralSurveyResDTO res = new ExcelStatisticGeneralSurveyResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "普查与技改", list);
        }
    }


    @Override
    public Page<InspectionJobListResDTO> queryER2(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER2(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }

    @Override
    public Page<InspectionJobListResDTO> queryER1(OneCarOneGearQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        return oneCarOneGearMapper.queryER1(reqDTO.of(), reqDTO.getEquipName(), reqDTO.getStartTime(), reqDTO.getEndTime());
    }


    @Override
    public RamsCarResDTO query4AQYYZB() {
        List<RamsCarResDTO> records = ramsMapper.query4AQYYZB();
        if (StringUtils.isEmpty(records)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        RamsCarResDTO ramsCarResDTO = records.get(0);
        String millionMiles = ramsCarResDTO.getMillionMiles();
        if (StringUtils.isNotEmpty(millionMiles) && Double.parseDouble(millionMiles) != 0.0) {
            //晚点故障数
            String affect11 = ramsCarResDTO.getAffect11();
            //不适合继续服务
            String affect21 = ramsCarResDTO.getAffect21();
            String faultNum = ramsCarResDTO.getFaultNum();
            //晚点
            String affect12 = df.format(Double.parseDouble(affect11) / Double.parseDouble(millionMiles));
            String affect22 = df.format(Double.parseDouble(affect21) / Double.parseDouble(millionMiles));
            if (Double.parseDouble(affect12) > ONE_POINT_FIVE) {
                ramsCarResDTO.setAffect13("未达标");
            } else {
                ramsCarResDTO.setAffect13("达标");
            }
            if (Double.parseDouble(affect22) > FOUR_POINT_FIVE) {
                ramsCarResDTO.setAffect23("未达标");
            } else {
                ramsCarResDTO.setAffect23("达标");
            }
            ramsCarResDTO.setAffect12(affect12);
            ramsCarResDTO.setAffect22(affect22);
            ramsCarResDTO.setFaultNum(faultNum);
        }
        return ramsCarResDTO;
    }

    @Override
    public List<SystemFaultsResDTO> queryresult3(String startDate, String endDate, String sys) {
        // 取dm.sysCode4Train字典列表
        if (StringUtils.isEmpty(sys)){
            return null;
        }
        if (StringUtils.isNotEmpty(startDate)) {
            startDate = startDate.substring(0, 7);
        } else {
            startDate = getLastMouths(11);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endDate = endDate.substring(0, 7);
        } else {
            endDate = getLastMouths(0);
        }
        Set<String> moduleIds = new HashSet<>();
        switch (sys) {
            case "01":
                moduleIds.add("B27");
                moduleIds.add("B17");
                break;
            case "02":
                moduleIds.add("B08");
                break;
            case "03":
                moduleIds.add("B19");
                moduleIds.add("B20");
                break;
            case "04":
                moduleIds.add("B03");
                break;
            case "05":
                moduleIds.add("B25");
                break;
            case "06":
                moduleIds.add("B28");
                break;
            case "07":
                moduleIds.add("B01");
                moduleIds.add("B02");
                moduleIds.add("B12");
                moduleIds.add("B18");
                moduleIds.add("B21");
                break;
            case "08":
                moduleIds.add("B04");
                moduleIds.add("B05");
                break;
            case "09":
                moduleIds.add("B06");
                moduleIds.add("B07");
                break;
            case "10":
                moduleIds.add("B11");
                moduleIds.add("B14");
                moduleIds.add("B13");
                break;
            case "11":
                moduleIds.add("B22");
                break;
            case "12":
                moduleIds.add("B09");
                moduleIds.add("B10");
                moduleIds.add("B15");
                moduleIds.add("B16");
                break;
            case "13":
                moduleIds.add("B29");
                break;
            case "14":
                moduleIds.add("B26");
                break;
            case "15":
                moduleIds.add("B30");
                break;
            default:
                break;
        }
        return ramsMapper.queryFautTypeByMonthBySys(moduleIds, startDate, endDate);
    }

    /**
     * 这段代码的作用是获取当前时间，然后回退i月
     * @param i
     * @return
     */
    private static String getLastMouths(int i) {
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
    @Override
    public List<RamsResult2ResDTO> queryresult2(String startDate, String endDate) {
        if (StringUtils.isNotEmpty(startDate)) {
            startDate = startDate.substring(0, 7);
        } else {
            startDate = getLastMouths(11);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endDate = endDate.substring(0, 7);
        } else {
            endDate = getLastMouths(0);
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        List<RamsResult2ResDTO> ramsResDTOS = ramsMapper.queryresult2(startDate, endDate);
        ramsResDTOS.forEach(a -> {
            double lateZ = 0.0D;
            double noServiceZ = 0.0D;
            a.setMiles(a.getMiles() == null ? "0" : a.getMiles());
            double miles = Double.parseDouble(a.getMiles()) != 0.0D ? Double.parseDouble(a.getMiles()) : 0.0D;
            if (Double.parseDouble(a.getLate()) == ZERO) {
                lateZ = Double.parseDouble("0");
            } else {
                lateZ = Double.parseDouble(a.getLate())  / miles / 10000;
            }
            if (Double.parseDouble(a.getNoService()) == ZERO) {
                noServiceZ = Double.parseDouble("0");
            } else {
                noServiceZ = Double.parseDouble(a.getNoService()) / miles /10000;
            }
            a.setLate(df.format(lateZ));
            a.setNoService(df.format(noServiceZ));
        });
        return ramsResDTOS;
    }

    @Override
    public List<RamsSysPerformResDTO> querySysPerform() {
        List<RamsSysPerformResDTO> ramsResDTOS = ramsMapper.querySysPerform();
        List<RamsResDTO> totalMilesList = ramsMapper.querytotalMiles();
        RamsResDTO totalMiles = totalMilesList.get(0);
        Map<String, RamsSysPerformResDTO> map = Maps.newHashMap();
        mapInitSysPerform(map);
        for (RamsSysPerformResDTO a : ramsResDTOS) {
            if (StringUtils.isEmpty(a.getModuleName())) {
                continue;
            }
            switch (substringFirstThree(a.getModuleName())) {
                case "B09":
                case "B10":
                    rebuildBlock4SP(a, "气动装置和空气分配系统", "10000", "7000");
                    break;
                case "B15":
                case "B16":
                case "B17":
                case "B27":
                    rebuildBlock4SP(a, "门窗系统", "10000", "7000");
                    break;
                case "B08":
                    rebuildBlock4SP(a, "制动系统", "49500", "49500");
                    break;
                case "B19":
                case "B20":
                    rebuildBlock4SP(a, "空调及通风系统", "210000", "46000");
                    break;
                case "B11":
                case "B14":
                case "B13":
                    rebuildBlock4SP(a, "辅助供电设备系统", "647749", "323825");
                    break;
                case "B03":
                    rebuildBlock4SP(a, "转向架", "409500", "409500");
                    break;
                case "B25":
                    rebuildBlock4SP(a, "雷达辅助系统", "647749", "323825");
                    break;
                case "B01":
                case "B02":
                case "B12":
                case "B18":
                case "B21":
                    rebuildBlock4SP(a, "车体结构及内装", "204800", "204800");
                    break;
                case "B06":
                case "B07":
                    rebuildBlock4SP(a, "牵引及高压系统", "36588", "11000");
                    break;
                case "B04":
                case "B05":
                    rebuildBlock4SP(a, "贯通道和车钩", "409500", "204800");
                    break;
                case "B22":
                    rebuildBlock4SP(a, "列车管理及列车控制系统", "191000", "91000");
                    break;
                case "B23":
                case "B24":
                    rebuildBlock4SP(a, "旅客信息系统", "191000", "91000");
                    break;
                case "B30":
                    rebuildBlock4SP(a, "运行性能检测系统", "36588", "11000");
                    break;
                case "B26":
                    rebuildBlock4SP(a, "走行部监测系统", "36588", "11000");
                    break;
                case "B29":
                    rebuildBlock4SP(a, "轨道检测系统", "36588", "11000");
                    break;
                case "B28":
                    rebuildBlock4SP(a, "弓网检测系统", "36588", "11000");
                    break;
                default:
                    break;
            }
            DecimalFormat df = new DecimalFormat("#0");
            double MTBF_LATE;
            if (Double.parseDouble(a.getNumLate()) == ZERO) {
                MTBF_LATE = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D;
            } else {
                MTBF_LATE = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNumLate());
            }
            a.setMTBF_LATE(df.format(MTBF_LATE));
            double MTBF_NOS;
            if (Double.parseDouble(a.getNumNos()) == ZERO) {
                MTBF_NOS = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D;
            } else {
                MTBF_NOS = Double.parseDouble(totalMiles.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNumNos());
            }
            a.setMTBF_NOS(df.format(MTBF_NOS));
            RamsSysPerformResDTO last = map.get(a.getModuleName());
            if (map.containsKey(a.getModuleName())) {
                a.setNumNos(String.valueOf(Integer.parseInt(a.getNumNos()) + Integer.parseInt(last.getNumNos())));
                a.setMTBF_NOS(String.valueOf(Integer.parseInt(a.getMTBF_NOS()) + Integer.parseInt(last.getMTBF_NOS())));
                map.put(a.getModuleName(), a);
            }
        }
        buildSysPerformIsCompliance(map);
        return new ArrayList<>(map.values());
    }

    private void mapInitSysPerform(Map<String, RamsSysPerformResDTO> map ,String name){
        RamsSysPerformResDTO a = new RamsSysPerformResDTO();
        a.setModuleName(name);
        a.setNumLate(CommonConstants.ZERO_STRING);
        a.setNumNos(CommonConstants.ZERO_STRING);
        a.setContractZBLATE(CommonConstants.ZERO_STRING);
        a.setContractZBNOS(CommonConstants.ZERO_STRING);
        a.setMTBF_LATE(CommonConstants.ZERO_STRING);
        a.setIsDB_LATE(CommonConstants.ZERO_STRING);
        a.setIsDB_NOS(CommonConstants.ZERO_STRING);
        a.setMTBF_NOS(CommonConstants.ZERO_STRING);
        map.put(name, a);
    }
    private void mapInitSysPerform(Map<String, RamsSysPerformResDTO> map) {
            mapInitSysPerform(map,"气动装置和空气分配系统");
            mapInitSysPerform(map,"门窗系统");
            mapInitSysPerform(map,"制动系统");
            mapInitSysPerform(map,"空调及通风系统");
            mapInitSysPerform(map,"辅助供电设备系统");
            mapInitSysPerform(map,"转向架");
            mapInitSysPerform(map,"雷达辅助系统");
            mapInitSysPerform(map,"车体结构及内装");
            mapInitSysPerform(map,"牵引及高压系统");
            mapInitSysPerform(map,"贯通道和车钩");
            mapInitSysPerform(map,"列车管理及列车控制系统");
            mapInitSysPerform(map,"旅客信息系统");
            mapInitSysPerform(map,"运行性能检测系统");
            mapInitSysPerform(map,"走行部监测系统");
            mapInitSysPerform(map,"轨道检测系统");
            mapInitSysPerform(map,"弓网检测系统");
    }

    /**
     * 各系统可靠性统计-构建是否达标标识
     * @param map 集合
     */
    private void buildSysPerformIsCompliance(Map<String, RamsSysPerformResDTO> map) {
        List<FaultConditionResDTO> list = queryCountFaultType();
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
            // 添加各系统平均无故障时间实际指标
            a.setZB(list.stream().filter(b -> b.getModuleName().equals(a.getModuleName())).collect(toList()).get(0).getZB());
        });
    }

    @Override
    public void exportSysPerform(HttpServletResponse response) throws IOException {
        List<RamsSysPerformResDTO> list = querySysPerform();
        if (StringUtils.isNotEmpty(list)) {
            List<ExcelRamsSysPerformResDTO> resList = new ArrayList<>();
            for (RamsSysPerformResDTO resDTO : list) {
                ExcelRamsSysPerformResDTO res = new ExcelRamsSysPerformResDTO();
                res.setModuleName(resDTO.getModuleName());
                res.setZb(resDTO.getZB());
                resList.add(res);
            }
            EasyExcelUtils.export(response, "列车子系统的可靠性目标", resList);
        }
    }

    /**
     * 真的屎山
     * 各系统故障情况统计
     */
    @Override
    public List<FaultConditionResDTO> queryCountFaultType() {
        List<FaultConditionResDTO> list = ramsMapper.queryCountFaut();
        List<RamsResDTO> ramsResDTOS = ramsMapper.querytotalMiles();
        RamsResDTO ramsResDTO = ramsResDTOS.get(0);
        Set<String> names = Sets.newHashSet();
        Map<String, FaultConditionResDTO> map = new HashMap<>();
        mapInit(map);
        for (FaultConditionResDTO a : list) {
            //TODO 合同指标 产品还没给
            if (StringUtils.isEmpty(a.getSC())){
                continue;
            }
            switch (substringFirstThree(a.getSC())) {
                case "B09":
                case "B10":
                    rebuildBlock(a, names, "气动装置和空气分配系统", "9000");
                    break;
                case "B15":
                case "B16":
                case "B17":
                case "B27":
                    rebuildBlock(a, names, "门窗系统", "9000");
                    break;
                case "B08":
                    rebuildBlock(a, names, "制动系统", "9000");
                    break;
                case "B19":
                case "B20":
                    rebuildBlock(a, names, "空调及通风系统", "9000");
                    break;
                case "B11":
                case "B14":
                case "B13":
                    rebuildBlock(a, names, "辅助供电设备系统", "9000");
                    break;
                case "B03":
                    rebuildBlock(a, names, "转向架", "9000");
                    break;
                case "B25":
                    rebuildBlock(a, names, "雷达辅助系统", "9000");
                    break;
                case "B01":
                case "B02":
                case "B12":
                case "B18":
                case "B21":
                    rebuildBlock(a, names, "车体结构及内装", "9000");
                    break;
                case "B06":
                case "B07":
                    rebuildBlock(a, names, "牵引及高压系统", "9000");
                    break;
                case "B04":
                case "B05":
                    rebuildBlock(a, names, "贯通道和车钩", "9000");
                    break;
                case "B22":
                    rebuildBlock(a, names, "列车管理及列车控制系统", "9000");
                    break;
                case "B23":
                case "B24":
                    rebuildBlock(a, names, "旅客信息系统", "9000");
                    break;
                case "B30":
                    rebuildBlock(a, names, "运行性能检测系统", "9000");
                    break;
                case "B26":
                    rebuildBlock(a, names, "走行部监测系统", "9000");
                    break;
                case "B29":
                    rebuildBlock(a, names, "轨道检测系统", "9000");
                    break;
                case "B28":
                    rebuildBlock(a, names, "弓网检测系统", "9000");
                    break;
                default:
                    break;
            }
            String moduleName = a.getModuleName();
            FaultConditionResDTO last = map.get(moduleName);
            if (map.containsKey(moduleName)) {
                last.setCRK(String.valueOf(Integer.parseInt(a.getCRK() == null ? CommonConstants.ZERO_STRING:a.getCRK()) + Integer.parseInt(last.getCRK()== null ? CommonConstants.ZERO_STRING:last.getCRK())));
                last.setZX(String.valueOf(Integer.parseInt(a.getZX() == null ? CommonConstants.ZERO_STRING:a.getZX()) + Integer.parseInt(last.getZX()== null ? CommonConstants.ZERO_STRING:last.getZX())));
                last.setZS(String.valueOf(Integer.parseInt(a.getZS()== null ? CommonConstants.ZERO_STRING:a.getZS()) + Integer.parseInt(last.getZS()== null ? CommonConstants.ZERO_STRING:last.getZS())));
                last.setYF(String.valueOf(Integer.parseInt(a.getYF() == null ? CommonConstants.ZERO_STRING:a.getYF()) + Integer.parseInt(last.getYF()== null ? CommonConstants.ZERO_STRING:last.getYF())));
                last.setNOYF(String.valueOf(Integer.parseInt(a.getNOYF()== null ? CommonConstants.ZERO_STRING:a.getNOYF()) + Integer.parseInt(last.getNOYF()== null ? CommonConstants.ZERO_STRING:last.getNOYF())));
                last.setModuleName(moduleName);
            }
        }
        buildFaultTypeIsCompliance(map, ramsResDTO);
        return new ArrayList<>(map.values());
    }

    private void mapInit(Map<String, FaultConditionResDTO> map) {
        mapInit(map,"气动装置和空气分配系统");
        mapInit(map,"门窗系统");
        mapInit(map,"制动系统");
        mapInit(map,"空调及通风系统");
        mapInit(map,"辅助供电设备系统");
        mapInit(map,"转向架");
        mapInit(map,"雷达辅助系统");
        mapInit(map,"车体结构及内装");
        mapInit(map,"牵引及高压系统");
        mapInit(map,"贯通道和车钩");
        mapInit(map,"列车管理及列车控制系统");
        mapInit(map,"旅客信息系统");
        mapInit(map,"运行性能检测系统");
        mapInit(map,"走行部监测系统");
        mapInit(map,"轨道检测系统");
        mapInit(map,"弓网检测系统");
    }

    public void mapInit(Map<String, FaultConditionResDTO> map, String name) {
        FaultConditionResDTO a = new FaultConditionResDTO();
        a.setCRK(CommonConstants.ZERO_STRING);
        a.setZX(CommonConstants.ZERO_STRING);
        a.setZS(CommonConstants.ZERO_STRING);
        a.setYF(CommonConstants.ZERO_STRING);
        a.setContractZB(CommonConstants.ZERO_STRING);
        a.setIsDB(CommonConstants.ZERO_STRING);
        a.setNOYF(CommonConstants.ZERO_STRING);
        a.setModuleName(name);
        map.put(name, a);
    }

    public static String substringFirstThree(String str) {
        // 如果字符串为空或者长度小于3，直接返回原字符串
        if (str == null || str.length() < CommonConstants.THREE) {
            return str;
        }
        return str.substring(0, 3);
    }
    /**
     * 各系统故障情况统计-构建是否达标标识
     * @param map 集合
     * @param ramsRes ram
     */
    private void buildFaultTypeIsCompliance(Map<String, FaultConditionResDTO> map, RamsResDTO ramsRes) {
        map.values().forEach(a -> {
            DecimalFormat df = new DecimalFormat("#0");
            double zb;
            if (Double.parseDouble(a.getNOYF()) == ZERO) {
                zb = Double.parseDouble(ramsRes.getTotalMiles()) * 4.0D / 55.0D;
            } else {
                zb = Double.parseDouble(ramsRes.getTotalMiles()) * 4.0D / 55.0D / Double.parseDouble(a.getNOYF());
            }
            a.setZB(df.format(zb));
            if (zb >= Double.parseDouble(a.getContractZB())) {
                a.setIsDB("达标");
            } else {
                a.setIsDB("未达标");
            }
        });
    }

    /**
     * RAMS 故障列表
     */
    @Override
    public Page<FaultRamsResDTO> queryRamsFaultList(RamsTimeReqDTO reqDTO) {
        Page<FaultRamsResDTO> list = ramsMapper.queryRamsFaultList(reqDTO.of(), reqDTO.getStartTime(), reqDTO.getEndTime(),null);
        if (StringUtils.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        List<FaultRamsResDTO> records = list.getRecords();
        records.forEach(a -> {
            FaultRamsResDTO faultRAMSResDTO = ramsMapper.queryPart(a.getFaultWorkNo());
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
    public void materialExport(MaterialListReqDTO reqDTO, HttpServletResponse response) throws IOException {
//        List<MaterialResDTO> exportList = materialMapper.exportList(reqDTO);
        List<MaterialResDTO> exportList = new ArrayList<>();
        if (exportList != null && !exportList.isEmpty()) {
            List<ExcelMaterialResDTO> list = new ArrayList<>();
            for (MaterialResDTO resDTO : exportList) {
                ExcelMaterialResDTO res = new ExcelMaterialResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "物料统计", list);
        }
    }

    @Override
    public void queryFaultFollowExport(String startTime, String endTime, String equipName, HttpServletResponse response) throws IOException {
        List<TrackQueryResDTO> trackQueryList = oneCarOneGearMapper.queryFaultFollow(startTime, endTime, equipName);
        if (trackQueryList != null && !trackQueryList.isEmpty()) {
            List<ExcelTrackQueryResDTO> list = new ArrayList<>();
            for (TrackQueryResDTO resDTO : trackQueryList) {
                ExcelTrackQueryResDTO res = new ExcelTrackQueryResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTrackCycle(String.valueOf(resDTO.getTrackCycle()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "物料统计", list);
        }
    }

    public void rebuildBlock4SP(RamsSysPerformResDTO map, String moduleName, String contractZB_LATE, String contractZB_NOS) {
        map.setModuleName(moduleName);
        map.setContractZBLATE(contractZB_LATE);
        map.setContractZBNOS(contractZB_NOS);
    }

    public void rebuildBlock(FaultConditionResDTO res, Set<String> module, String moduleName, String contractZB) {
        DecimalFormat df = new DecimalFormat("#0");
        String ZX = res.getZX();
        String CRK = res.getCRK();
        String YF = res.getYF();
        String ZS = res.getZS();
        String NOYF = res.getNOYF();
        res.setContractZB(contractZB);
        res.setModuleName(moduleName);
        // if (module.contains(moduleName)) {
        //     res.setZX(df.format(Double.parseDouble(ZX) + ZX));
        //     res.setCRK(df.format(Double.parseDouble(CRK) + CRK));
        //     res.setYF(df.format(Double.parseDouble(YF) + YF));
        //     res.setZS(df.format(Double.parseDouble(ZS) + ZS));
        //     res.setNOYF(df.format(Double.parseDouble(NOYF) + NOYF));
        // } else {
        //     res.setModuleName(moduleName);
        //     res.setZX(ZX);
        //     res.setCRK(CRK);
        //     res.setYF(YF);
        //     res.setZS(ZS);
        //     res.setNOYF(NOYF);
        // }
    }

    @Override
    public RamsTrainReliabilityResDTO trainReliability(String startTime, String endTime, String trainNo) {
        RamsTrainReliabilityResDTO res = new RamsTrainReliabilityResDTO();
        // 获取各指标项的故障次数
        double delayCount = ramsMapper.countRamsFaultList(startTime, endTime, trainNo, "'10'", "'03','04','05'","0");
        double notCount = ramsMapper.countRamsFaultList(startTime, endTime, trainNo, "'10'", "'06','07','08','09'","0");
        double faultCount = ramsMapper.countRamsFaultList(startTime, endTime, trainNo, null, null,"0");
         double miles = ramsMapper.getMileSubtract(startTime, endTime, trainNo);
//        Double start = ramsMapper.getMileByTrainNoStart(startTime, trainNo);
//        Double end = ramsMapper.getMileByTrainNoEnd(endTime, trainNo);
//        double miles = 0.0;
//        if (null != start && null != end) {
//            miles = end - start;
//        }
        if (miles == 0) {
         throw new CommonException(ErrorCode.NORMAL_ERROR, "选定统计周期内累计运营里程为0，无法计算");
        }
        res.setTotalMile(miles);
        // 实际指标计算
        res.setRealDelay(countTrainReliabilityIndex(delayCount, miles));
        res.setRealNot(countTrainReliabilityIndex(notCount, miles));
        res.setRealFault(countTrainReliabilityIndex(faultCount, miles));
        // todo 合同指标及是否达标填充

        return res;
    }

    /**
     * 计算列车可靠性指标
     * @param count 次数
     * @param miles 运营里程
     * @return 计算列车可靠性指标
     */
    private Double countTrainReliabilityIndex(double count, double miles) {
        if (miles != 0) {
            return count * 1000000 / (4 * miles);
        }
        return 0.0;
    }

    @Override
    public Page<FaultRamsResDTO> trainReliabilityFaultList(String startTime, String endTime, String trainNo, PageReqDTO pageReqDTO) {
        Page<FaultRamsResDTO> list = ramsMapper.queryRamsFaultList(pageReqDTO.of(), startTime, endTime, trainNo);
        if (StringUtils.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        List<FaultRamsResDTO> records = list.getRecords();
        records.forEach(a -> {
            FaultRamsResDTO faultRamsRes = ramsMapper.queryPart(a.getFaultWorkNo());
            if (StringUtils.isNotNull(faultRamsRes)) {
                String replacementName = faultRamsRes.getReplacementName();
                String oldRepNo = faultRamsRes.getOldRepNo();
                String newRepNo = faultRamsRes.getNewRepNo();
                String operateCostTime = faultRamsRes.getOperateCostTime();
                a.setReplacementName(replacementName == null ? CommonConstants.EMPTY : replacementName);
                a.setOldRepNo(oldRepNo == null ? CommonConstants.EMPTY : oldRepNo);
                a.setNewRepNo(newRepNo == null ? CommonConstants.EMPTY : newRepNo);
                a.setOperateCostTime(operateCostTime == null ? CommonConstants.EMPTY : operateCostTime);
            }
        });
        return list;
    }

    @Override
    public List<SubjectFaultResDTO> getSubjectFaultOpen(String startTime, String endTime) {
        List<SubjectFaultResDTO> list = new ArrayList<>();
        for (OperateDailySystem system : OperateDailySystem.values()) {
            SubjectFaultResDTO res = new SubjectFaultResDTO();
            res.setSubjectName(system.getName());
            Long num = faultQueryMapper.getSubjectFaultNum(system.getEquipmentCategory(), startTime, endTime);
            res.setFaultNum(StringUtils.isNull(num) ? 0L : num);
            list.add(res);
        }
        return list;
    }
}


