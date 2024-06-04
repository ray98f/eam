package com.wzmtr.eam.enums;

/**
 * 异常枚举类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/26
 */
public enum ErrorCode {

    /**
     * 普通异常
     */
    NORMAL_ERROR(99999999, "normal.error"),
    /**
     * Authorization校验失败
     */
    AUTHORIZATION_CHECK_FAIL(401, "authorization.check.fail"),
    /**
     * Authorization过期
     */
    AUTHORIZATION_IS_OVERDUE(401, "authorization.is.overdue"),
    /**
     * Authorization失效
     */
    AUTHORIZATION_INVALID(401, "authorization.invalid"),
    /**
     * Authorization为空
     */
    AUTHORIZATION_EMPTY(401, "authorization.empty"),
    /**
     * 该用户无相关资源操作权限
     */
    RESOURCE_AUTH_FAIL(32000000, "resource.authority.error"),
    /**
     * 参数错误
     */
    PARAM_ERROR(32000001, "param.error"),
    /**
     * 参数为空
     */
    PARAM_NULL(32000002, "param.null"),
    /**
     * 参数超过范围
     */
    PARAM_OUT_OF_RANGE(32000003, "param.range.error"),
    /**
     * 错误的枚举值
     */
    ENUM_VALUE_ERROR(32000004, "enum.value.error"),
    /**
     * 字段不符合要求，仅限中英文字母、数字、中划线和下划线，且长度在4-32之间
     */
    PARAM_PATTERN_INCOMPATIBLE(32000005, "param.pattern.incompatible"),
    /**
     * 参数不能为空
     */
    PARAM_NULL_ERROR(32000006, "param.null.error"),
    /**
     * 资源配置初始化失败
     */
    RESOURCE_INIT_ERROR(32000007, "resource.init.error"),
    /**
     * 参数小于最小值
     */
    PARAM_MIN_ERROR(32000008, "param.min"),
    /**
     * 参数大于最大值
     */
    PARAM_MAX_ERROR(32100009, "param.max"),
    /**
     * 数据已存在
     */
    DATA_EXIST(32000010, "data.exist"),
    /**
     * 数据库新增失败
     */
    INSERT_ERROR(31000011, "insert.error"),
    /**
     * 数据库搜索失败
     */
    SELECT_ERROR(31000012, "select.error"),
    /**
     * 数据库搜索为空
     */
    SELECT_EMPTY(31000013, "select.empty"),
    /**
     * 数据库修改失败
     */
    UPDATE_ERROR(31000014, "update.error"),
    /**
     * 数据库删除失败
     */
    DELETE_ERROR(31000015, "delete.error"),
    /**
     * 参数不在枚举范围中
     */
    NOT_IN_ENUM(32000018, "not.in.enum"),
    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXIST(32000019, "resource.not.exist"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(32000021, "user.not.exist"),
    /**
     * OpenApi签名校验
     */
    OPENAPI_VERIFY_FAIL(32000026, "openapi.verify.fail"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(32100029, "file.upload.error"),
    /**
     * 文件大小应小于20MB
     */
    FILE_BIG(32100030, "file.big"),
    /**
     * 文件删除失败
     */
    FILE_DELETE_ERROR(32100031, "file.delete.error"),
    /**
     * 数据导入失败
     */
    IMPORT_ERROR(32100032, "import.error"),
    /**
     * 移交单 {0}：数量必须为整数
     */
    TRANSFER_QUANTITY_ERROR(32100033, "transfer.quantity.error"),
    /**
     * 移交单 {0}：为已处理状态，不能重复生成
     */
    TRANSFER_HAS_HANDLE(32100034, "transfer.has.handle"),
    /**
     * 只有编辑和驳回状态的数据才能够进行修改
     */
    TRANSFER_SPLIT_ERROR(32100035, "transfer.split.error"),
    /**
     * 选择行数据进行操作
     */
    SELECT_NOTHING(32000036, "select.nothing"),
    /**
     * 有标红*必填项没有填写
     */
    REQUIRED_NULL(32000037, "required.null"),
    /**
     * 请输入正确的设备编码
     */
    EQUIP_CODE_ERROR(32000038, "equip.code.error"),
    /**
     * 当前操作人非记录创建者
     */
    CREATOR_USER_ERROR(32000039, "creator.user.error"),
    /**
     * 非编辑状态不可修改
     */
    CAN_NOT_MODIFY(32000040, "can.not.modify"),
    /**
     * 该计划已存在明细
     */
    PLAN_HAS_DETAIL(32000041, "plan.has.detail"),
    /**
     * 检测有效期必须大于检测日期
     */
    VERIFY_DATE_ERROR(32000042, "verify.date.error"),
    /**
     * 只能编辑本专业数据
     */
    ONLY_OWN_SUBJECT(32000043, "only.own.subject"),
    /**
     * 数据被使用
     */
    RESOURCE_USE(32000046, "resource.use"),
    /**
     * 审核通过状态无法再次通过
     */
    EXAMINE_DONE(32000047, "examine.done"),
    /**
     * 编辑状态无法直接通过
     */
    EXAMINE_NOT_DONE(32000048, "examine.not.done"),
    /**
     * 非送审状态下不可驳回
     */
    REJECT_ERROR(32000049, "reject.error"),
    /**
     * 流程引擎错误
     */
    BPMN_ERROR(32000050, "bpmn.error"),
    /**
     * 无权审核
     */
    NOT_REVIEWER(32000051, "not.reviewer"),
    /**
     * 今日列车 {0} 里程及能耗已存在
     */
    TRAIN_MILE_DAILY_EXIST(32000052, "train.mile.daily.exist"),
    /**
     * 导出失败
     */
    EXPORT_ERROR(32100053, "export.error"),
    /**
     * 开放接口故障接收错误
     */
    FAULT_OPEN_ERROR(32500001, "fault.open.error"),
    /**
     * 开放接口故障接收鉴权错误
     */
    FAULT_OPEN_TOKEN_ERROR(32500000, "fault.open.token.error");

    private final Integer code;

    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String messageOf(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code.equals(code)) {
                return errorCode.message;
            }
        }
        return "";
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
