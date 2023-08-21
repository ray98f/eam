package com.wzmtr.eam.impl.statistic;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.*;
import com.wzmtr.eam.mapper.statistic.*;
import com.wzmtr.eam.service.statistic.StatisticService;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        Page<MaterialResDTO> page = materialMapper.query(reqDTO.of(), reqDTO.getPlanName(), reqDTO.getMatName(), reqDTO.getStartTime(), reqDTO.getEndTime(), reqDTO.getTrainNo());
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return new Page<>();
        }
        return page;
    }

    @Override
    public Page<CarFaultQueryResDTO> query(CarFaultQueryReqDTO reqDTO) {
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
    public InspectionJobListResDTO querydmer3(OneCarOneGearQueryReqDTO reqDTO) {
        return oneCarOneGearMapper.querydmer3(reqDTO);
    }
}
