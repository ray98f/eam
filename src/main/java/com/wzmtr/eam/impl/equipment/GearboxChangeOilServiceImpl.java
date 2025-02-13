package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelGearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelGearboxChangeOilResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GearboxChangeOilMapper;
import com.wzmtr.eam.service.equipment.GearboxChangeOilService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return gearboxChangeOilMapper.pageGearboxChangeOil(pageReqDTO.of(), trainNo);
    }

    @Override
    public GearboxChangeOilResDTO getGearboxChangeOilDetail(String id) {
        return gearboxChangeOilMapper.getGearboxChangeOilDetail(id);
    }

    @Override
    public void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO) {
        gearboxChangeOilReqDTO.setRecId(TokenUtils.getUuId());
        gearboxChangeOilReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        gearboxChangeOilReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        gearboxChangeOilMapper.addGearboxChangeOil(gearboxChangeOilReqDTO);
    }

    @Override
    public void deleteGearboxChangeOil(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                if (!gearboxChangeOilMapper.getRecCreator(id).equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            gearboxChangeOilMapper.deleteGearboxChangeOil(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGearboxChangeOil(MultipartFile file) {
        List<ExcelGearboxChangeOilReqDTO> list = EasyExcelUtils.read(file, ExcelGearboxChangeOilReqDTO.class);
        List<GearboxChangeOilReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (ExcelGearboxChangeOilReqDTO reqDTO : list) {
                GearboxChangeOilReqDTO req = new GearboxChangeOilReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setOrgType(Objects.isNull(reqDTO.getOrgType()) ? "" : "检修工班".equals(reqDTO.getOrgType()) ? "10" : "20");
                req.setRecId(TokenUtils.getUuId());
                req.setDeleteFlag("0");
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                req.setTotalMiles(new BigDecimal(reqDTO.getTotalMiles()));
                temp.add(req);
            }
        }
        if (!temp.isEmpty()) {
            gearboxChangeOilMapper.importGearboxChangeOil(temp);
        }
    }

    @Override
    public void exportGearboxChangeOil(List<String> ids, HttpServletResponse response) throws IOException {
        List<GearboxChangeOilResDTO> gearboxChangeOilResDTOList = gearboxChangeOilMapper.exportGearboxChangeOil(ids);
        if (gearboxChangeOilResDTOList != null && !gearboxChangeOilResDTOList.isEmpty()) {
            List<ExcelGearboxChangeOilResDTO> list = new ArrayList<>();
            for (GearboxChangeOilResDTO resDTO : gearboxChangeOilResDTOList) {
                ExcelGearboxChangeOilResDTO res = new ExcelGearboxChangeOilResDTO();
                BeanUtils.copyProperties(resDTO, res);
                if (!Objects.isNull(resDTO.getTotalMiles())) {
                    res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                }
                res.setOrgType(CommonConstants.TEN_STRING.equals(resDTO.getOrgType()) ? "检修工班" : "售后服务站");
                list.add(res);
            }
            EasyExcelUtils.export(response, "齿轮箱换油台账信息", list);
        }
    }

}
