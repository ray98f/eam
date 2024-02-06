package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelWheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelWheelsetLathingResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.WheelsetLathingMapper;
import com.wzmtr.eam.service.equipment.WheelsetLathingService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class WheelsetLathingServiceImpl implements WheelsetLathingService {

    @Autowired
    private WheelsetLathingMapper wheelsetLathingMapper;

    @Override
    public Page<WheelsetLathingResDTO> pageWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return wheelsetLathingMapper.pageWheelsetLathing(pageReqDTO.of(), trainNo, carriageNo, axleNo, wheelNo);
    }

    @Override
    public WheelsetLathingResDTO getWheelsetLathingDetail(String id) {
        return wheelsetLathingMapper.getWheelsetLathingDetail(id);
    }

    @Override
    public void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO) {
        wheelsetLathingReqDTO.setRecId(TokenUtil.getUuId());
        wheelsetLathingReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        wheelsetLathingReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        wheelsetLathingMapper.addWheelsetLathing(wheelsetLathingReqDTO);
    }

    @Override
    public void deleteWheelsetLathing(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                if (!wheelsetLathingMapper.getWheelsetLathingDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            wheelsetLathingMapper.deleteWheelsetLathing(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importWheelsetLathing(MultipartFile file) {
        List<ExcelWheelsetLathingReqDTO> list = EasyExcelUtils.read(file, ExcelWheelsetLathingReqDTO.class);
        List<WheelsetLathingReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (ExcelWheelsetLathingReqDTO reqDTO : list) {
                WheelsetLathingReqDTO req = new WheelsetLathingReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                if (StringUtils.isNotEmpty(reqDTO.getAxleNo())) {
                    req.setAxleNo("一轴".equals(reqDTO.getAxleNo()) ? "01" : "二轴".equals(reqDTO.getAxleNo()) ? "02" : "三轴".equals(reqDTO.getAxleNo()) ? "03" : "04");
                } else {
                    req.setAxleNo(reqDTO.getAxleNo());
                }
                req.setRecId(TokenUtil.getUuId());
                req.setDeleteFlag("0");
                req.setRecCreator(TokenUtil.getCurrentPersonId());
                req.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(req);
            }
        }
        if (StringUtils.isNotEmpty(temp)) {
            wheelsetLathingMapper.importWheelsetLathing(temp);
        }
    }

    @Override
    public void exportWheelsetLathing(List<String> ids, HttpServletResponse response) throws IOException {
        List<WheelsetLathingResDTO> wheelsetLathingResDTOList = wheelsetLathingMapper.exportWheelsetLathing(ids);
        if (wheelsetLathingResDTOList != null && !wheelsetLathingResDTOList.isEmpty()) {
            List<ExcelWheelsetLathingResDTO> list = new ArrayList<>();
            for (WheelsetLathingResDTO resDTO : wheelsetLathingResDTOList) {
                ExcelWheelsetLathingResDTO res = new ExcelWheelsetLathingResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setAxleNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getAxleNo()) ? "一轴" : CommonConstants.LINE_CODE_TWO.equals(resDTO.getAxleNo()) ? "二轴" : "03".equals(resDTO.getAxleNo()) ? "三轴" : "四轴");
                list.add(res);
            }
            EasyExcelUtils.export(response, "轮对镟修台账信息", list);
        }
    }

}
