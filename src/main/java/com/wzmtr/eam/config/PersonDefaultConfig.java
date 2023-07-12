package com.wzmtr.eam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "user.default")
public class PersonDefaultConfig {

    private  String password;
    private  String userType;
    private  String loginFlag;
    private  String createBy;
    private  String updateBy;
}
