package com.wzmtr.eam.impl.overhaul;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.SchedulingReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingRuleResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingBuildResDTO;
import com.wzmtr.eam.dto.res.overhaul.SchedulingListResDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.overhaul.SchedulingMapper;
import com.wzmtr.eam.service.overhaul.SchedulingService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

/**
 * 预防性检修管理-检修工单排期
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
@Service
@Slf4j
public class SchedulingServiceImpl implements SchedulingService {

    /**
     * 检修计划对应检修时间，例如：30天包->1天
     */
    private static final Map<Integer, Integer> PLAN_DAY_MAP = new HashMap<>();

    static {
        PLAN_DAY_MAP.put(4, 1);
        PLAN_DAY_MAP.put(30, 1);
        PLAN_DAY_MAP.put(90, 2);
        PLAN_DAY_MAP.put(180, 2);
        PLAN_DAY_MAP.put(360, 5);
    }

    /**
     * 一级修-4
     */
    private static final int ONE_LEVEL_REPAIR_FOUR = 4;
    /**
     * 一级修-8
     */
    private static final int ONE_LEVEL_REPAIR_EIGHT = 8;
    /**
     * 二级修-30天包
     */
    private static final int MONTH = 30;
    /**
     * 二级修-90天包
     */
    private static final int QUARTER = 90;
    /**
     * 二级修-180天包
     */
    private static final int HALF_YEAR = 180;
    /**
     * 二级修-360天包
     */
    private static final int YEAR = 360;

    @Autowired
    private SchedulingMapper schedulingMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public List<SchedulingListResDTO> listScheduling(String startTime, String endTime) {
        List<SchedulingListResDTO> list = new ArrayList<>();
        List<RegionResDTO> trains = equipmentMapper.listTrainRegion("02");
        if (StringUtils.isNotEmpty(trains)) {
            for (RegionResDTO train : trains) {
                SchedulingListResDTO res = new SchedulingListResDTO();
                res.setEquipCode(train.getNodeCode());
                res.setEquipName(train.getNodeName());
                res.setSchedulingList(schedulingMapper.listScheduling(res.getEquipCode(), startTime, endTime));
                list.add(res);
            }
        }
        return list;
    }

    @Override
    public SchedulingResDTO getSchedulingDetail(String id) {
        return schedulingMapper.getSchedulingDetail(id);
    }

    @Override
    public SchedulingResDTO getLastSchedulingDetail(String id) {
        SchedulingResDTO res = schedulingMapper.getSchedulingDetail(id);
        if (StringUtils.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return schedulingMapper.getLastSchedulingDetail(res.getEquipCode(), res.getType(), res.getDay());
    }

    @Override
    public void modifyScheduling(SchedulingReqDTO req) {
        SchedulingResDTO old = schedulingMapper.getSchedulingDetail(req.getRecId());
        if (StringUtils.isNull(old)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        schedulingMapper.modifyScheduling(req);
        // 当修改后的时间比原先的时间大，修改该设备后续所有排期的日期
        if (DateUtils.dateCompare(req.getDay(), old.getDay(), CommonConstants.DAY) == 1) {
            long days = DateUtils.getDayBetweenDays(old.getDay(), req.getDay());
            if (days > 0) {
                req.setDay(old.getDay());
                schedulingMapper.syncModifyScheduling(req, days);
            }
        }
    }

    @Override
    public void generateOrderScheduling(List<String> equipCodes) throws ParseException {
        if (StringUtils.isNotEmpty(equipCodes)) {
            for (String equipCode : equipCodes) {
                // 移除设备在今天之后的排期
                schedulingMapper.removeSchedulingAfterNow(equipCode, TokenUtils.getCurrentPersonId());
                // 一级修排期
                List<SchedulingBuildResDTO> oneList = scheduling(equipCode, "1");
                oneList.removeIf(Objects::isNull);
                if (StringUtils.isNotEmpty(oneList)) {
                    // 一级修排期数据新增数据库
                    schedulingMapper.addOverhaulOrderScheduling(oneList, TokenUtils.getCurrentPersonId());
                }
                // 二级修排期
                List<SchedulingBuildResDTO> twoList = scheduling(equipCode, "2");
                twoList.removeIf(Objects::isNull);
                if (StringUtils.isNotEmpty(twoList)) {
                    for (SchedulingBuildResDTO scheduling : twoList) {
                        String day = schedulingMapper.getLastLevelOneRepairDay(
                                DateUtils.addDayDay(scheduling.getDay(), -2), scheduling.getDay());
                        if (StringUtils.isNotEmpty(day)) {
                            scheduling.setDay(day);
                        }
                        // 二级修排期数据新增数据库
                        schedulingMapper.addOverhaulOrderScheduling(Collections.singletonList(scheduling), TokenUtils.getCurrentPersonId());
                    }
                }
            }
        }
    }

    /**
     * 排期
     * @param equipCode 车辆设备编号
     * @param type 级修类型 1一级修 2二级修
     * @throws ParseException 异常
     */
    private List<SchedulingBuildResDTO> scheduling(String equipCode, String type) throws ParseException {
        Map<String, SchedulingBuildResDTO> levelOneRepairMap = new HashMap<>(150);
        Map<String, SchedulingBuildResDTO> levelTwoRepairMap = new HashMap<>(50);
        // 获取车辆绑定的工单触发规则
        List<SchedulingRuleResDTO> list = schedulingMapper.getTrainRule(equipCode, type);
        if (StringUtils.isNotEmpty(list)) {
            // 获取车辆上一次已触发的排期信息
            SchedulingResDTO scheduling = schedulingMapper.getTrainLastTriggerScheduling(equipCode, type);
            // 当上一次已触发的排期信息不为空时，取上次触发时间为起始时间
            String startTimeStr = "", endTimeStr = "";
            // 记录上次触发排期名称为起始排期数字
            // firstType为二级修时间最短的检修包
            int lastNum = 0, firstType = 0;
            if (StringUtils.isNotNull(scheduling)) {
                if (StringUtils.isNotEmpty(scheduling.getDay())) {
                    startTimeStr = scheduling.getDay();
                    endTimeStr = DateUtils.localDate2String(LocalDate.parse(startTimeStr).plusDays(400));
                }
                if (StringUtils.isNotEmpty(scheduling.getSchedulingName())) {
                    lastNum = Integer.parseInt(scheduling.getSchedulingName());
                }
            }
            // 根据存在的工单触发规则进行排期
            for (SchedulingRuleResDTO rule : list) {
                if (StringUtils.isEmpty(startTimeStr)) {
                    // 检修计划首次开始时间为起始时间，起始时间390天后为结束时间
                    startTimeStr = rule.getFirstBeginTime();
                    endTimeStr = DateUtils.localDate2String(LocalDate.parse(startTimeStr).plusDays(400));
                }
                // 构建排期数据
                if (CommonConstants.TWO_STRING.equals(type)) {
                    firstType = levelTwoRepairBuildMap(rule, levelTwoRepairMap, startTimeStr, endTimeStr, lastNum, firstType);
                } else {
                    levelOneRepairBuildMap(rule, levelOneRepairMap, startTimeStr, endTimeStr, lastNum);
                }
            }
            if (CommonConstants.ONE_STRING.equals(type)) {
                // 去除value对象为空的map
                levelOneRepairMap.entrySet().removeIf(a -> StringUtils.isNull(a.getValue()));
                // map重新根据key排序
                Map<String, SchedulingBuildResDTO> levelOneRepairTreeMap = new TreeMap<>(levelOneRepairMap);
                return new ArrayList<>(levelOneRepairTreeMap.values());
            } else {
                // 去除value对象为空的map
                levelTwoRepairMap.entrySet().removeIf(a -> StringUtils.isNull(a.getValue()));
                // map重新根据key排序
                Map<String, SchedulingBuildResDTO> levelTwoRepairTreeMap = new TreeMap<>(levelTwoRepairMap);
                List<SchedulingBuildResDTO> twoValues = new ArrayList<>(levelTwoRepairTreeMap.values());
                // 二级修根据日期类型，递归延期排期数据
                twoValues = buildDurationSchedulingList(twoValues);
                return twoValues;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 一级修排期-根据规则周期进行map填充
     * @param rule 规则
     * @param map map集合
     * @param startTimeStr 起始时间
     * @param endTimeStr 截止日期
     * @param lastNum 上一次触发的一级修排期数
     * @throws ParseException 异常
     */
    private void levelOneRepairBuildMap(SchedulingRuleResDTO rule, Map<String, SchedulingBuildResDTO> map,
                                        String startTimeStr, String endTimeStr, int lastNum) throws ParseException {
        List<String> days = getDays(startTimeStr, endTimeStr, ONE_LEVEL_REPAIR_FOUR, lastNum, 0);
        for (int i = 0; i < days.size(); i++) {
            map.put(days.get(i), buildLevelOneRepairScheduling(rule, lastNum, days, i));
        }
    }

    /**
     * 二级修排期-根据规则周期进行map填充
     * @param rule 规则
     * @param map map集合
     * @param startTimeStr 起始时间
     * @param endTimeStr 截止日期
     * @param lastNum 上一次触发的排期数
     * @param firstType 二级修时间最短的检修包
     * @throws ParseException 异常
     */
    private int levelTwoRepairBuildMap(SchedulingRuleResDTO rule, Map<String, SchedulingBuildResDTO> map,
                                       String startTimeStr, String endTimeStr, int lastNum, int firstType) throws ParseException {
        List<String> month, quarter, halfYear, year;
        boolean bool;
        // 根据规则周期进行map日期填充
        switch (Math.toIntExact(rule.getPeriod() / 24)) {
            case 30:
                firstType = MONTH;
                // 获取日期范围内步长为30的所有日期
                month = getDays(startTimeStr, endTimeStr, MONTH, lastNum, firstType);
                for (int i = 0; i < month.size(); i++) {
                    map.put(month.get(i), buildLevelTwoRepairScheduling(rule, lastNum, month, i, MONTH));
                }
                break;
            case 90:
                if (lastNum == 0 && firstType > 0) {
                    startTimeStr = DateUtils.getDayBefore(startTimeStr, -firstType);
                } else if (firstType == 0) {
                    firstType = QUARTER;
                }
                // 获取日期范围内步长为90的所有日期
                quarter = getDays(startTimeStr, endTimeStr, QUARTER, lastNum, firstType);
                for (int i = 0; i < quarter.size(); i++) {
                    map.put(quarter.get(i), buildLevelTwoRepairScheduling(rule, lastNum, quarter, i, QUARTER));
                }
                break;
            case 180:
                if (lastNum == 0 && firstType > 0) {
                    startTimeStr = DateUtils.getDayBefore(startTimeStr, -firstType);
                } else if (firstType == 0) {
                    firstType = HALF_YEAR;
                }
                // 获取日期范围内步长为180的所有日期
                halfYear = getDays(startTimeStr, endTimeStr, HALF_YEAR, lastNum, firstType);
                for (int i = 0; i < halfYear.size(); i++) {
                    map.put(halfYear.get(i), buildLevelTwoRepairScheduling(rule, lastNum, halfYear, i, HALF_YEAR));
                }
                break;
            case 360:
                if (lastNum == 0 && firstType > 0) {
                    startTimeStr = DateUtils.getDayBefore(startTimeStr, -firstType);
                } else if (firstType == 0) {
                    firstType = YEAR;
                }
                // 获取日期范围内步长为360的所有日期
                year = getDays(startTimeStr, endTimeStr, YEAR, lastNum, firstType);
                for (int i = 0; i < year.size(); i++) {
                    map.put(year.get(i), buildLevelTwoRepairScheduling(rule, lastNum, year, i, YEAR));
                }
                break;
            default:
                break;
        }
        return firstType;
    }

    /**
     * 获取日期范围内步长为dataType的所有日期
     * @param startTime 起始日期
     * @param endTime 截止日期
     * @param dateType 时间类型
     * @param lastNum 上一次触发的排期数
     * @param firstType 二级修时间最短的检修包
     * @return 日期列表
     * @throws ParseException 异常
     */
    private List<String> getDays(String startTime, String endTime, Integer dateType, int lastNum, int firstType) throws ParseException {
        // 上一次已触发排期存在时，向前追溯修改起始时间
        String changeStartTime = startTime;
        if (lastNum != 0) {
            changeStartTime = DateUtils.getDayBefore(startTime, -(lastNum % dateType));
        }
        // 获取日期范围内，指定日期的固定步长日期的所有日期
        List<String> days = DateUtils.getAllTimesWithinRange(changeStartTime, endTime, dateType);
        // 满足一定条件时，去除第一个排期时间
        if (dateType == MONTH || dateType == ONE_LEVEL_REPAIR_FOUR) {
            if (lastNum != 0 && StringUtils.isNotEmpty(days)) {
                days.remove(0);
            }
        } else {
            boolean bool = (lastNum != 0 || (0 < firstType && firstType < dateType)) && StringUtils.isNotEmpty(days);
            if (bool) {
                days.remove(0);
            }
        }
        return days;
    }

    /**
     * 一级修排期-组装排期信息
     * @param rule 工单触发规则信息
     * @param lastNum 上一次触发的排期数
     * @param days 日期列表
     * @param i    步长
     * @return 排期信息
     */
    private SchedulingBuildResDTO buildLevelOneRepairScheduling(SchedulingRuleResDTO rule, Integer lastNum,
                                                                List<String> days, int i) {
        SchedulingBuildResDTO res = new SchedulingBuildResDTO();
        BeanUtils.copyProperties(rule, res);
        res.setDay(days.get(i));
        int name = (lastNum + ((i + 1) * ONE_LEVEL_REPAIR_FOUR)) % ONE_LEVEL_REPAIR_EIGHT;
        res.setSchedulingName(String.valueOf(name == 0 ? ONE_LEVEL_REPAIR_EIGHT : name));
        res.setRecId(TokenUtils.getUuId());
        res.setIsTrigger("0");
        res.setType("1");
        res.setDateType(ONE_LEVEL_REPAIR_FOUR);
        res.setIsDuration("1");
        res.setPackageType("");
        return res;
    }

    /**
     * 二级修排期-组装排期信息
     * @param rule 工单触发规则信息
     * @param lastNum 上一次触发的排期数
     * @param days 日期列表
     * @param i 步长
     * @param dateType 时间类型
     * @return 排期信息
     */
    private SchedulingBuildResDTO buildLevelTwoRepairScheduling(SchedulingRuleResDTO rule, Integer lastNum,
                                                                List<String> days, int i, Integer dateType) {
        SchedulingBuildResDTO res = new SchedulingBuildResDTO();
        BeanUtils.copyProperties(rule, res);
        res.setDay(days.get(i));
        int name = (lastNum - lastNum % dateType + ((i + 1) * dateType)) % YEAR;
        res.setSchedulingName(String.valueOf(name == 0 ? YEAR : name));
        res.setRecId(TokenUtils.getUuId());
        res.setIsTrigger("0");
        res.setType("2");
        res.setDateType(dateType);
        if (dateType == QUARTER || dateType == HALF_YEAR || dateType == YEAR) {
            res.setIsDuration("0");
        } else {
            res.setIsDuration("1");
        }
        res.setPackageType(String.valueOf(dateType));
        return res;
    }


    /**
     * 二级修排期-根据日期类型，递归延期排期数据
     * @param list 排期数据
     * @return 修改后的排期数据
     * @throws ParseException 异常
     */
    private List<SchedulingBuildResDTO> buildDurationSchedulingList(List<SchedulingBuildResDTO> list) throws ParseException {
        if (list.stream().anyMatch(scheduling -> CommonConstants.ZERO_STRING.equals(scheduling.getIsDuration()))) {
            for (int i = 0; i < list.size(); i++) {
                // 90包后的天数延后一天
                boolean quarterBool = (Integer.parseInt(list.get(i).getSchedulingName()) % QUARTER == 0) &&
                        CommonConstants.ZERO_STRING.equals(list.get(i).getIsDuration()) && list.get(i).getDateType() == QUARTER;
                if (quarterBool) {
                    for (int j = i + 1; j < list.size(); j++) {
                        list.get(j).setDay(DateUtils.addDayDay(list.get(j).getDay(), PLAN_DAY_MAP.get(QUARTER) - 1));
                    }
                    list.get(i).setIsDuration("1");
                    break;
                }
                // 180包后的天数延后一天
                boolean halfYearBool = (Integer.parseInt(list.get(i).getSchedulingName()) % HALF_YEAR == 0) &&
                        CommonConstants.ZERO_STRING.equals(list.get(i).getIsDuration()) && list.get(i).getDateType() == HALF_YEAR;
                if (halfYearBool) {
                    for (int j = i + 1; j < list.size(); j++) {
                        list.get(j).setDay(DateUtils.addDayDay(list.get(j).getDay(), PLAN_DAY_MAP.get(HALF_YEAR) - 1));
                    }
                    list.get(i).setIsDuration("1");
                    break;
                }
                // 360包后的天数延后四天
                boolean yearBool = (Integer.parseInt(list.get(i).getSchedulingName()) % YEAR == 0) &&
                        CommonConstants.ZERO_STRING.equals(list.get(i).getIsDuration()) && list.get(i).getDateType() == YEAR;
                if (yearBool) {
                    for (int j = i + 1; j < list.size(); j++) {
                        list.get(j).setDay(DateUtils.addDayDay(list.get(j).getDay(), PLAN_DAY_MAP.get(YEAR) - 1));
                    }
                    list.get(i).setIsDuration("1");
                    break;
                }
            }
            return buildDurationSchedulingList(list);
        } else {
            return list;
        }
    }

}
