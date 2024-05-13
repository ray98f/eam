package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.OtherEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 其他设备管理-其他设备台账
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
@Mapper
@Repository
public interface OtherEquipMapper {

    /**
     * 获取其他设备台账列表
     * @param page 分页信息
     * @param equipCode 设备编号
     * @param equipName 设备名称
     * @param otherEquipCode 其他设备编号
     * @param factNo 出厂编号
     * @param useLineNo 线路
     * @param position1Code 位置一
     * @param otherEquipType 其他设备类别
     * @param equipStatus 设备状态
     * @return 其他设备台账列表
     */
    Page<OtherEquipResDTO> pageOtherEquip(Page<OtherEquipResDTO> page, String equipCode, String equipName, String otherEquipCode, String factNo,
                                          String useLineNo, String position1Code, String otherEquipType, String equipStatus);

    /**
     * 获取其他设备台账详情
     * @param id id
     * @return 设备台账详情
     */
    OtherEquipResDTO getOtherEquipDetail(String id);

    /**
     * 修改设备台账信息
     * @param otherEquipReqDTO 其他设备台账信息
     */
    void updateEquip(OtherEquipReqDTO otherEquipReqDTO);

    /**
     * 判断其他设备台账是否已存在
     * @param otherEquipReqDTO 其他设备台账信息
     * @return 是否已存在
     */
    Integer selectOtherEquipIsExist(OtherEquipReqDTO otherEquipReqDTO);

    /**
     * 新增其他设备台账
     * @param otherEquipReqDTO 其他设备台账参数
     */
    void addOtherEquip(OtherEquipReqDTO otherEquipReqDTO);


    /**
     * 编辑其他设备台账
     * @param otherEquipReqDTO 其他设备台账信息
     */
    void modifyOtherEquip(OtherEquipReqDTO otherEquipReqDTO);

    /**
     * 获取其他设备台账列表
     * @param ids ids
     * @return 其他设备台账列表
     */
    List<OtherEquipResDTO> listOtherEquip(List<String> ids);

    /**
     * 获取其他设备检测历史记录列表
     * @param page 分页参数
     * @param equipCode 设备编码
     * @return 其他设备检测历史记录列表
     */
    Page<OtherEquipHistoryResDTO> pageOtherEquipHistory(Page<OtherEquipHistoryResDTO> page, String equipCode);

    /**
     * 获取其他设备检测历史记录详情
     * @param id id
     * @return 其他设备检测历史记录详情
     */
    OtherEquipHistoryResDTO getOtherEquipHistoryDetail(String id);

    /**
     * 获取其他设备检测历史记录列表
     * @param equipCode 设备编码
     * @return 其他设备检测历史记录列表
     */
    List<OtherEquipHistoryResDTO> listOtherEquipHistory(String equipCode);
}
