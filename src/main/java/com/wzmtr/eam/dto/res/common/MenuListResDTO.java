package com.wzmtr.eam.dto.res.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data

public class MenuListResDTO {

    private String id;

    private String parentId;

    private String name;

    private Integer type;

    private String icon;

    private Integer sort;

    private String url;

    private String permCode;

    private String component;

    private Integer status;

    private Integer isShow;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateDate;

    private List<MenuInfo> children;

    @Data
    public static class MenuInfo{

        private String id;

        private String parentId;

        private String name;

        private Integer type;

        private String icon;

        private Integer sort;

        private String url;

        private String permCode;

        private String component;

        private Integer status;

        private Integer isShow;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "GMT+8"
        )
        private Date updateDate;

        private List<ButtonInfo> children;

        @Data
        public static class ButtonInfo{

            private String id;

            private String parentId;

            private String name;

            private Integer type;

            private String icon;

            private Integer sort;

            private String url;

            private String permCode;

            private String component;

            private Integer status;

            private Integer isShow;

            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(
                    pattern = "yyyy-MM-dd HH:mm:ss",
                    timezone = "GMT+8"
            )
            private Date updateDate;
        }
    }
}
