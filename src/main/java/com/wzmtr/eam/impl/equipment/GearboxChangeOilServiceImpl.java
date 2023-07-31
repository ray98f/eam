package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GearboxChangeOilMapper;
import com.wzmtr.eam.service.equipment.GearboxChangeOilService;
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
public class GearboxChangeOilServiceImpl implements GearboxChangeOilService {

    @Autowired
    private GearboxChangeOilMapper gearboxChangeOilMapper;

    @Override
    public Page<GearboxChangeOilResDTO> pageGearboxChangeOil(String trainNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return gearboxChangeOilMapper.pageGearboxChangeOil(pageReqDTO.of(), trainNo);
    }

    @Override
    public GearboxChangeOilResDTO getGearboxChangeOilDetail(String id) {
        return gearboxChangeOilMapper.getGearboxChangeOilDetail(id);
    }

    @Override
    public void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO) {
        gearboxChangeOilReqDTO.setRecId(TokenUtil.getUuId());
        gearboxChangeOilReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        gearboxChangeOilReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        gearboxChangeOilMapper.addGearboxChangeOil(gearboxChangeOilReqDTO);
    }

    @Override
    public void deleteGearboxChangeOil(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!gearboxChangeOilMapper.getGearboxChangeOilDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            gearboxChangeOilMapper.deleteGearboxChangeOil(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGearboxChangeOil(MultipartFile file) {
        // todo
    }

    @Override
    public void exportGearboxChangeOil(String trainNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "列车号", "完成日期", "作业单位", "作业人员", "确认人员", "备注", "附件编号", "创建者", "创建时间");
        List<GearboxChangeOilResDTO> gearboxChangeOilResDTOList = gearboxChangeOilMapper.listGearboxChangeOil(trainNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (gearboxChangeOilResDTOList != null && !gearboxChangeOilResDTOList.isEmpty()) {
            for (GearboxChangeOilResDTO resDTO : gearboxChangeOilResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("列车号", resDTO.getTrainNo());
                map.put("完成日期", resDTO.getCompleteDate());
                map.put("作业单位", resDTO.getOrgType());
                map.put("作业人员", resDTO.getOperator());
                map.put("确认人员", resDTO.getConfirmor());
                map.put("备注", resDTO.getRemark());
                map.put("附件编号", resDTO.getDocId());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("齿轮箱换油台账信息", listName, list, null, response);
    }

}
