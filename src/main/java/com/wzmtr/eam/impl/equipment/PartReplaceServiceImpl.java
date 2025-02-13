package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelPartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelPartReplaceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.PartReplaceMapper;
import com.wzmtr.eam.service.equipment.PartReplaceService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class PartReplaceServiceImpl implements PartReplaceService {

    @Autowired
    private PartReplaceMapper partReplaceMapper;

    @Override
    public Page<PartReplaceResDTO> pagePartReplace(String equipName, String replacementName,
                                                   String faultWorkNo, String orgType, String replaceReason,
                                                   String workOrderType, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return partReplaceMapper.pagePartReplace(pageReqDTO.of(), equipName, replacementName,
                faultWorkNo, orgType, replaceReason, workOrderType);
    }

    @Override
    public PartReplaceResDTO getPartReplaceDetail(String id) {
        return partReplaceMapper.getPartReplaceDetail(id);
    }

    @Override
    public List<PartReplaceBomResDTO> getBom(String equipCode, String node) {
        if (StringUtils.isNotEmpty(equipCode) && StringUtils.isEmpty(node)) {
            node = partReplaceMapper.selectBomCode(equipCode);
        }
        if (StringUtils.isEmpty(node)) {
            return null;
        }
        return partReplaceMapper.getBom(node, equipCode);
    }

    @Override
    public void addPartReplace(PartReplaceReqDTO partReplaceReqDTO) {
        //默认为 1故障工单，2为检修工单
        if(StringUtils.isNotEmpty(partReplaceReqDTO.getFaultWorkNo())){
            if(StringUtils.isEmpty(partReplaceReqDTO.getWorkOrderType())){
                partReplaceReqDTO.setWorkOrderType(CommonConstants.ONE_STRING);
            }else{
                partReplaceReqDTO.setWorkOrderType(CommonConstants.TWO_STRING);
            }
        }else{
            partReplaceReqDTO.setWorkOrderType(CommonConstants.THREE_STRING);
        }
        partReplaceReqDTO.setRecId(TokenUtils.getUuId());
        partReplaceReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        partReplaceReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        partReplaceMapper.addPartReplace(partReplaceReqDTO);
    }

    @Override
    public void deletePartReplace(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                if (!partReplaceMapper.getPartReplaceDetail(id).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            partReplaceMapper.deletePartReplace(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importPartReplace(MultipartFile file) {
        List<ExcelPartReplaceReqDTO> list = EasyExcelUtils.read(file, ExcelPartReplaceReqDTO.class);
        List<PartReplaceReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (ExcelPartReplaceReqDTO reqDTO : list) {
                PartReplaceReqDTO req = new PartReplaceReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setOrgType(Objects.isNull(reqDTO.getOrgType()) ? "" : "检修工班".equals(reqDTO.getOrgType()) ? "10" : "20");
                req.setRecId(TokenUtils.getUuId());
                req.setDeleteFlag("0");
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                temp.add(req);
            }
        }
        if (StringUtils.isNotEmpty(temp)) {
            partReplaceMapper.importPartReplace(temp);
        }
    }

    @Override
    public void exportPartReplace(List<String> ids, HttpServletResponse response) throws IOException {
        List<PartReplaceResDTO> partReplaceResDTOList = partReplaceMapper.listPartReplace(ids);
        if (partReplaceResDTOList != null && !partReplaceResDTOList.isEmpty()) {
            List<ExcelPartReplaceResDTO> list = new ArrayList<>();
            for (PartReplaceResDTO resDTO : partReplaceResDTOList) {
                ExcelPartReplaceResDTO res = new ExcelPartReplaceResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setOrgType(CommonConstants.TEN_STRING.equals(resDTO.getOrgType()) ? "检修工班" : "售后服务站");
                list.add(res);
            }
            EasyExcelUtils.export(response, "部件更换台账信息", list);
        }
    }

}
