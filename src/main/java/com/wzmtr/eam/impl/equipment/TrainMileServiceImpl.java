package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileageReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelTrainMileDailyReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.SumDailyMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileDailyResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileageResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelTrainMileDailyResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelTrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelTrainMileageResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.TrainMileMapper;
import com.wzmtr.eam.service.equipment.TrainMileService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class TrainMileServiceImpl implements TrainMileService {

    @Autowired
    private TrainMileMapper trainMileMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public Page<TrainMileResDTO> pageTrainMile(String equipCode, String equipName, String originLineNo, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMileMapper.pageTrainMile(pageReqDTO.of(), equipCode, equipName, originLineNo);
    }

    @Override
    public TrainMileResDTO getTrainMileDetail(String id) {
        return trainMileMapper.getTrainMileDetail(id);
    }

    @Override
    public void exportTrainMile(String equipCode, String equipName, String originLineNo, HttpServletResponse response) throws IOException {
        List<TrainMileResDTO> trainMileResDTOList = trainMileMapper.listTrainMile(equipCode, equipName, originLineNo);
        if (trainMileResDTOList != null && !trainMileResDTOList.isEmpty()) {
            List<ExcelTrainMileResDTO> list = new ArrayList<>();
            for (TrainMileResDTO resDTO : trainMileResDTOList) {
                ExcelTrainMileResDTO res = new ExcelTrainMileResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                res.setTotalTractionEnergy(String.valueOf(resDTO.getTotalTractionEnergy()));
                res.setTotalAuxiliaryEnergy(String.valueOf(resDTO.getTotalAuxiliaryEnergy()));
                res.setTotalRegenratedElectricity(String.valueOf(resDTO.getTotalRegenratedElectricity()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "车辆走行里程信息", list);
        }
    }

    @Override
    public void modifyTrainMile(TrainMileReqDTO trainMileReqDTO) {
        TrainMileResDTO oldRes = trainMileMapper.getTrainMileDetail(trainMileReqDTO.getRecId());
        if (Objects.isNull(oldRes)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        BigDecimal oldMile = oldRes.getTotalMiles();
        BigDecimal newMile = new BigDecimal(trainMileReqDTO.getTotalMiles().trim());
        BigDecimal oldTraction = oldRes.getTotalTractionEnergy();
        BigDecimal newTraction = new BigDecimal(trainMileReqDTO.getTotalTractionEnergy().trim());
        BigDecimal oldAuxiliary = oldRes.getTotalAuxiliaryEnergy();
        BigDecimal newAuxiliary = new BigDecimal(trainMileReqDTO.getTotalAuxiliaryEnergy().trim());
        BigDecimal oldRegenrat = oldRes.getTotalRegenratedElectricity();
        BigDecimal newRegenrat = new BigDecimal(trainMileReqDTO.getTotalRegenratedElectricity().trim());
        trainMileMapper.updateTrainMile(trainMileReqDTO);
        BigDecimal milesIncrement;
        BigDecimal tractionIncrement;
        BigDecimal auxiliaryIncrement;
        BigDecimal regenratedIncrement;
        if (oldMile != null && oldTraction != null && oldAuxiliary != null && oldRegenrat != null) {
            milesIncrement = newMile.subtract(oldMile);
            tractionIncrement = newTraction.subtract(oldTraction);
            auxiliaryIncrement = newAuxiliary.subtract(oldAuxiliary);
            regenratedIncrement = newRegenrat.subtract(oldRegenrat);
        } else {
            milesIncrement = newMile;
            tractionIncrement = newTraction;
            auxiliaryIncrement = newAuxiliary;
            regenratedIncrement = newRegenrat;
        }
        TrainMileageReqDTO trainMileageReqDTO = new TrainMileageReqDTO();
        BeanUtils.copyProperties(trainMileReqDTO, trainMileageReqDTO);
        trainMileageReqDTO.setTotalMiles(new BigDecimal(trainMileReqDTO.getTotalMiles()));
        trainMileageReqDTO.setTotalTractionEnergy(new BigDecimal(trainMileReqDTO.getTotalTractionEnergy()));
        trainMileageReqDTO.setTotalAuxiliaryEnergy(new BigDecimal(trainMileReqDTO.getTotalAuxiliaryEnergy()));
        trainMileageReqDTO.setTotalRegenratedElectricity(new BigDecimal(trainMileReqDTO.getTotalRegenratedElectricity()));
        trainMileageReqDTO.setMilesIncrement(milesIncrement);
        trainMileageReqDTO.setTractionIncrement(tractionIncrement);
        trainMileageReqDTO.setAuxiliaryIncrement(auxiliaryIncrement);
        trainMileageReqDTO.setRegenratedIncrement(regenratedIncrement);
        trainMileageReqDTO.setRecId(TokenUtils.getUuId());
        trainMileageReqDTO.setFillinUserId(TokenUtils.getCurrentPersonId());
        trainMileageReqDTO.setFillinTime(DateUtils.getCurrentTime());
        trainMileageReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        trainMileageReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        trainMileageReqDTO.setRecStatus("0");
        trainMileMapper.insertTrainMileage(trainMileageReqDTO);
    }

    @Override
    public Page<TrainMileageResDTO> pageTrainMileage(String startTime, String endTime, String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMileMapper.pageTrainMileage(pageReqDTO.of(), startTime, endTime, equipCode);
    }

    @Override
    public TrainMileageResDTO getTrainMileageDetail(String id) {
        return trainMileMapper.getTrainMileageDetail(id);
    }

    @Override
    public void exportTrainMileage(String startTime, String endTime, String equipCode, HttpServletResponse response) throws IOException {
        List<TrainMileageResDTO> trainMileageResDTOList = trainMileMapper.listTrainMileage(startTime, endTime, equipCode);
        if (trainMileageResDTOList != null && !trainMileageResDTOList.isEmpty()) {
            List<ExcelTrainMileageResDTO> list = new ArrayList<>();
            for (TrainMileageResDTO resDTO : trainMileageResDTOList) {
                ExcelTrainMileageResDTO res = new ExcelTrainMileageResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                res.setMilesIncrement(String.valueOf(resDTO.getMilesIncrement()));
                res.setTotalTractionEnergy(String.valueOf(resDTO.getTotalTractionEnergy()));
                res.setTractionIncrement(String.valueOf(resDTO.getTractionIncrement()));
                res.setTotalAuxiliaryEnergy(String.valueOf(resDTO.getTotalAuxiliaryEnergy()));
                res.setAuxiliaryIncrement(String.valueOf(resDTO.getAuxiliaryIncrement()));
                res.setTotalRegenratedElectricity(String.valueOf(resDTO.getTotalRegenratedElectricity()));
                res.setRegenratedIncrement(String.valueOf(resDTO.getRegenratedIncrement()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "车辆走行里程历史信息", list);
        }
    }

    @Override
    public Page<TrainMileDailyResDTO> pageTrainDailyMile(String day, String equipCode, String equipName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMileMapper.pageTrainDailyMile(pageReqDTO.of(), day, equipCode, equipName);
    }

    @Override
    public TrainMileDailyResDTO getTrainDailyMileDetail(String id) {
        return trainMileMapper.getTrainDailyMileDetail(id);
    }

    @Override
    public void addTrainDailyMile(TrainMileDailyReqDTO trainMileDailyReqDTO) {
        List<TrainMileDailyResDTO> list = trainMileMapper.listTrainDailyMile(trainMileDailyReqDTO.getDay(), trainMileDailyReqDTO.getEquipCode());
        if (StringUtils.isNotEmpty(list)) {
            throw new CommonException(ErrorCode.TRAIN_MILE_DAILY_EXIST, trainMileDailyReqDTO.getEquipName());
        }
        trainMileDailyReqDTO.setRecId(TokenUtils.getUuId());
        trainMileDailyReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        trainMileDailyReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        // todo 获取车辆PHM系统数据

        trainMileMapper.addTrainDailyMile(trainMileDailyReqDTO);
        // 修改设备表相关数据
        TrainMileReqDTO trainMileReqDTO = new TrainMileReqDTO();
        BeanUtils.copyProperties(trainMileDailyReqDTO, trainMileReqDTO);
        trainMileReqDTO.setRecId(null);
        trainMileMapper.updateTrainMile(trainMileReqDTO);
    }

    @Override
    public void modifyTrainDailyMile(TrainMileDailyReqDTO trainMileDailyReqDTO) {
        trainMileDailyReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        trainMileDailyReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        // todo 获取车辆PHM系统数据

        trainMileMapper.modifyTrainDailyMile(trainMileDailyReqDTO);
        // 修改设备表相关数据
        TrainMileReqDTO trainMileReqDTO = new TrainMileReqDTO();
        BeanUtils.copyProperties(trainMileDailyReqDTO, trainMileReqDTO);
        trainMileReqDTO.setRecId(null);
        if (StringUtils.isNotNull(trainMileDailyReqDTO.getTotalWorkMile())) {
            trainMileReqDTO.setTotalMiles(String.valueOf(trainMileDailyReqDTO.getTotalWorkMile()));
        }
        if (StringUtils.isNotNull(trainMileDailyReqDTO.getTotalTractionEnergy())) {
            trainMileReqDTO.setTotalTractionEnergy(String.valueOf(trainMileDailyReqDTO.getTotalTractionEnergy()));
        }
        if (StringUtils.isNotNull(trainMileDailyReqDTO.getTotalAuxiliaryEnergy())) {
            trainMileReqDTO.setTotalAuxiliaryEnergy(String.valueOf(trainMileDailyReqDTO.getTotalAuxiliaryEnergy()));
        }
        if (StringUtils.isNotNull(trainMileDailyReqDTO.getTotalRegenratedElectricity())) {
            trainMileReqDTO.setTotalRegenratedElectricity(String.valueOf(trainMileDailyReqDTO.getTotalRegenratedElectricity()));
        }
        trainMileMapper.updateTrainMile(trainMileReqDTO);
    }

    @Override
    public void deleteTrainDailyMile(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            trainMileMapper.deleteTrainDailyMile(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            for (String id : baseIdsEntity.getIds()) {
                TrainMileDailyResDTO trainCodeRes = trainMileMapper.getTrainDailyMileDetail(id);
                if (!Objects.isNull(trainCodeRes)) {
                    List<TrainMileDailyResDTO> list = trainMileMapper.listTrainDailyMile(null, trainCodeRes.getEquipCode());
                    if (StringUtils.isNotEmpty(list)) {
                        TrainMileReqDTO trainMileReqDTO = new TrainMileReqDTO();
                        BeanUtils.copyProperties(list.get(0), trainMileReqDTO);
                        trainMileReqDTO.setRecId(null);
                        trainMileMapper.updateTrainMile(trainMileReqDTO);
                    }
                }
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportTrainDailyMile(List<String> ids, HttpServletResponse response) throws IOException {
        List<TrainMileDailyResDTO> trainMileageResDTOList = trainMileMapper.exportTrainDailyMile(ids);
        if (trainMileageResDTOList != null && !trainMileageResDTOList.isEmpty()) {
            List<ExcelTrainMileDailyResDTO> list = new ArrayList<>();
            for (TrainMileDailyResDTO resDTO : trainMileageResDTOList) {
                ExcelTrainMileDailyResDTO res = new ExcelTrainMileDailyResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "每日列车里程及能耗", list);
        }
    }

    /**
     * 导入每日列车里程及能耗列表
     * @param file 文件
     */
    @Override
    public void importTrainDailyMile(MultipartFile file) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = sdf.parse(sdf.format(new Date()));
        List<ExcelTrainMileDailyReqDTO> list = EasyExcelUtils.read(file, ExcelTrainMileDailyReqDTO.class);
        if (StringUtils.isNotEmpty(list)) {
            // 判断导入的日期是否在当前日期周期之前
            for (ExcelTrainMileDailyReqDTO reqDTO : list) {
                if (now.before(sdf.parse(reqDTO.getDay() + " 03:00:00"))) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "导入文件中存在当前日期周期后的数据，请修改后重新导入");
                }
            }
            for (ExcelTrainMileDailyReqDTO reqDTO : list) {
                TrainMileDailyReqDTO req = new TrainMileDailyReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setRecId(TokenUtils.getUuId());
                EquipmentResDTO equipment = equipmentMapper.getEquipByName(req.getEquipName());
                if (StringUtils.isNotNull(equipment)) {
                    req.setEquipCode(equipment.getEquipCode());
                }
                Double totalWorkMile = trainMileMapper.getLastTotalWorkMile(req.getEquipCode(), req.getDay());
                if (StringUtils.isNotNull(totalWorkMile)) {
                    req.setTotalWorkMile(BigDecimal.valueOf(totalWorkMile).add(req.getDailyWorkMile()));
                }
                req.setRecRevisor(TokenUtils.getCurrentPersonId());
                req.setRecReviseTime(DateUtils.getCurrentTime());
                // 根据导入数据修改值
                trainMileMapper.importTrainDailyMile(req);
            }
        }
    }

    @Override
    public void initTrainDailyMile(String startTime, String endTime) {
        List<RegionResDTO> trains = equipmentMapper.listTrainRegion("02");
        List<String> dates = DateUtils.getDatesBetween(startTime, endTime);
        if (StringUtils.isNotEmpty(dates) && StringUtils.isNotEmpty(trains)) {
            int times = (int) Math.ceil(dates.size() / 80.0);
            for (int i = 0; i < times; i++) {
                trainMileMapper.initTrainDailyMile(dates.subList(i * 80, Math.min((i + 1) * 80, dates.size() - 1)), trains);
            }
        }
    }

    @Override
    public SumDailyMileResDTO getSumDailyMileByDay(String day) {
        return trainMileMapper.getSumDailyMileByDay(day);
    }

}
