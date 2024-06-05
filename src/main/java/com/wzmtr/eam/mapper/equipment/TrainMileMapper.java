package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileageReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.SumDailyMileResDTO;
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
     * 获取每日列车里程及能耗列表
     * * @param pageReqDTO 分页参数
     * @param day 时间
     * @param equipCode 设备编号
     * @param equipName 车号
     * @return 每日列车里程及能耗列表
     */
    Page<TrainMileDailyResDTO> pageTrainDailyMile(Page<TrainMileDailyResDTO> page, String day, String equipCode, String equipName);

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
     * 导出每日列车里程及能耗列表
     * @param ids ids
     * @return 每日列车里程及能耗列表
     */
    List<TrainMileDailyResDTO> exportTrainDailyMile(List<String> ids);

    /**
     * 导入每日列车里程及能耗列表
     * @param req 每日列车里程及能耗
     */
    void importTrainDailyMile(TrainMileDailyReqDTO req);

    /**
     * 批量新增每日列车里程及能耗
     * @param list 列表
     */
    void batchAddTrainMile(List<TrainMileDailyReqDTO> list);

    /**
     * 获取前一天的列车累计运营里程
     * @param equipCode 设备编码
     * @param day 日期
     * @return 前一天的列车累计运营里程
     */
    Double getLastTotalWorkMile(String equipCode, String day);

    /**
     * 初始化每日列车里程及能耗
     * @param dateDays 初始化日期
     * @param trains 列车信息
     */
    void initTrainDailyMile(List<String> dateDays, List<RegionResDTO> trains);

    /**
     * 根据日期获取当天所有列车的总里程（含非运营）总数
     * @param day 日期
     * @return 总数
     */
    SumDailyMileResDTO getSumDailyMileByDay(String day);

}
