package com.wzmtr.eam.service.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.TrainMileageReqDTO;
import com.wzmtr.eam.dto.res.TrainMileResDTO;
import com.wzmtr.eam.dto.res.TrainMileageResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.TrainMileMapper;
import com.wzmtr.eam.service.equipment.TrainMileService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
    public void exportTrainMile(String equipCode, String equipName, String originLineNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "车号", "里程(公里)", "牵引总能耗(kW·h)",
                "辅助总能耗(kW·h)", "再生总电量(kW·h)", "备注", "维护时间");
        List<TrainMileResDTO> trainMileResDTOList = trainMileMapper.listTrainMile(equipCode, equipName, originLineNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (trainMileResDTOList != null && !trainMileResDTOList.isEmpty()) {
            for (TrainMileResDTO trainMileResDTO : trainMileResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", trainMileResDTO.getRecId());
                map.put("设备编码", trainMileResDTO.getEquipCode());
                map.put("车号", trainMileResDTO.getEquipName());
                map.put("里程(公里)", String.valueOf(trainMileResDTO.getTotalMiles()));
                map.put("牵引总能耗(kW·h)", String.valueOf(trainMileResDTO.getTotalTractionEnergy()));
                map.put("辅助总能耗(kW·h)", String.valueOf(trainMileResDTO.getTotalAuxiliaryEnergy()));
                map.put("再生总电量(kW·h)", String.valueOf(trainMileResDTO.getTotalRegenratedElectricity()));
                map.put("备注", trainMileResDTO.getRemark());
                map.put("维护时间", trainMileResDTO.getFillinTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("车辆走行里程信息", listName, list, null, response);
    }

    @Override
    public void modifyTrainMile(List<TrainMileReqDTO> list) {
        if (list != null && !list.isEmpty()) {
            for (TrainMileReqDTO resDTO : list) {
                TrainMileResDTO oldRes = trainMileMapper.getTrainMileDetail(resDTO.getRecId());
                if (Objects.isNull(oldRes)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                BigDecimal oldMile = oldRes.getTotalMiles();
                BigDecimal newMile = new BigDecimal(resDTO.getTotalMiles().trim());
                BigDecimal oldTraction = oldRes.getTotalTractionEnergy();
                BigDecimal newTraction = new BigDecimal(resDTO.getTotalTractionEnergy().trim());
                BigDecimal oldAuxiliary = oldRes.getTotalAuxiliaryEnergy();
                BigDecimal newAuxiliary = new BigDecimal(resDTO.getTotalAuxiliaryEnergy().trim());
                BigDecimal oldRegenrat = oldRes.getTotalRegenratedElectricity();
                BigDecimal newRegenrat = new BigDecimal(resDTO.getTotalRegenratedElectricity().trim());
                trainMileMapper.updateTrainMile(resDTO);
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
                BeanUtils.copyProperties(resDTO, trainMileageReqDTO);
                trainMileageReqDTO.setMilesIncrement(milesIncrement);
                trainMileageReqDTO.setTractionIncrement(tractionIncrement);
                trainMileageReqDTO.setAuxiliaryIncrement(auxiliaryIncrement);
                trainMileageReqDTO.setRegenratedIncrement(regenratedIncrement);
                trainMileageReqDTO.setRecId(TokenUtil.getUuId());
                trainMileageReqDTO.setEquipName(resDTO.getEquipName());
                trainMileageReqDTO.setFillinUserId(TokenUtil.getCurrentPersonId());
                trainMileageReqDTO.setFillinTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                trainMileageReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                trainMileageReqDTO.setRecCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                trainMileageReqDTO.setRecStatus("0");
                trainMileageReqDTO.setRemark(resDTO.getRemark());
                trainMileMapper.insertTrainMileage(trainMileageReqDTO);
            }

        }
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
    public void exportTrainMileage(String startTime, String endTime, String equipCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "车号", "里程(公里)", "增加里程(公里)", "填报时间", "填报人",
                "牵引总能耗(kW·h)", "牵引能耗增量", "辅助总能耗(kW·h)", "辅助能耗增量", "再生总电量(kW·h)", "再生电量增量", "备注");
        List<TrainMileageResDTO> trainMileageResDTOList = trainMileMapper.listTrainMileage(startTime, endTime, equipCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (trainMileageResDTOList != null && !trainMileageResDTOList.isEmpty()) {
            for (TrainMileageResDTO trainMileageResDTO : trainMileageResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", trainMileageResDTO.getRecId());
                map.put("设备编码", trainMileageResDTO.getEquipCode());
                map.put("车号", trainMileageResDTO.getEquipName());
                map.put("里程(公里)", String.valueOf(trainMileageResDTO.getTotalMiles()));
                map.put("增加里程(公里)", String.valueOf(trainMileageResDTO.getMilesIncrement()));
                map.put("填报时间", trainMileageResDTO.getFillinTime());
                map.put("填报人", trainMileageResDTO.getFillinUserName());
                map.put("牵引总能耗(kW·h)", String.valueOf(trainMileageResDTO.getTotalTractionEnergy()));
                map.put("牵引能耗增量", String.valueOf(trainMileageResDTO.getTractionIncrement()));
                map.put("辅助总能耗(kW·h)", String.valueOf(trainMileageResDTO.getTotalAuxiliaryEnergy()));
                map.put("辅助能耗增量", String.valueOf(trainMileageResDTO.getAuxiliaryIncrement()));
                map.put("再生总电量(kW·h)", String.valueOf(trainMileageResDTO.getTotalRegenratedElectricity()));
                map.put("再生电量增量", String.valueOf(trainMileageResDTO.getRegenratedIncrement()));
                map.put("备注", trainMileageResDTO.getRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("车辆走行里程历史信息", listName, list, null, response);
    }

}
