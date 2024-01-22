package com.wzmtr.eam.utils.bpmn;

import com.wzmtr.eam.constant.BpmnConstants;

/**
 * @Author lize
 * @Date 2023/3/28
 */
public class FastFlowPathUrl {
    public static final String BASE_URL = BpmnConstants.BPMN_URL;
    public static final String REGISTER = BASE_URL + "/uc/api/user/v2/save";
    public static final String LOGIN = BASE_URL + "/sys/getJwt";
    public static final String INSTANCE_START = BASE_URL + "/a1bpmn/api/runtime/instance/v2/start";
    public static final String INSTANCE_AGREE = BASE_URL + "/a1bpmn/api/runtime/task/v2/complete";
    public static final String INSTANCE_REJECT = BASE_URL + "/a1bpmn/api/history/task/v1/invalid/";
    public static final String USER_ADD = BASE_URL + "/uc/api/user/v2/save";
    public static final String PERSON_LIST = BASE_URL + "/uc/api/user/page/list";
    /**
     * 获取流程列表
     */
    public static final String FLOW_LIST = BASE_URL + "/a1bpmn/api/models/listJson";
    /**
     * 审批意见
     */
    public static final String EXAMINE_OPINION = BASE_URL + "/a1bpmn/api/runtime/hisInstance/v1/nodeOpinion";
    /**
     * 获取下一个taskKey
     */
    public static final String NEXT_TASK_KEY = BASE_URL + "/a1bpmn/api/cockpit/process-instance/getNextNode/";
    /**
     * 获取最新的taskId
     */
    public static final String QUERY_TASKID_BY_PROCID = BASE_URL + "/a1bpmn/api/cockpit/runtime/queryTaskIdByProcId";
    /**
     * 获取模型信息
     */
    public static final String QUERY_MODEL_INFO = BASE_URL + "/a1bpmn/api/model/get/";
    /**
     * 新增维度
     */
    public static final String DEMENSION_ADD = BASE_URL + "/uc/api/demension/save";
    /**
     * 新增组织
     */
    public static final String ORG_ADD = BASE_URL + "/uc/api/org/save";
    /**
     * 获取tenantId的所有组织
     */
    public static final String GET_ORG_BY_PARENT_ID = BASE_URL + "/uc/api/org/getOrgByParentId";
    /**
     * 新增岗位
     */
    public static final String POST_ADD = BASE_URL + "/uc/api/orgPost/save";
    /**
     * 退出
     */
    public static final String LOGOUT = BASE_URL + "/logout";
    /**
     * 待办
     */
    public static final String EXAMINE_LIST = BASE_URL + "/a1bpmn/api/runtime/task/v2/list";
    /**
     * 已办
     */
    public static final String EXAMINED_LIST = BASE_URL + "/a1bpmn/api/history/task/v1/list";
    /**
     * 已发
     */
    public static final String INSTANCE_RUNNING_LIST = BASE_URL + "/a1bpmn/api/startUp/list/running";
    /**
     * 办结
     */
    public static final String INSTANCE_ENDING_LIST = BASE_URL + "/a1bpmn/api/startUp/his/list";
    /**
     * 获得表单参数
     */
    public static final String RENDER_HIS_FORM = BASE_URL + "/form/bus/render/get/renderHisForm/";
    /**
     * 用taskId获取流程的key
     */
    public static final String GET_DEF_KEY = BASE_URL + "/a1bpmn/api/runtime/getTaskBtn/";

}