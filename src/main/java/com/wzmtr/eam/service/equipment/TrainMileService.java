package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.TrainMileDailyReqDTO;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileDailyResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileageResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface TrainMileService {

    Page<TrainMileResDTO> pageTrainMile(String equipCode, String equipName, String originLineNo, PageReqDTO pageReqDTO);

    TrainMileResDTO getTrainMileDetail(String id);

    void exportTrainMile(String equipCode, String equipName, String originLineNo, HttpServletResponse response) throws IOException;

    void modifyTrainMile(TrainMileReqDTO trainMileReqDTO);

    Page<TrainMileageResDTO> pageTrainMileage(String startTime, String endTime, String equipCode, PageReqDTO pageReqDTO);

    TrainMileageResDTO getTrainMileageDetail(String id);

    void exportTrainMileage(String startTime, String endTime, String equipCode, HttpServletResponse response) throws IOException;

    /**
     * 获取每日列车里程及能耗列表
     * @param day 时间
     * @param equipCode 设备编号
     * @param equipName 车号
     * @param pageReqDTO 分页参数
     * @return 每日列车里程及能耗列表
     */
    Page<TrainMileDailyResDTO> pageTrainDailyMile(String day, String equipCode, String equipName, PageReqDTO pageReqDTO);

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
     * @param baseIdsEntity ids
     */
    void deleteTrainDailyMile(BaseIdsEntity baseIdsEntity);

    /**
     * 导出每日列车里程及能耗列表
     * @param ids ids
     * @param response res
     * @throws IOException 流异常
     */
    void exportTrainDailyMile(List<String> ids, HttpServletResponse response) throws IOException;

    /**
     * 导入每日列车里程及能耗列表
     * @param file 文件
     * @throws ParseException 异常
     */
    void importTrainDailyMile(MultipartFile file) throws ParseException;

}
