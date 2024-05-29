package com.wzmtr.eam.service.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.OtherEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.OtherEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 其他设备管理-其他设备台账
 * @author  Ray
 * @version 1.0
 * @date 2024/02/02
 */
public interface OtherEquipService {

    /**
     * 获取其他设备台账列表
     * @param equipCode 设备编号
     * @param equipName 设备名称
     * @param otherEquipCode 其他设备编号
     * @param factNo 出厂编号
     * @param useLineNo 线路
     * @param position1Code 位置一
     * @param otherEquipType 其他设备类别
     * @param equipStatus 设备状态
     * @param pageReqDTO 分页信息
     * @return 其他设备台账列表
     */
    Page<OtherEquipResDTO> pageOtherEquip(String equipCode, String equipName, String otherEquipCode, String factNo,
                                          String useLineNo, String position1Code, String otherEquipType, String equipStatus, PageReqDTO pageReqDTO);

    /**
     * 获取其他设备台账详情
     * @param id id
     * @return 设备台账详情
     */
    OtherEquipResDTO getOtherEquipDetail(String id);

    /**
     * 编辑其他设备台账
     * @param otherEquipReqDTO 其他设备台账信息
     */
    void modifyOtherEquip(OtherEquipReqDTO otherEquipReqDTO);

    /**
     * 导入其他设备台账
     * @param file 文件
     * @throws ParseException 异常
     */
    void importOtherEquip(MultipartFile file) throws ParseException;

    /**
     * 导出其他设备台账
     * @param ids ids
     * @param response response
     * @throws IOException 流
     */
    void exportOtherEquip(List<String> ids, HttpServletResponse response) throws IOException;

    /**
     * 获取其他设备检测历史记录列表
     * @param equipCode 设备编码
     * @param pageReqDTO 分页参数
     * @return 其他设备检测历史记录列表
     */
    Page<OtherEquipHistoryResDTO> pageOtherEquipHistory(String equipCode, PageReqDTO pageReqDTO);

    /**
     * 获取其他设备检测历史记录详情
     * @param id id
     * @return 其他设备检测历史记录详情
     */
    OtherEquipHistoryResDTO getOtherEquipHistoryDetail(String id);

    /**
     * 导出其他设备检测历史记录详情
     * @param equipCode 设备编码
     * @param response response
     * @throws IOException 流
     */
    void exportOtherEquipHistory(String equipCode, HttpServletResponse response) throws IOException;
}
