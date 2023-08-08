package com.wzmtr.eam.impl.SpareAndCarVideo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.spareAndCarVideo.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.SpareAndCarVideo.CarVideoMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.carVideoCall.CarVideoService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 10:56
 */
@Service
@Slf4j
public class CarVideoCallServiceImpl implements CarVideoService {
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    CarVideoMapper carVideoMapper;
    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public Page<CarVideoResDTO> list(CarVideoReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<CarVideoResDTO> list = carVideoMapper.query(reqDTO.of(), reqDTO.getRecId(), reqDTO.getStartApplyTime(), reqDTO.getEndApplyTime(), reqDTO.getRecStatus());
        if (CollectionUtil.isNotEmpty(list.getRecords())) {
            List<CarVideoResDTO> records = list.getRecords();
            records.forEach(a -> a.setApplyDeptCode(organizationMapper.getOrgById(a.getApplyDeptCode())));
            return list;
        }
        return new Page<>();
    }

    @Override
    public CarVideoResDTO detail(SidEntity reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getId())) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        CarVideoResDTO detail = carVideoMapper.detail(reqDTO.getId());
        if (detail == null) {
            return null;
        }
        detail.setApplyDeptCode(organizationMapper.getOrgById(detail.getApplyDeptCode()));
        return detail;
    }

    @Override
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isNotEmpty(reqDTO.getIds())) {
            carVideoMapper.deleteByIds(reqDTO.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CarVideoAddReqDTO reqDTO) {
        if (__DateUtil.dateCompare(reqDTO.getVideoEndTime(), reqDTO.getVideoStartTime(), "yyyy-MM-dd HH:mm:ss") != 1) {
            log.error("视频截止时间必须大于视频开始时间!");
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPerson().getPersonName());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        reqDTO.setArchiveFlag("0");
        reqDTO.setRecStatus("10");
        if (StringUtils.isNotEmpty(reqDTO.getTrainNo())) {
            reqDTO.setEquipCode(equipmentMapper.selectByEquipName(reqDTO.getTrainNo()).get(0).getEquipCode());
        }
        reqDTO.setDeleteFlag("0");
        reqDTO.setCompanyName(" ");
        carVideoMapper.add(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CarVideoAddReqDTO reqDTO) {
        if (StringUtils.isEmpty(reqDTO.getRecId())) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        CarVideoResDTO res = carVideoMapper.detail(reqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        assert !Objects.equals(res.getRecStatus(), "10") : "非编辑状态不可修改";
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        carVideoMapper.update(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operate(CarVideoOperateReqDTO reqDTO) {
        // todo
    }

    @Override
    public void export(String recId, HttpServletResponse response) {
        //TODO
    }
}
