package com.wzmtr.eam.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 基础常量
 * @author  Ray
 * @version 1.0
 * @date 2023/11/31
 */
public class CommonConstants {

    /**
     * Jwt相关
     */
    public static final String OPEN_URL = "/open";
    public static final String SWAGGER_URL = "swagger";
    public static final String MDM_SYNC_URL = "mdmSync";
    public static final String AUTHORIZATION = "Authorization";
    public static final String JSESSIONID_FIX = ";jsessionid";
    public static final String FILTER_ERROR= "filter.error";
    public static final String ERROR_EXTHROW = "/error/exthrow";

    public static final String S1 = "S1";

    public static final String S2 = "S2";

    /**
     * id
     */
    public static final String ID = "id";

    public static final String WSDL_SUCCESS = "0";

    public static final String EMPTY = "";
    public static final String BLANK = " ";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String UNDERLINE = "_";
    public static final String SHORT_BAR = "-";
    public static final String SINGLE_QUOTATION_MARK = "'";
    public static final String DOUBLE_QUOTATION_MARKS = "\"";
    public static final String LOCALHOST = "localhost";
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

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

    // ---------------------- 权限相关 ----------------------
    // 查看全专业角色
    public static final String SYS_ALL_01 = "SYS_ALL_01";
    // 系统管理员
    public static final String ADMIN = "admin";
    public static final String DM = "DM";
    //运维管理部-专业工程师
    public static final String DM_004 = "DM_004";
    //车辆部-专业工程师
    public static final String DM_005 = "DM_005";
    //中铁通-专业工程师
    public static final String DM_006 = "DM_006";
    //中铁通-生产调度
    public static final String DM_007 = "DM_007";
    //中车-工班长
    public static final String DM_012 = "DM_012";
    public static final String DM_013 = "DM_013";
    //中车-专业工程师
    public static final String DM_032 = "DM_032";
    //设备工程师
    public static final String DM_037 = "DM_037";
    //工程车工程师
    public static final String DM_045 = "DM_045";
    //中车-生产调度
    public static final String DM_048 = "DM_048";
    //中铁通-工班长
    public static final String DM_051 = "DM_051";
    //OCC调度
    public static final String DM_052 = "DM_052";
    //中车-检调
    public static final String ZCJD = "ZCJD";
    //中车-售后服务站
    public static final String ZC_SHFW = "ZC_SHFW";
    //中车-检修班ID
    public static final String ZC_JX = "100000044";
    //中车
    public static final String ZC = "温州中车四方轨道车辆有限公司";
    //中铁通
    public static final String ZTT = "中铁通轨道运营有限公司";

    /**
     * 代办/待阅
     */
    public static final String TODO_GENERAL_SURVEY =  "收到列车号为:%s、完成时间为:%s的普车技改台账，请及时查看";
    public static final String TODO_GD_TPL =  "收到工单编号为:%s的%s工单，请及时办理";
    public static final String TODO_GD_OCC =  "收到工单编号为:%s的%s工单，请及时查看";

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
     * 编号数字
     */
    public static final String ZERO_ONE_STRING = "01";
    public static final String ZERO_TWO_STRING = "02";
    public static final String ZERO_THREE_STRING = "03";
    public static final String ZERO_FOUR_STRING = "04";

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
    public static final String THIRTY_FIVE_STRING = "35";
    public static final String FORTY_STRING = "40";
    public static final String FIFTY_STRING = "50";
    public static final String NINETY_STRING = "90";

    /**
     * 数字
     */
    public static final int ZERO = 0;
    public static final long ZERO_LONG = 0L;
    public static final int ONE = 1;
    public static final int NEGATIVE_ONE = -1;
    public static final int TWO = 2;
    public static final int NEGATIVE_TWO = -2;
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

    public static final int ONE_THOUSAND = 1000;

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
    public static final String DM_STATION2 = "dm.station2";
    public static final String DM_CONTEXT_PATH = "dm.contextPath";
    public static final String DM_IMPORT_TEMPLATE = "dm.import.template";
    public static final String AT_STATION_POS2 = "at.station.pos2";
    public static final String DM_EQUIP_CATEGORY_SUBCLASS = "dm.equip.category.subclass";
    public static final String DM_ER_REC_STATUS = "dm.er.recStatus";

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
    public static final List<String> ZC_LIST = Arrays.asList("06", "07");
    public static final String INIT_EQUIPMENT_CODE = "920000000001";

    /**
     * 系统分隔符
     */
    public static String SYSTEM_SEPARATOR = "/";

    /**
     * 获取项目根目录
     */
    public static String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", SYSTEM_SEPARATOR);

    /**
     * 临时文件相关
     */
    public final static String DEFAULT_FOLDER_TMP = PROJECT_ROOT_DIRECTORY + "/tmp";
    public final static String DEFAULT_FOLDER_TMP_GENERATE = PROJECT_ROOT_DIRECTORY + "/tmp-generate";
    public static final String FAULT_NO_PREFIX = "GZ";
    public static final String FAULT_WORK_NO_PREFIX = "GD";
    public static final String FAULT_NO = "fault_no";
    public static final String FAULT_WORK_NO = "fault_work_no";

}
