package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.basic.query.RegionQuery;
import com.wzmtr.eam.dto.req.fault.FaultCancelReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFlowReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportOpenReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportOpenResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.PersonMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.shiro.model.Person;
import com.wzmtr.eam.utils.Assert;
import com.wzmtr.eam.utils.BeanUtils;
import com.wzmtr.eam.utils.ChineseCharacterUtil;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StreamUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import com.wzmtr.eam.utils.mq.FaultSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
@Slf4j
public class FaultReportServiceImpl implements FaultReportService {

    @Value("${spring.redis.key-prefix}")
    private String keyPrefix;
    @Resource
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private FaultReportMapper faultReportMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private TrackQueryService trackQueryService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private OrgMajorMapper orgMajorMapper;
    @Autowired
    FaultSender faultSender;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private FaultQueryServiceImpl faultQueryServiceImpl;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addToFault(FaultReportReqDTO reqDTO) {
//        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
//        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        FaultInfoDO faultInfo = reqDTO.toFaultInfoInsertDO(reqDTO);
//        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        String nextFaultNo = CodeUtils.generateFaultNo();
        insertToFaultInfo(faultInfo, nextFaultNo);
        FaultOrderDO faultOrder = reqDTO.toFaultOrderInsertDO(reqDTO);
//        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        String nextFaultWorkNo = CodeUtils.generateFaultWorkNo();
        insertToFaultOrder(faultOrder, nextFaultNo, nextFaultWorkNo);
        // 添加流程记录
        addFaultFlow(nextFaultNo, nextFaultWorkNo);
        String majorCode = reqDTO.getMajorCode();
        // 中铁通 且不是行车调度的故障类型 直接变更为已派工状态 并给该工班下的人发待办
        if (!CommonConstants.ZC_LIST.contains(majorCode)) {
            if (!CommonConstants.TEN_STRING.equals(reqDTO.getFaultType())) {
                String positionCode = reqDTO.getPositionCode();
                if (StringUtils.isNotEmpty(positionCode) && StringUtils.isNotEmpty(majorCode)) {
                    if (StringUtils.isEmpty(faultInfo.getRepairDeptCode())) {
                        // 专业和位置查维修部门
                        OrgMajorResDTO organ = orgMajorMapper.getOrganByStationAndMajor(positionCode, majorCode);
                        if (StringUtils.isNotNull(organ)) {
                            faultInfo.setRepairDeptCode(organ.getOrgCode());
                            // 负责人为中铁通工班长角色
                            Person person = personMapper.searchLeaderByMajorAndPositionAndRole(majorCode, positionCode, CommonConstants.DM_051);
                            if (StringUtils.isNotNull(person)) {
                                faultOrder.setRepairRespUserId(person.getLoginName());
                            }
                        }
                    } else {
                        Person person = personMapper.searchLeaderByDeptAndRole(faultInfo.getRepairDeptCode(), CommonConstants.DM_051);
                        if (StringUtils.isNotNull(person)) {
                            faultOrder.setRepairRespUserId(person.getLoginName());
                        }
                    }
                    // 默认为紧急
                    faultInfo.setExt1(CommonConstants.ZERO_ONE_STRING);
                    faultInfo.setRecRevisor(TokenUtils.getCurrentPersonId());
                    faultInfo.setRecReviseTime(DateUtils.getCurrentTime());
                    faultOrder.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                    faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
                    faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
                    faultReportMapper.updateFaultOrder(faultOrder);
                    faultReportMapper.updateFaultInfo(faultInfo);
                    // 故障维修待办推送
                    String newId = organizationMapper.getIdByAreaId(faultInfo.getRepairDeptCode());
                    List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(newId, null);
                    if (CollectionUtil.isNotEmpty(userList)) {
                        for (BpmnExaminePersonRes map2 : userList) {
                            overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"),
                                    faultOrder.getRecId(), nextFaultWorkNo, map2.getUserId(), "故障维修", "RepairFaultAdd",
                                    TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                        }
                    }
                }
            } else {
                // 行车调度的故障类型 且中铁通的给生产调度发待办
                toZttTrainDispatch(reqDTO, faultOrder, nextFaultWorkNo);
            }
        } else {
            // 中车给中车检调
            toZcProsecute(reqDTO, faultOrder, nextFaultWorkNo);
        }
        // 知会OCC调度
        toOcc(reqDTO, nextFaultWorkNo);
        return nextFaultNo;
    }

