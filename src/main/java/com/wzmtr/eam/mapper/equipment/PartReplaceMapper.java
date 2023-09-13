package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PartReplaceMapper {

    Page<PartReplaceResDTO> pagePartReplace(Page<PartReplaceResDTO> page, String equipName, String replacementName, String faultWorkNo, String orgType, String replaceReason);

    PartReplaceResDTO getPartReplaceDetail(String id);

    String selectBomCode(String equipCode);

    List<PartReplaceBomResDTO> getBom(String node);

    void addPartReplace(PartReplaceReqDTO equipmentChargeReqDTO);

    void deletePartReplace(List<String> ids, String userId, String time);

    void importPartReplace(List<PartReplaceReqDTO> list);

    List<PartReplaceResDTO> listPartReplace(String equipName, String replacementName, String faultWorkNo, String orgType, String replaceReason);

}
