package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileageReqDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileDailyResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileageResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TrainMileMapper {

    Page<TrainMileResDTO> pageTrainMile(Page<TrainMileResDTO> page, String equipCode, String equipName, String originLineNo);

    TrainMileResDTO getTrainMileDetail(String id);

    List<TrainMileResDTO> listTrainMile(String equipCode, String equipName, String originLineNo);

    void updateTrainMile(TrainMileReqDTO trainMileReqDTO);

    void insertTrainMileage(TrainMileageReqDTO trainMileageReqDTO);

    Page<TrainMileageResDTO> pageTrainMileage(Page<TrainMileageResDTO> page, String startTime, String endTime, String equipCode);

    TrainMileageResDTO getTrainMileageDetail(String id);

    List<TrainMileageResDTO> listTrainMileage(String startTime, String endTime, String equipCode);

    /**
     * 分页获取每日列车里程及能耗列表
     * @param page 分页
     * @param day 时间
     * @param equipCode 设备编号
     * @return 每日列车里程及能耗列表
     */
    Page<TrainMileDailyResDTO> pageTrainDailyMile(Page<TrainMileDailyResDTO> page, String day, String equipCode);

    /**
     * 获取每日列车里程及能耗详情
     * @param id 主键id
     * @return 每日列车里程及能耗详情
     */
    TrainMileDailyResDTO getTrainDailyMileDetail(String id);

    /**
     * 新增当日列车里程及能耗
     * @param trainMileDailyReqDTO 当日列车里程及能耗返回类
     */
    void addTrainDailyMile(TrainMileDailyReqDTO trainMileDailyReqDTO);

    /**
     * 修改当日列车里程及能耗
     * @param trainMileDailyReqDTO 当日列车里程及能耗返回类
     */
    void modifyTrainDailyMile(TrainMileDailyReqDTO trainMileDailyReqDTO);

    /**
     * 删除当日列车里程及能耗
     * @param ids ids
     * @param userId 操作人id
     * @param time 操作时间
     */
    void deleteTrainDailyMile(List<String> ids, String userId, String time);

    /**
     * 获取每日列车里程及能耗列表
     * @param day 时间
     * @param equipCode 设备编号
     * @return 每日列车里程及能耗列表
     */
    List<TrainMileDailyResDTO> listTrainDailyMile(String day, String equipCode);

    /**
     * 导入每日列车里程及能耗列表
     * @param list 每日列车里程及能耗列表
     */
    void importTrainDailyMile(List<TrainMileDailyReqDTO> list);

}