    /**
     * 知会OCC调度
     * @param reqDTO 故障传参
     * @param faultWorkNo 故障工单编号
     */
    private void toOcc(FaultReportReqDTO reqDTO, String faultWorkNo) {
        if (StringUtils.isNotNull(reqDTO.getMaintenance()) && reqDTO.getMaintenance()) {
            List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(null, CommonConstants.DM_052);
            for (BpmnExaminePersonRes map2 : userList) {
                if (CollectionUtil.isNotEmpty(userList)) {
                    overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_OCC, faultWorkNo, "知会OCC调度的故障"),
                            TokenUtils.getUuId(), faultWorkNo, map2.getUserId(), "知会OCC调度", "OCC",
                            TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_READ_QUERY.value());
                }
            }
        }
    }

    private void toZcProsecute(FaultReportReqDTO reqDTO, FaultOrderDO faultOrder, String nextFaultWorkNo) {
        faultOrder.setOrderStatus(OrderStatus.TI_BAO.getCode());
        faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrder);
        List<String> users = faultQueryServiceImpl.getUsersByCompanyAndRole(reqDTO.getMajorCode(), null, "ZCJD");
        if (CollectionUtil.isNotEmpty(users)) {
            overTodoService.insertTodoWithUserList(users, String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"),
                    faultOrder.getRecId(), nextFaultWorkNo, "故障已下发", "faultSendZc",
                    TokenUtils.getCurrentPersonId(), null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    private void toZttTrainDispatch(FaultReportReqDTO reqDTO, FaultOrderDO faultOrder, String nextFaultWorkNo) {
        faultOrder.setOrderStatus(OrderStatus.TI_BAO.getCode());
        faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrder);
        List<String> users = faultQueryServiceImpl.getUsersByCompanyAndRole(reqDTO.getMajorCode(), CommonConstants.DM_007, null);
        if (CollectionUtil.isNotEmpty(users)) {
            overTodoService.insertTodoWithUserList(users, String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"),
                    faultOrder.getRecId(), nextFaultWorkNo, "故障已下发", "faultSendZtt",
                    TokenUtils.getCurrentPersonId(), null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeReport(FaultReportReqDTO reqDTO) {
        // 获取AOP代理对象
        FaultInfoDO faultInfo = reqDTO.toFaultInfoInsertDO(reqDTO);
        //更新故障信息
        modifyToFaultInfo(faultInfo);
        FaultOrderDO faultOrder = reqDTO.toFaultOrderChangeDO(reqDTO);
        faultOrder.setRecRevisor(TokenUtils.getCurrentPerson().getPersonId());
        faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrder);
        // 添加流程记录
        // 中铁通 且是行车调度的故障类型 直接变更为已派工状态 并给该工班下的人发待办
        addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        String majorCode = reqDTO.getMajorCode();
        if (!CommonConstants.ZC_LIST.contains(majorCode)) {
            if (!CommonConstants.TEN_STRING.equals(reqDTO.getFaultType())) {
                String positionCode = reqDTO.getPositionCode();
                if (StringUtils.isNotEmpty(positionCode) && StringUtils.isNotEmpty(majorCode)) {
                    if (StringUtils.isEmpty(faultInfo.getRepairDeptCode())) {
                        // 专业和位置查维修部门
                        OrgMajorResDTO organ = orgMajorMapper.getOrganByStationAndMajor(positionCode, majorCode);
                        if (StringUtils.isNotNull(organ)) {
                            faultInfo.setRepairDeptCode(organ.getOrgCode());
                            // 负责人为中铁通工班长角色
                            Person person = personMapper.searchLeaderByMajorAndPositionAndRole(majorCode, positionCode, CommonConstants.DM_051);
                            if (StringUtils.isNotNull(person)) {
                                faultOrder.setRepairRespUserId(person.getLoginName());
                            }
                        }
                    } else {
                        Person person = personMapper.searchLeaderByDeptAndRole(faultInfo.getRepairDeptCode(), CommonConstants.DM_051);
                        if (StringUtils.isNotNull(person)) {
                            faultOrder.setRepairRespUserId(person.getLoginName());
                        }
                    }
                    // 默认为紧急
                    faultInfo.setExt1(CommonConstants.ZERO_ONE_STRING);
                    faultOrder.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                    faultInfo.setRecRevisor(TokenUtils.getCurrentPersonId());
                    faultInfo.setRecReviseTime(DateUtils.getCurrentTime());
                    faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
                    faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
                    faultReportMapper.updateFaultOrder(faultOrder);
                    faultReportMapper.updateFaultInfo(faultInfo);
                    // 故障维修待办推送
                    String newId = organizationMapper.getIdByAreaId(faultInfo.getRepairDeptCode());
                    List<BpmnExaminePersonRes> userList = roleMapper.getUserByOrgAndRole(newId, null);
                    if (CollectionUtil.isNotEmpty(userList)) {
                        for (BpmnExaminePersonRes map2 : userList) {
                            overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL, reqDTO.getFaultWorkNo(), "故障"),
                                    faultOrder.getRecId(), reqDTO.getFaultWorkNo(), map2.getUserId(), "故障维修",
                                    "RepairFaultChange", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                        }
                    }
                }
            } else {
                // 行车调度的故障类型 且中铁通的给生产调度发待办
                toZttTrainDispatch(reqDTO, faultOrder, reqDTO.getFaultWorkNo());
            }
        } else {
            // 中车给中车检调
            toZcProsecute(reqDTO, faultOrder, reqDTO.getFaultWorkNo());
        }
        // 知会OCC调度
        toOcc(reqDTO, reqDTO.getFaultWorkNo());
    }

    @Override
    public FaultReportOpenResDTO addToFaultOpen(FaultReportOpenReqDTO reqDTO) {
        String authorization = httpServletRequest.getHeader("app-key");
        if (!CommonConstants.FAULT_OPEN_APP_KEY.equals(authorization)) {
            throw new CommonException(ErrorCode.FAULT_OPEN_TOKEN_ERROR);
        }
        if (StringUtils.isEmpty(reqDTO.getSysFaultNo())
                || StringUtils.isEmpty(reqDTO.getEquipCode())
                || StringUtils.isEmpty(reqDTO.getFaultDetail())
                || StringUtils.isEmpty(reqDTO.getSysName())
                || StringUtils.isEmpty(reqDTO.getAlarmTime())
                || StringUtils.isEmpty(reqDTO.getLineCode())
                || StringUtils.isEmpty(reqDTO.getFaultStatus())) {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        String nextFaultNo, nextFaultWorkNo;
        String key = keyPrefix
                + ChineseCharacterUtil.getUpperCase(reqDTO.getSysName(), false)
                + CommonConstants.SHORT_BAR + reqDTO.getSysFaultNo();
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            try {
//            String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
//            String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
//            nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
//            String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
                nextFaultNo = CodeUtils.generateFaultNo();
                nextFaultWorkNo = CodeUtils.generateFaultWorkNo();
                reqDTO.setFaultNo(nextFaultNo);
                reqDTO.setFaultWorkNo(nextFaultWorkNo);
            } catch (Exception e) {
                log.error("open exception message", e);
                throw new CommonException(ErrorCode.FAULT_OPEN_ERROR);
            }
            try {
                // 推送消息至mq
                faultSender.sendFault(reqDTO);
                FaultReportOpenResDTO res = new FaultReportOpenResDTO();
                res.setFaultNo(nextFaultNo);
                res.setFaultWorkNo(nextFaultWorkNo);
                stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(res));
                return res;
            } catch (Exception e) {
                log.error("open exception message", e);
                throw new CommonException(ErrorCode.NORMAL_ERROR, "推送异常！");
            }
        } else {
            return JSON.parseObject(stringRedisTemplate.opsForValue().get(key), FaultReportOpenResDTO.class);
        }
    }

    public void insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
        faultInfoDO.setFaultNo(nextFaultNo);
        faultInfoDO.setRecId(TokenUtils.getUuId());
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtils.getCurrentTime());
        faultInfoDO.setFillinUserId(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setRecCreateTime(DateUtils.getCurrentTime());
        String stationNo = userAccountMapper.selectStationByUser(TokenUtils.getCurrentPerson().getPersonId());
        if (StringUtils.isNotNull(stationNo)) {
            faultInfoDO.setRecStation(stationNo);
        }
        faultReportMapper.addToFaultInfo(faultInfoDO);
    }

    public void insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String nextFaultWorkNo) {
        faultOrderDO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderDO.setFaultNo(nextFaultNo);
        faultOrderDO.setDeleteFlag("0");
        faultOrderDO.setRecId(TokenUtils.getUuId());
        faultOrderDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultOrderDO.setRecCreateTime(DateUtils.getCurrentTime());
        faultReportMapper.addToFaultOrder(faultOrderDO);
    }

    //变更故障信息
    public void modifyToFaultInfo(FaultInfoDO faultInfoDO) {
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtils.getCurrentTime());
        faultInfoDO.setFillinUserId(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateToFaultInfo(faultInfoDO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        SysOffice office = userAccountMapper.getUserOrg(TokenUtils.getCurrentPersonId());
        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(reqDTO.getMajorCode()) &&
                StringUtils.isNotNull(office) && !office.getNames().contains(CommonConstants.PASSENGER_TRANSPORT_DEPT)) {
            userMajorList = userAccountService.listUserMajor();
        }
        Page<FaultReportResDTO> list;
        //获取用户当前角色
        List<UserRoleResDTO> userRoles = userAccountService.getUserRolesById(TokenUtils.getCurrentPersonId());
        // 如果用户的角色中包含中车、中铁通专业工程师，获取状态为完工验收之后的数据
        String type = null;
        if (userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_032))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_006))) {
            type = CommonConstants.ONE_STRING;
        } else if (userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_052))) {
            type = CommonConstants.TWO_STRING;
        }
        String userStation = userAccountMapper.selectStationByUser(TokenUtils.getCurrentPersonId());
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        //admin 中铁通生产调度 中车生产调度可以查看本专业的所有数据外 ，其他的角色根据 提报、派工 、验收阶段人员查看
        if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_007))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_048))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_004))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_005))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_056))) {
            list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(),
                    reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                    reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                    reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(), reqDTO.getFaultWorkNo(),
                    reqDTO.getLineCode(), userMajorList, null, null, null, null);
        } else {
            list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(),
                    reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                    reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                    reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),
                    reqDTO.getFaultWorkNo(), reqDTO.getLineCode(), userMajorList, TokenUtils.getCurrentPersonId(),
                    TokenUtils.getCurrentPerson().getOfficeAreaId(), type, userStation);
        }
        List<FaultReportResDTO> records = list.getRecords();
        buildRes(records);
        list.setRecords(records);
        return list;
    }

    @Override
    public Page<FaultDetailResDTO> openApiList(FaultReportPageReqDTO reqDTO) {
        if (StringUtils.isNotEmpty(reqDTO.getPositionName())) {
            List<RegionResDTO> regionRes = regionMapper.selectByQuery(RegionQuery.builder().nodeName(reqDTO.getPositionName()).build());
            Set<String> nodeCodes = regionRes.stream().map(RegionResDTO::getNodeCode).collect(Collectors.toSet());
            reqDTO.setPositionCodes(nodeCodes);
        }
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> page = faultReportMapper.openApiList(reqDTO.of(), reqDTO);
        List<FaultDetailResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (FaultDetailResDTO res : list) {
                buildFaultDetailRes(res);
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO) {
        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(reqDTO.getMajorCode())) {
            userMajorList = userAccountService.listUserMajor();
        }
        Page<FaultReportResDTO> list;
        //获取用户当前角色
        List<UserRoleResDTO> userRoles = userAccountService.getUserRolesById(TokenUtils.getCurrentPersonId());
        // 如果用户的角色中包含中车、中铁通专业工程师，获取状态为完工验收之后的数据
        String type = null;
        if (userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_032))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_006))) {
            type = CommonConstants.ONE_STRING;
        } else if (userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_052))) {
            type = CommonConstants.TWO_STRING;
        }
        String userStation = userAccountMapper.selectStationByUser(TokenUtils.getCurrentPersonId());
        //admin 中铁通生产调度 中车生产调度可以查看本专业的所有数据外 ，其他的角色根据 提报、派工 、验收阶段人员查看
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_007))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_048))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_004))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_005))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_056))) {
            list = faultReportMapper.carFaultReportList(reqDTO.of(), reqDTO.getFaultNo(),
                    reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                    reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                    reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),
                    reqDTO.getFaultAffect(), userMajorList, null, null, null, null);
        } else {
            list = faultReportMapper.carFaultReportList(reqDTO.of(), reqDTO.getFaultNo(),
                    reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                    reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                    reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),
                    reqDTO.getFaultAffect(), userMajorList, TokenUtils.getCurrentPersonId(),
                    TokenUtils.getCurrentPerson().getOfficeAreaId(), type, userStation);
        }
        List<FaultReportResDTO> records = list.getRecords();
        buildRes(records);
        return list;
    }

    private void buildRes(List<FaultReportResDTO> records) {
        Set<String> positionCodes = StreamUtils.mapToSet(records, FaultReportResDTO::getPositionCode);
        List<RegionResDTO> regionRes = regionMapper.selectByQuery(RegionQuery.builder().nodeCodes(positionCodes).build());
        Map<String, RegionResDTO> regionMap = StreamUtils.toMap(regionRes, RegionResDTO::getNodeCode);
        records.forEach(a -> {
            LineCode line = LineCode.getByCode(a.getLineCode());
//            if (StringUtils.isNotEmpty(a.getDocId())) {
//                a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(CommonConstants.COMMA))));
//            }
            a.setLineName(line == null ? a.getLineCode() : line.getDesc());
            if (regionMap.containsKey(a.getPositionCode())) {
                a.setPositionName(regionMap.get(a.getPositionCode()).getNodeName());
            }
            if (StringUtils.isNotEmpty(a.getRepairDeptCode())) {
                a.setRepairDeptName(organizationMapper.getNamesById(a.getRepairDeptCode()));
            }
            if (StringUtils.isNotEmpty(a.getFillinDeptCode())) {
                a.setFillinDeptName(organizationMapper.getNamesById(a.getFillinDeptCode()));
            }
        });
    }

    private void buildFaultDetailRes(FaultDetailResDTO a) {
        if (StringUtils.isNotEmpty(a.getDocId())) {
            a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(CommonConstants.COMMA))));
        }
        if (StringUtils.isNotEmpty(a.getRepairDeptCode())) {
            a.setRepairDeptName(organizationMapper.getNamesById(a.getRepairDeptCode()));
        }
        if (StringUtils.isNotEmpty(a.getFillinDeptCode())) {
            a.setFillinDeptName(organizationMapper.getNamesById(a.getFillinDeptCode()));
        }
    }


    @Override
    public FaultDetailResDTO detail(FaultDetailReqDTO reqDTO) {
        return trackQueryService.faultDetail(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FaultCancelReqDTO reqDTO) {
        // 已提报故障单撤销 逻辑删 涉及faultinfo和faultorder两张表
        faultReportMapper.cancelOrder(reqDTO);
        faultReportMapper.cancelInfo(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(FaultCancelReqDTO reqDTO) {
        // faultWorkNo的recId
        String faultWorkNo = reqDTO.getFaultWorkNo();
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        // order表作废状态
        faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        String faultNo = reqDTO.getFaultNo();
        // info表更新
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        // 取消待办
        overTodoService.cancelTodo(reqDTO.getOrderRecId());
        // 添加流程记录
        addFaultFlow(faultNo, faultWorkNo);
    }

    @Override
    public void update(FaultReportReqDTO reqDTO) {
        Assert.isNotEmpty(reqDTO.getFaultNo(), "参数缺失[故障编号]不能为空!");
        // 修改已提报故障单  修改时间 修改人， 各属性的值
        FaultInfoDO infoUpdate = BeanUtils.convert(reqDTO, FaultInfoDO.class);
        infoUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
        infoUpdate.setRecReviseTime(DateUtils.getCurrentTime());
        FaultOrderDO orderUpdate = BeanUtils.convert(reqDTO, FaultOrderDO.class);
        orderUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
        orderUpdate.setRecReviseTime(DateUtils.getCurrentTime());
        if (StringUtils.isEmpty(reqDTO.getDocId())) {
            // 前端传的是个空值，特殊处理下
            infoUpdate.setDocId(CommonConstants.BLANK);
            orderUpdate.setDocId(CommonConstants.BLANK);
        }
        if (null != reqDTO.getMaintenance()) {
            infoUpdate.setExt4(reqDTO.getMaintenance().toString());
        }
        if (CommonConstants.ZERO_STRING.equals(orderUpdate.getOrderStatus())) {
            orderUpdate.setOrderStatus("10");
            infoUpdate.setFaultStatus("20");
        }
        faultReportMapper.updateFaultInfo(infoUpdate);
        faultReportMapper.updateFaultOrder(orderUpdate);
    }

    /**
     * 新增工单流程
     * @param faultNo     故障编号
     * @param faultWorkNo 故障工单编号
     */
    public void addFaultFlow(String faultNo, String faultWorkNo) {
        FaultFlowReqDTO faultFlowReqDTO = new FaultFlowReqDTO();
        faultFlowReqDTO.setRecId(TokenUtils.getUuId());
        faultFlowReqDTO.setFaultNo(faultNo);
        faultFlowReqDTO.setFaultWorkNo(faultWorkNo);
        faultFlowReqDTO.setOperateUserId(TokenUtils.getCurrentPersonId());
        faultFlowReqDTO.setOperateUserName(TokenUtils.getCurrentPerson().getPersonName());
        faultFlowReqDTO.setOperateTime(DateUtils.getCurrentTime());
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
        if (!Objects.isNull(faultOrderDO)) {
            faultFlowReqDTO.setOrderStatus(faultOrderDO.getOrderStatus());
        }
        faultReportMapper.addFaultFlow(faultFlowReqDTO);
    }
}

