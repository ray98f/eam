package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EquipmentService {

    /**
     * 获取列车列表
     * @param lineCode 线路编号
     * @return 列车列表
     */
    List<RegionResDTO> listTrainRegion(String lineCode);

    /**
     * 获取设备树
     * @param lineCode 线路编号
     * @param regionCode 位置编号
     * @param recId 位置id
     * @param parentNodeRecId 父位置id
     * @param equipmentCategoryCode 设备分类编号
     * @return 设备树
     */
    EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode);

    /**
     * 获取设备台账列表
     * @param equipCode 设备编码
     * @param equipName 设备名称
     * @param useLineNo 线路编号
     * @param useSegNo 线段编号
     * @param position1Code 位置一
     * @param majorCode 专业编号
     * @param systemCode 系统编号
     * @param equipTypeCode 设备分类编号
     * @param brand 品牌
     * @param startTime 出产开始时间
     * @param endTime 出产结束时间
     * @param manufacture 生产厂家
     * @param pageReqDTO 分页参数
     * @return 设备台账列表
     */
    Page<EquipmentResDTO> pageEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, PageReqDTO pageReqDTO);

    List<EquipmentResDTO> allList(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
            String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture);

    EquipmentResDTO getEquipmentDetail(String id);

    /**
     * 新增设备台账
     * @param equipmentReqDTO 设备台账信息
     */
    void addEquipment(EquipmentReqDTO equipmentReqDTO);

    /**
     * 编辑设备台账
     * @param equipmentReqDTO 设备台账信息
     */
    void modifyEquipment(EquipmentReqDTO equipmentReqDTO);

    /**
     * 删除设备台账
     * @param baseIdsEntity ids
     */
    void deleteEquipment(BaseIdsEntity baseIdsEntity);

    void importEquipment(MultipartFile file);

    /**
     * 导出设备台账
     * @param ids ids
     * @param response response
     * @throws IOException 异常
     */
    void exportEquipment(List<String> ids, HttpServletResponse response) throws IOException;

    /**
     * 生成二维码
     * @param baseIdsEntity ids
     * @return 二维码列表
     * @throws ParseException 异常
     */
    List<EquipmentQrResDTO> generateQr(BaseIdsEntity baseIdsEntity) throws ParseException;

    Page<OverhaulOrderDetailResDTO> listOverhaul(String equipCode, PageReqDTO pageReqDTO);

    Page<FaultDetailResDTO> listFault(String equipCode, PageReqDTO pageReqDTO);

    Page<PartReplaceResDTO> listPartReplace(String equipCode, PageReqDTO pageReqDTO);
}
