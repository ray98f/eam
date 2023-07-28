package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.PartReplaceResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PartReplaceMapper {

    Page<PartReplaceResDTO> pagePartReplace(Page<PartReplaceResDTO> page, String equipName);

    PartReplaceResDTO getPartReplaceDetail(String id);

    void addPartReplace(PartReplaceReqDTO equipmentChargeReqDTO);

    void deletePartReplace(List<String> ids, String userId, String time);

    List<PartReplaceResDTO> listPartReplace(String equipName);

}
