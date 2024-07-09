package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.fault.FaultPartReplaceOpenResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PartReplaceMapper {

    Page<PartReplaceResDTO> pagePartReplace(Page<PartReplaceResDTO> page, String equipName, String replacementName, String faultWorkNo, String orgType, String replaceReason,String workOrderType);

    /**
     * 开放接口获取故障中更换的部件列表
     * @param faultWorkNo 故障工单号
     * @return 部件列表
     */
    List<FaultPartReplaceOpenResDTO> listOpenPartReplace(String faultWorkNo);

    PartReplaceResDTO getPartReplaceDetail(String id);

    String getEquipLineNo(String equipCode);

    String selectBomCode(String equipCode);

    List<PartReplaceBomResDTO> getBom(String node, String equipCode);

    void addPartReplace(PartReplaceReqDTO equipmentChargeReqDTO);

    void deletePartReplace(List<String> ids, String userId, String time);

    void importPartReplace(List<PartReplaceReqDTO> list);

    List<PartReplaceResDTO> listPartReplace(List<String> ids);

}
