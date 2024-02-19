package com.wzmtr.eam.impl.video;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.export.CarVideoExportBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.CarVideoDO;
import com.wzmtr.eam.dto.req.video.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoExportReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.video.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.video.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.VideoApplyStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.video.CarVideoMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.video.CarVideoService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    OverTodoService overTodoService;
    @Autowired
    DictionariesMapper dictionariesMapper;
    @Override
    public Page<CarVideoResDTO> list(CarVideoReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<CarVideoResDTO> list = carVideoMapper.query(reqDTO.of(), reqDTO.getApplyNo(), reqDTO.getStartApplyTime(), reqDTO.getEndApplyTime(), reqDTO.getRecStatus());
        if (CollectionUtil.isNotEmpty(list.getRecords())) {
            List<CarVideoResDTO> records = list.getRecords();
            records.forEach(a -> {
                if (StringUtils.isNotEmpty(a.getApplyDeptCode())) {
                    a.setApplyDeptName(organizationMapper.getNamesById(a.getApplyDeptCode()));
                }
            });
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
        if (StringUtils.isNotEmpty(detail.getApplyDeptCode())) {
            detail.setApplyDeptName(organizationMapper.getNamesById(detail.getApplyDeptCode()));
        }
        return detail;
    }

    @Override
    public void delete(BaseIdsEntity reqDTO) {
        if (CollectionUtil.isNotEmpty(reqDTO.getIds())) {
            carVideoMapper.deleteByIds(reqDTO.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CarVideoAddReqDTO reqDTO) {
        if (DateUtils.dateCompare(reqDTO.getVideoEndTime(), reqDTO.getVideoStartTime(), CommonConstants.TIME) != 1) {
            log.error("视频截止时间必须大于视频开始时间!");
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        reqDTO.setRecId(TokenUtils.getUuId());
        reqDTO.setRecCreator(TokenUtils.getCurrentPerson().getPersonName());
        reqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        reqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        reqDTO.setArchiveFlag("0");
        reqDTO.setDeleteFlag("0");
        String maxCode = carVideoMapper.selectMaxCode();
        reqDTO.setApplyNo(CodeUtils.getNextCode(maxCode, "VA"));
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
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        if (StringUtils.isNotEmpty(reqDTO.getTrainNo())) {
            reqDTO.setEquipCode(equipmentMapper.selectByEquipName(reqDTO.getTrainNo()).get(0).getEquipCode());
        }
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        carVideoMapper.update(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operate(CarVideoOperateReqDTO reqDTO) {
        Assert.notNull(reqDTO.getRecId(),ErrorCode.PARAM_ERROR);
        // 这里的recId传生成的uuid
        CarVideoResDTO detail = carVideoMapper.detail(reqDTO.getRecId());
        if (detail == null) {
            log.error("该记录不存在-{}", reqDTO.getRecId());
            return;
        }
        CarVideoDO carVideoDO = BeanUtils.convert(detail, CarVideoDO.class);
        // 下达
        if (CommonConstants.TWENTY_STRING.equals(reqDTO.getRecStatus())) {
            if (!CommonConstants.TEN_STRING.equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "非编辑状态不可下达!");
            }
            Assert.notNull(reqDTO.getDispatchUserId(),ErrorCode.NORMAL_ERROR, "失败,检修调度不能为空");
            carVideoDO.setDispatchUserId(reqDTO.getDispatchUserId());
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            overTodoService.insertTodo("视频调阅流转", detail.getRecId(), detail.getApplyNo(), reqDTO.getDispatchUserId(), "视频调阅下达", "DMBR0022", TokenUtils.getCurrentPersonId());
        }
        // 派工
        if (CommonConstants.THIRTY_STRING.equals(reqDTO.getRecStatus())) {
            if (!CommonConstants.TWENTY_STRING.equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非下达状态下不可派工");
            }
            Assert.isTrue(StringUtils.isNotEmpty(reqDTO.getWorkerId()) && StringUtils.isNotEmpty(reqDTO.getWorkClass()), ErrorCode.PARAM_ERROR, "派工人信息不能为空");
            overTodoService.overTodo(reqDTO.getRecId(), "");
            String[] split = reqDTO.getWorkerId().split(",");
            for (int i = 0; i < split.length; i++) {
                overTodoService.insertTodo("视屏调阅流转", detail.getRecId(), detail.getApplyNo(), split[i], "视频调阅派工", "DMBR0022", TokenUtils.getCurrentPersonId());
            }
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            carVideoDO.setDispatchTime(DateUtils.getCurrentTime());
            carVideoDO.setWorkerId(reqDTO.getWorkerId());
            carVideoDO.setWorkClass(reqDTO.getWorkClass());
        }
        // 完工
        if (CommonConstants.FORTY_STRING.equals(reqDTO.getRecStatus())) {
            if (!CommonConstants.THIRTY_STRING.equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非派工状态下不可完工");
            }
            overTodoService.overTodo(reqDTO.getRecId(), "");
            overTodoService.insertTodo("视频调阅流转", detail.getRecId(), detail.getRecId(), reqDTO.getDispatchUserId(), "视频调阅完工", "DMBR0022", TokenUtils.getCurrentPersonId());
            // TODO: 2023/9/14 发短信
            // Map<Object, Object> User = new HashMap<>();
            // User.put("loginName", (detail.getDispatchUserId()));
            // List phones = this.dao.query("DMDM59.queryPhoneByUser", User);
            // if (phones != null && phones.size() > 0) {
            //     String content = "视频调阅已完工，请注意";
            //     EiInfo eiInfo = new EiInfo();
            //     eiInfo.set("contacts", phones);
            //     eiInfo.set("content", content);
            //     ISendMessage.sendMessageByPhoneList(eiInfo);
            // }
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            carVideoDO.setRecCreator(TokenUtils.getCurrentPersonId());
            carVideoDO.setWorkTime(DateUtils.getCurrentTime());
        }
        // 关闭
        if (CommonConstants.FIFTY_STRING.equals(reqDTO.getRecStatus())) {
            if (!CommonConstants.FORTY_STRING.equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非完工状态下不可关闭");
            }
            overTodoService.overTodo(reqDTO.getRecId(), "");
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            carVideoDO.setCloseTime(DateUtils.getCurrentTime());
            carVideoDO.setCloserId(TokenUtils.getCurrentPersonId());
        }
        carVideoMapper.operate(carVideoDO);
    }

    @Override
    public void export(CarVideoExportReqDTO reqDTO, HttpServletResponse response) {
        // 2级修180天
        List<CarVideoResDTO> resList = carVideoMapper.list(reqDTO);
        List<CarVideoExportBO> exportList = new ArrayList<>();
        if (CollectionUtil.isEmpty(resList)) {
            log.error("数据为空，无导出数据");
            return;
        }
        for (CarVideoResDTO res : resList) {
            CarVideoExportBO exportBO = BeanUtils.convert(res, CarVideoExportBO.class);
            String status = res.getRecStatus();
            VideoApplyStatus recStatus = VideoApplyStatus.getByCode(status);
            String applyDeptName = " ";
            if (StringUtils.isNotEmpty(res.getApplyDeptCode())) {
                applyDeptName = organizationMapper.getNamesById(res.getApplyDeptCode());
            }
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.videoApplyType", res.getApplyType());
            exportBO.setApplyDeptName(applyDeptName);
            exportBO.setRecStatus(recStatus != null ? recStatus.getDesc() : status);
            exportBO.setApplyType(dictionaries.getItemCname());
            exportList.add(exportBO);
        }
        try {
            EasyExcelUtils.export(response, "检调视频调阅", exportList);
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR);
        }
    }
}