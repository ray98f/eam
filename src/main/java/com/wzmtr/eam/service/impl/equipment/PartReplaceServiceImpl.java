package com.wzmtr.eam.service.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.PartReplaceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.PartReplaceMapper;
import com.wzmtr.eam.service.equipment.PartReplaceService;
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
public class PartReplaceServiceImpl implements PartReplaceService {

    @Autowired
    private PartReplaceMapper partReplaceMapper;

    @Override
    public Page<PartReplaceResDTO> pagePartReplace(String equipName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return partReplaceMapper.pagePartReplace(pageReqDTO.of(), equipName);
    }

    @Override
    public PartReplaceResDTO getPartReplaceDetail(String id) {
        return partReplaceMapper.getPartReplaceDetail(id);
    }

    @Override
    public List<PartReplaceBomResDTO> getBom(String equipCode, String node) {
        if (equipCode != null && !"".equals(equipCode)) {
            node = partReplaceMapper.selectBomCode(equipCode);
            if (Objects.isNull(node) || "".equals(node)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
        }
        return partReplaceMapper.getBom(node);
    }

    @Override
    public void addPartReplace(PartReplaceReqDTO partReplaceReqDTO) {
        partReplaceReqDTO.setRecId(TokenUtil.getUuId());
        partReplaceReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        partReplaceReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        partReplaceMapper.addPartReplace(partReplaceReqDTO);
    }

    @Override
    public void deletePartReplace(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!partReplaceMapper.getPartReplaceDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            partReplaceMapper.deletePartReplace(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importPartReplace(MultipartFile file) {
        // todo
    }

    @Override
    public void exportPartReplace(String equipName, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "故障工单编号", "设备编码", "设备名称", "作业单位", "作业人员", "更换配件代码",
                "更换配件名称", "更换原因", "旧配件编号", "新配件编号", "更换所用时间", "处理日期", "备注", "附件编号", "创建者", "创建时间");
        List<PartReplaceResDTO> partReplaceResDTOList = partReplaceMapper.listPartReplace(equipName);
        List<Map<String, String>> list = new ArrayList<>();
        if (partReplaceResDTOList != null && !partReplaceResDTOList.isEmpty()) {
            for (PartReplaceResDTO resDTO : partReplaceResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("故障工单编号", resDTO.getFaultWorkNo());
                map.put("设备编码", resDTO.getEquipCode());
                map.put("设备名称", resDTO.getEquipName());
                map.put("作业单位", resDTO.getOrgType());
                map.put("作业人员", resDTO.getOperator());
                map.put("更换配件代码", resDTO.getReplacementNo());
                map.put("更换配件名称", resDTO.getReplacementName());
                map.put("更换原因", resDTO.getReplaceReason());
                map.put("旧配件编号", resDTO.getOldRepNo());
                map.put("新配件编号", resDTO.getNewRepNo());
                map.put("更换所用时间", resDTO.getOperateCostTime());
                map.put("处理日期", resDTO.getReplaceDate());
                map.put("备注", resDTO.getRemark());
                map.put("附件编号", resDTO.getDocId());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("部件更换台账信息", listName, list, null, response);
    }

}
