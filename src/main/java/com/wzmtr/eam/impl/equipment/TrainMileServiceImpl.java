package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileageReqDTO;
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
import com.wzmtr.eam.mapper.equipment.TrainMileMapper;
import com.wzmtr.eam.service.equipment.TrainMileService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class TrainMileServiceImpl implements TrainMileService {

    @Autowired
    private TrainMileMapper trainMileMapper;

    @Override
    public Page<TrainMileResDTO> pageTrainMile(String equipCode, String equipName, String originLineNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        trainMileageReqDTO.setRecId(TokenUtil.getUuId());
        trainMileageReqDTO.setFillinUserId(TokenUtil.getCurrentPersonId());
        trainMileageReqDTO.setFillinTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        trainMileageReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        trainMileageReqDTO.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        trainMileageReqDTO.setRecStatus("0");
        trainMileMapper.insertTrainMileage(trainMileageReqDTO);
    }

    @Override
    public Page<TrainMileageResDTO> pageTrainMileage(String startTime, String endTime, String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
    public Page<TrainMileDailyResDTO> pageTrainDailyMile(String day, String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMileMapper.pageTrainDailyMile(pageReqDTO.of(), day, equipCode);
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
        trainMileDailyReqDTO.setRecId(TokenUtil.getUuId());
        trainMileDailyReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        trainMileDailyReqDTO.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
        trainMileDailyReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        trainMileDailyReqDTO.setRecReviseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // todo 获取车辆PHM系统数据

        trainMileMapper.modifyTrainDailyMile(trainMileDailyReqDTO);
        // 修改设备表相关数据
        TrainMileReqDTO trainMileReqDTO = new TrainMileReqDTO();
        BeanUtils.copyProperties(trainMileDailyReqDTO, trainMileReqDTO);
        trainMileReqDTO.setRecId(null);
        trainMileMapper.updateTrainMile(trainMileReqDTO);
    }

    @Override
    public void deleteTrainDailyMile(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            trainMileMapper.deleteTrainDailyMile(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
    public void exportTrainDailyMile(String day, String equipCode, HttpServletResponse response) throws IOException {
        List<TrainMileDailyResDTO> trainMileageResDTOList = trainMileMapper.listTrainDailyMile(day, equipCode);
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

}
