package com.wzmtr.eam.dto.req.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 常用功能传参类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@Data
@ApiModel
public class CommonFuncReqDTO {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 用户常用功能列表
     */
    @ApiModelProperty(value = "用户常用功能列表")
    private List<UserCommonFunc> userCommonFuncList;

    @Data
    public static class UserCommonFunc {
        /**
         * 常用功能id
         */
        @ApiModelProperty(value = "常用功能id")
        private String permissionId;
        /**
         * 排序
         */
        @ApiModelProperty(value = "排序")
        private String sort;
    }
}
