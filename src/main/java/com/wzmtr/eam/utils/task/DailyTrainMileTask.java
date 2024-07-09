package com.wzmtr.eam.utils.task;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.TrainMileMapper;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务-每日列车里程
 * @author  Ray
 * @version 1.0
 * @date 2024/04/03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DailyTrainMileTask {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private TrainMileMapper trainMileMapper;

    /**
     * 触发新增每日列车里程台账
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        List<TrainMileDailyReqDTO> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<RegionResDTO> trains = equipmentMapper.listTrainRegion(CommonConstants.LINE_CODE_ONE);
        if (StringUtils.isNotEmpty(trains)) {
            for (RegionResDTO region : trains) {
                TrainMileDailyReqDTO req = new TrainMileDailyReqDTO();
                req.setRecId(TokenUtils.getUuId());
                req.setEquipCode(region.getNodeCode());
                req.setEquipName(region.getNodeName());
                req.setDay(sdf.format(new Date()));
                Double totalWorkMile = trainMileMapper.getLastTotalWorkMile(req.getEquipCode(), req.getDay());
                if (StringUtils.isNotNull(totalWorkMile)) {
                    req.setTotalWorkMile(BigDecimal.valueOf(totalWorkMile));
                }
                req.setDailyWorkMile(new BigDecimal(0));
                req.setRecCreator("admin");
                req.setRecCreateTime(DateUtils.getCurrentTime());
                list.add(req);
            }
        }
        if (StringUtils.isNotEmpty(list)) {
            trainMileMapper.batchAddTrainMile(list);
        }
    }

}
