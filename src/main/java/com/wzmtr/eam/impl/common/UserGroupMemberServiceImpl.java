package com.wzmtr.eam.impl.common;

import com.google.common.collect.Lists;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.DispatchResDTO;
import com.wzmtr.eam.dto.res.common.ZcjxResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import com.wzmtr.eam.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:28
 */
@Service
public class UserGroupMemberServiceImpl implements UserGroupMemberService {

    public static final String OPERATE_PROFESSION_ENGINE = "DM_004";
    @Autowired
    private OrgMajorMapper orgMajorMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCode) {
        // DM_012 是故障跟踪这边写死的 原来的逻辑 我也看不懂
        return orgMajorMapper.getDepartmentUserByGroupName("DM_012", groupCode);
    }

    @Override
    public List<OrganMajorLineType> getDepartmentUserByGroupName(String groupCname, String dptCode) {
        return orgMajorMapper.getDepartmentUserByGroupName(groupCname, dptCode);
    }

    @Override
    public List<BpmnExaminePersonRes> getZcOverhaulPlanExamineUser(String subjectCode, String lineCode) {
        List<BpmnExaminePersonRes> nextUser = new ArrayList<>();
        String roleCode;
        if (CommonConstants.CAR_SUBJECT_CODE.equals(subjectCode)) {
            roleCode = "DM_005";
            nextUser.addAll(roleMapper.getUserBySubjectAndLineAndRole(subjectCode, lineCode, roleCode));
        } else if (CommonConstants.CAR_DEVICE_SUBJECT_CODE.equals(subjectCode)) {
            roleCode = "DM_004";
            nextUser.addAll(roleMapper.getUserByOrgAndRole(roleCode, "D0901"));
        } else {
            roleCode = "DM_004";
            nextUser.addAll(roleMapper.getUserBySubjectAndLineAndRole(subjectCode, lineCode, roleCode));
        }
        if (nextUser.isEmpty()) {
            if (OPERATE_PROFESSION_ENGINE.equals(roleCode)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "专业工程师（运营）角色中没有人员，不能进行送审操作");
            }
            throw new CommonException(ErrorCode.NORMAL_ERROR, "专业工程师（车辆部）角色中没有人员，不能进行送审操作");
        }
        return nextUser;
    }

    @Override
    public List<DispatchResDTO> queryDispatch() {
        List<DispatchResDTO> dispatchList = orgMajorMapper.queryDispatch();
        if (StringUtils.isEmpty(dispatchList)) {
            return Lists.newArrayList();
        }
        return dispatchList;
    }

    @Override
    public List<ZcjxResDTO> queryJXWorker(String dicCode) {
        List<ZcjxResDTO> userList = new ArrayList<>();
        //中车检修班30
        if(CommonConstants.THIRTY_STRING.equals(dicCode)){
            userList = orgMajorMapper.queryJXWorker(CommonConstants.ZC_JX);
        //售后服务站20
        }else{
            userList = orgMajorMapper.querySHWorker( CommonConstants.ZC_JX,CommonConstants.ZC_SHFW);
        }
        return userList;
    }
}
