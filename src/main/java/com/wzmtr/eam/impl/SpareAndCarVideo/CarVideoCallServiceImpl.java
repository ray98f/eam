package com.wzmtr.eam.impl.SpareAndCarVideo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dataobject.CarVideoDO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoAddReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoExportReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoOperateReqDTO;
import com.wzmtr.eam.dto.req.spareAndCarVideo.CarVideoReqDTO;
import com.wzmtr.eam.dto.res.spareAndCarVideo.CarVideoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.SpareAndCarVideo.CarVideoMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.carVideoCall.CarVideoService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
                a.setApplyDeptName(organizationMapper.getNamesById(a.getApplyDeptCode()));
            }); return list;
        } return new Page<>();
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
        detail.setApplyDeptName(organizationMapper.getOrgById(detail.getApplyDeptCode()));
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
        if (DateUtil.dateCompare(reqDTO.getVideoEndTime(), reqDTO.getVideoStartTime(), "yyyy-MM-dd HH:mm:ss") != 1) {
            log.error("视频截止时间必须大于视频开始时间!");
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPerson().getPersonName());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        if (StringUtils.isNotEmpty(reqDTO.getTrainNo())) {
            reqDTO.setEquipCode(equipmentMapper.selectByEquipName(reqDTO.getTrainNo()).get(0).getEquipCode());
        }
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        carVideoMapper.update(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operate(CarVideoOperateReqDTO reqDTO) {
        // 这里的recId传生成的uuid
        CarVideoResDTO detail = carVideoMapper.detail(reqDTO.getRecId());
        if (detail == null) {
            log.error("该记录不存在-{}", reqDTO.getRecId());
            return;
        }
        CarVideoDO carVideoDO = new CarVideoDO();
        if ("20".equals(reqDTO.getRecStatus())) {
            if (!"10".equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "非编辑状态不可下达!");
            }
            if (StringUtils.isBlank(detail.getDispatchUserId())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,检修调度不能为空");
            }
            carVideoDO.setDispatchUserId(reqDTO.getDispatchUserId());
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            overTodoService.insertTodo("视频调阅流转", detail.getRecId(), detail.getApplyNo(), reqDTO.getDispatchUserId(), "视频调阅下达", "DMBR0022", TokenUtil.getCurrentPersonId());
        }
        if ("30".equals(reqDTO.getRecStatus())) {
            if (!"20".equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非下达状态下不可派工");
            }
            overTodoService.overTodo(reqDTO.getRecId(), "");
            String[] split = reqDTO.getWorkerId().split(",");
            for (int i = 0; i < split.length; i++) {
                overTodoService.insertTodo("视屏调阅流转", detail.getRecId(), detail.getApplyNo(), split[i], "视频调阅派工", "DMBR0022", TokenUtil.getCurrentPersonId());
            }
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            carVideoDO.setDispatchTime(DateUtil.getCurrentTime());
            carVideoDO.setWorkerId(reqDTO.getWorkerId());
            carVideoDO.setWorkClass(reqDTO.getWorkClass());
        }
        if ("40".equals(reqDTO.getRecStatus())) {
            if (!"30".equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非派工状态下不可完工");
            }
            overTodoService.overTodo(reqDTO.getRecId(), "");
            overTodoService.insertTodo("视频调阅流转", detail.getRecId(), detail.getRecId(), reqDTO.getDispatchUserId(), "视频调阅完工", "DMBR0022", TokenUtil.getCurrentPersonId());
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
            carVideoDO.setRecCreator(reqDTO.getRecStatus());
            carVideoDO.setWorkTime(DateUtil.getCurrentTime());
        }
        if ("50".equals(reqDTO.getRecStatus())) {
            if (!"40".equals(detail.getRecStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "失败,非完工状态下不可关闭\"");
            }
            overTodoService.overTodo(reqDTO.getRecId(), "");
            carVideoDO.setRecStatus(reqDTO.getRecStatus());
            carVideoDO.setCloseTime(DateUtil.getCurrentTime());
            carVideoDO.setCloserId(TokenUtil.getCurrentPersonId());
        }
        carVideoMapper.operate(carVideoDO);
    }

    @Override
    public void export(CarVideoExportReqDTO reqDTO, HttpServletResponse response) {
        // 2级修180天
        List<CarVideoResDTO> resList = carVideoMapper.list(reqDTO);
        List<String> listName = Arrays.asList("调阅记录号", "车组号", "调阅性质", "申请部门", "视频起始时间", "视频截止时间", "申请调阅原因", "状态");
        List<Map<String, String>> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(resList)) {
            log.error("数据为空，无导出数据");
            return;
        }
        for (CarVideoResDTO res : resList) {
            String applyDeptName = organizationMapper.getOrgById(res.getApplyDeptCode());
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.videoApplyType", res.getApplyType());
            Map<String, String> map = new HashMap<>();
            map.put("调阅记录号", res.getApplyNo());
            map.put("车组号", res.getTrainNo());
            map.put("调阅性质", dictionaries.getItemCname());
            map.put("申请部门", applyDeptName);
            map.put("视频起始时间", res.getVideoStartTime());
            map.put("视频截止时间", res.getVideoEndTime());
            map.put("申请调阅原因", res.getApplyReason());
            map.put("状态", res.getRecStatus());
            list.add(map);
        }
        ExcelPortUtil.excelPort("检调视频调阅", listName, list, null, response);
    }
}