package com.wzmtr.eam.enums;

public enum ErrorCode {

    NORMAL_ERROR(99999999, "normal.error"),

    /**
     * 鉴权
     */
    AUTHORIZATION_CHECK_FAIL(401, "authorization.check.fail"),
    AUTHORIZATION_IS_OVERDUE(401, "authorization.is.overdue"),
    AUTHORIZATION_INVALID(401, "authorization.invalid"),
    AUTHORIZATION_EMPTY(401, "authorization.empty"),
    /**
     * 该用户无相关资源操作权限
     */
    RESOURCE_AUTH_FAIL(32000000, "resource.authority.error"),

    /**
     * 参数错误
     */
    PARAM_ERROR(32000001, "param.error"),

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

    PARAM_MAX_ERROR(32100009, "param.max"),

    DATA_EXIST(32000010, "data.exist"),

    INSERT_ERROR(31000011, "insert.error"),

    SELECT_ERROR(31000012, "select.error"),

    SELECT_EMPTY(31000013, "select.empty"),

    UPDATE_ERROR(31000014, "update.error"),

    DELETE_ERROR(31000015, "delete.error"),

    SIGN_ERROR(31000016, "signTime.error"),
    CLOCK_ERROR(31000017, "clock.error"),

    /**
     * 参数不在枚举范围中
     */
    NOT_IN_ENUM(32000018, "not.in.enum"),

    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXIST(32000019, "resource.not.exist"),

    /**
     * 鉴权 32000060 - 32000079
     */
    USER_EXIST(32000020, "user.exist"),
    USER_NOT_EXIST(32000021, "user.not.exist"),
    USER_DISABLE(32000022, "user.disable"),
    OLD_PASSWORD_ERROR(32000023, "old.password.error"),
    PASSWORD_SAME(32000024, "password.same"),
    PASSWORD_ERROR(32000025, "password.error"),

    /**
     * OpenApi签名校验
     */
    OPENAPI_VERIFY_FAIL(32000026, "openapi.verify.fail"),

    /**
     * 字典
     */
    DIC_TYPE_ALREADY_EXIST(32000027, "dic.type.already.exist"),
    DIC_TYPE_NOT_EXIST(32000028, "dic.type.not.exist"),
    /**
     * 其他
     */
    FILE_UPLOAD_ERROR(32100029, "file.upload.error"),
    FILE_BIG(32100030, "file.big"),
    FILE_DELETE_ERROR(32100031, "file.delete.error"),

    IMPORT_ERROR(32100032, "import.error"),

    TRANSFER_QUANTITY_ERROR(32100033, "transfer.quantity.error"),
    TRANSFER_HAS_HANDLE(32100034, "transfer.has.handle"),
    TRANSFER_SPLIT_ERROR(32100035, "transfer.split.error"),
    SELECT_NOTHING(32000036, "select.nothing"),
    REQUIRED_NULL(32000037, "required.null"),
    EQUIP_CODE_ERROR(32000038, "equip.code.error"),
    CREATOR_USER_ERROR(32000039, "creator.user.error"),
    CAN_NOT_MODIFY(32000040, "can.not.modify"),
    PLAN_HAS_DETAIL(32000041, "plan.has.detail"),
    VERIFY_DATE_ERROR(32000042, "verify.date.error"),
    ONLY_OWN_SUBJECT(32000043, "only.own.subject"),
    NO_DETAIL(32000044, "no.detail"),
    ROOT_ERROR(32000045,"root.error"),
    RESOURCE_USE(32000046, "resource.use"),
    EXAMINE_DONE(32000047, "examine.done"),
    EXAMINE_NOT_DONE(32000048, "examine.not.done"),
    REJECT_ERROR(32000049, "reject.error"),
    BPMN_ERROR(32000050, "bpmn.error"),
    NOT_REVIEWER(32000051, "not.reviewer");

    private Integer code;

    private String message;

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
