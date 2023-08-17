package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.MeaListReqDTO;
import com.wzmtr.eam.dto.req.MeaReqDTO;
import com.wzmtr.eam.dto.res.MeaResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface MeaMapper {

    Page<MeaResDTO> pageMea(Page<MeaResDTO> page, MeaListReqDTO req);

    MeaResDTO getMeaDetail(String id);

    void addMea(List<MeaReqDTO> list);

    List<MeaResDTO> listMea(MeaListReqDTO meaListReqDTO);
}
