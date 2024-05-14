package com.wzmtr.eam.constant;

public class CommonConstants {

    /**
     * id
     */
    public static final String ID = "id";

    public static final String WSDL_SUCCESS = "0";

    public static final String EMPTY = "";
    public static final String BLANK = " ";
    public static final String COMMA = ",";
    public static final String UNDERLINE = "_";
    public static final String SINGLE_QUOTATION_MARK = "'";
    public static final String DOUBLE_QUOTATION_MARKS = "\"";

    public static final String DAY = "yyyy-MM-dd";
    public static final String TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String XLS = "xls";

    public static final String XLSX = "xlsx";

    public static final String UNKNOWN = "unknown";

    public static final String ROOT = "root";
    /**
     * 开启
     */
    public static final String ON = "on";
    /**
     * 关闭
     */
    public static final String OFF = "off";
    public static final String DEF_PWD = "wzmtr@123456";
    /**
     * 权限
     */

    public static final String SYS_ALL_01 = "SYS_ALL_01";  //查看全专业角色
    public static final String ADMIN = "admin";  //系统管理员
    public static final String DM_004 = "DM_004";//运维管理部-专业工程师
    public static final String DM_006 = "DM_006";//中铁通-专业工程师
    public static final String DM_007 = "DM_007";//中铁通-生产调度
    public static final String DM_012 = "DM_012";//中车-工班长
    public static final String DM_013 = "DM_013";
    public static final String DM_051 = "DM_051";//中铁通-工班长
    public static final String DM_037 = "DM_048";//中车-生产调度
    public static final String DM_045 = "DM_045";//工程车工程师
    public static final String ZCJD = "ZCJD";//中车-检调
    public static final String DM_048 = "DM_048";//中车-生产调度
    public static final String ZC_SHFW = "ZC_SHFW";//中车-售后服务站
    public static final String ZC_JX = "100000044";//中车-检修班ID
    public static final String ZC = "温州中车四方轨道车辆有限公司";//中车
    public static final String ZTT = "中铁通轨道运营有限公司";//中铁通

    /**
     * 代办/待阅
     */
    public static final String TODO_GENERAL_SURVEY =  "收到列车号为:%s、完成时间为:%s的普车技改台账，请及时查看";
    public static final String TODO_GD_TPL =  "收到工单编号为:%s的%s工单，请及时办理";


    public static final String EQUIPMENT_ROOM_CODE_0 = "R100000";

    /**
     * 故障分析流程--部长审核节点
     */
    public static final String FAULT_ANALIZE_REVIEW_NODE = "UserTask_0zkqpyn";
    /**
     * 故障跟踪流程--部长审核节点
     */
    public static final String FAULT_TRACK_REVIEW_NODE = "UserTask_1ftz952";

    /**
     * 字符串数字
     */
    public static final String ZERO_STRING = "0";
    public static final String ONE_STRING = "1";
    public static final String TWO_STRING = "2";
    public static final String THREE_STRING = "3";
    public static final String FOUR_STRING = "4";
    public static final String FIVE_STRING = "5";
    public static final String SIX_STRING = "6";
    public static final String SEVEN_STRING = "7";
    public static final String EIGHT_STRING = "8";
    public static final String NINE_STRING = "9";
    public static final String TEN_STRING = "10";
    public static final String FOURTEEN_STRING = "14";
    public static final String FIFTEEN_STRING = "15";
    public static final String TWENTY_STRING = "20";
    public static final String THIRTY_STRING = "30";
    public static final String FORTY_STRING = "40";
    public static final String FIFTY_STRING = "50";
    public static final String NINETY_STRING = "90";

    /**
     * 数字
     */
    public static final int ZERO = 0;
    public static final long ZERO_LONG = 0L;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int FOURTEEN = 14;
    public static final int TWENTY = 20;
    public static final int THIRTY = 30;

    public static final String MESSAGE = "message";

    /**
     * 流程相关
     */
    public static final String CODE = "code";
    public static final String PROCESS_ERROR_CODE = "-1";

    public static final String CAR_DEVICE_SUBJECT_CODE = "06";
    public static final String CAR_SUBJECT_CODE = "07";

    /**
     * 工班类别
     */
    public static final String FIRST_REPAIR_SHIFT = "一级修";
    public static final String SECOND_REPAIR_SHIFT = "二级修";

    /**
     * 线路编号
     */
    public static final String LINE_CODE_ONE = "01";
    public static final String LINE_CODE_TWO = "02";

    /**
     * 设备分类编号
     */
    public static final String EQUIP_CATE_ENGINEER_CAR_CODE = "17";

    /**
     * 字典字段code
     */
    public static final String DM_VEHICLE_SPECIALTY_CODE = "dm.vehicleSpecialty";
    public static final String DM_MATCH_CONTROL_CODE = "dm.matchControl";

    public static final String DM_FAULT_FOLLOW_STATUS = "dm.faultFollowStatus";

    /**
     * 工单推送内容相关
     */
    public static final String FAULT_CONTENT_BEGIN = "【市铁投集团】工单号：";
    public static final String FAULT_CONTENT_END = "】故障管理流程";
    public static final String FAULT_FINISHED_CONFIRM_CN = "故障完工确认";
    public static final String FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN = "工单完工确认并故障再派工";
    public static final String FAULT_TUNING_CONFIRM_CN = "故障设调确认";

    public static final String ERROR = "异常";

    public static final String FAULT_OPEN_APP_KEY = "ubcrjTM9BE1F79Cc";

    public static final String FAULT_FOLLOW_REPORT = "fault_follow_report";

    public static final String PASSENGER_TRANSPORT_DEPT = "运营分公司-客运部";

}
