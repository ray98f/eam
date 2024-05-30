package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
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

    MeaResDTO getMeaDetail(String id, String equipCode);

    void importMea(List<MeaReqDTO> list);

    /**
     * 新增计量器具
     * @param meaReqDTO 计量器具参数
     */
    void addMea(MeaReqDTO meaReqDTO);

    List<MeaResDTO> listMea(List<String> ids);

    void updateone(MeaResDTO meaResDTO);

    Page<SubmissionRecordDetailResDTO> pageMeaRecord(Page<SubmissionRecordDetailResDTO> page, String equipCode);
}
