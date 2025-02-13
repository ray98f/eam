package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.EquipmentExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentSiftReqDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.basic.LineResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface EquipmentMapper {

    /**
     * 获取列车列表
     * @param lineCode 线路编号
     * @return 列车列表
     */
    List<RegionResDTO> listTrainRegion(String lineCode);

    List<RegionResDTO> listLine();

    List<RegionResDTO> listRegion(String lineCode, String regionCode, String recId);

    List<RegionResDTO> listCarRegion(String lineCode, String recId);

    List<EquipmentCategoryResDTO> listEquipmentCategory(String equipmentCategoryCode, String lineCode, String recId, String regionCode);

    /**
     * 获取设备台账列表
     * @param page 分页参数
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
     * @param majors 专业列表
     * @return 设备台账列表
     */
    Page<EquipmentResDTO> pageEquipment(Page<EquipmentResDTO> page, String equipCode, String equipName,
                                        String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime,
                                        String endTime, String manufacture, List<String> majors);

    Page<EquipmentResDTO> pageEquipmentByRoom(Page<EquipmentResDTO> page,String roomId, String equipCode, String equipName, String majorCode, String systemCode);

    List<EquipmentResDTO> allList(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                        String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture);

    EquipmentResDTO getEquipmentDetail(String id);

    /**
     * 根据设备编号获取设备详情
     * @param code 设备编号
     * @return 设备详情
     */
    EquipmentResDTO getEquipmentDetailByCode(String code);

    /**
     * 获取当前最大的设备编号
     * @return 设备编号
     */
    String getMaxCode();

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
     * @param ids ids
     * @param userId 用户id
     * @param time 时间
     */
    void deleteEquipment(List<String> ids, String userId, String time);

    void importEquipment(List<EquipmentReqDTO> list);

    void insertEquipment(EquipmentReqDTO equipmentReqDTO);

    /**
     * 根据id获取设备台账列表
     * @param reqDTO 导出传参
     * @return 设备台账列表
     */
    List<EquipmentResDTO> listEquipment(EquipmentExportReqDTO reqDTO);

    Page<EquipmentResDTO> pageSiftEquipment(Page<EquipmentResDTO> page, EquipmentSiftReqDTO req);

    List<EquipmentResDTO> siftEquipment(EquipmentSiftReqDTO equipmentSiftReqDTO);

    EquipmentResDTO getSplitTransferDetail(String id);

    void updateEquipment(EquipmentReqDTO equipmentReqDTO);

    List<EquipmentResDTO> selectByEquipName(String equipName);

    List<EquipmentResDTO> queryMajor(String equipName);

    Page<OverhaulOrderDetailResDTO> listOverhaul(Page<OverhaulOrderDetailResDTO> page, String equipCode);

    Page<FaultDetailResDTO> listFault(Page<FaultDetailResDTO> page, String equipCode);

    LineResDTO queryLine(String recId);

    LineResDTO queryCarLine(String lineCode);

    Page<PartReplaceResDTO> listPartReplace(Page<PartReplaceResDTO> page, String equipCode);

    /**
     * 根据设备名称获取设备信息
     * @param name 设备名称
     * @return 设备信息
     */
    EquipmentResDTO getEquipByName(String name);
}
