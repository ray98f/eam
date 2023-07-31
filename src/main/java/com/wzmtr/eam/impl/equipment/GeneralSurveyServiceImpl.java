package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GeneralSurveyMapper;
import com.wzmtr.eam.service.equipment.GeneralSurveyService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class GeneralSurveyServiceImpl implements GeneralSurveyService {

    @Autowired
    private GeneralSurveyMapper generalSurveyMapper;

    @Override
    public Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return generalSurveyMapper.pageGeneralSurvey(pageReqDTO.of(), trainNo);
    }

    @Override
    public GeneralSurveyResDTO getGeneralSurveyDetail(String id) {
        return generalSurveyMapper.getGeneralSurveyDetail(id);
    }

    @Override
    public void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyReqDTO.setRecId(TokenUtil.getUuId());
        generalSurveyReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        generalSurveyReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        generalSurveyMapper.addGeneralSurvey(generalSurveyReqDTO);
    }

    @Override
    public void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!generalSurveyMapper.getGeneralSurveyDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            generalSurveyMapper.deleteGeneralSurvey(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGeneralSurvey(MultipartFile file) {
        // todo
    }

    @Override
    public void exportGeneralSurvey(String trainNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "列车号", "类别", "技术通知单编号", "项目内容", "完成时间", "作业单位", "备注", "附件编号", "创建者", "创建时间");
        List<GeneralSurveyResDTO> generalSurveyResDTOList = generalSurveyMapper.listGeneralSurvey(trainNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (generalSurveyResDTOList != null && !generalSurveyResDTOList.isEmpty()) {
            for (GeneralSurveyResDTO resDTO : generalSurveyResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("列车号", resDTO.getTrainNo());
                map.put("类别", resDTO.getRecType());
                map.put("技术通知单编号", resDTO.getRecNotifyNo());
                map.put("项目内容", resDTO.getRecDetail());
                map.put("完成时间", resDTO.getCompleteDate());
                map.put("作业单位", resDTO.getOrgType());
                map.put("备注", resDTO.getRemark());
                map.put("附件编号", resDTO.getDocId());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("普查与技改台账信息", listName, list, null, response);
    }

}
