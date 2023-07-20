package com.wzmtr.eam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan("com.wzmtr.eam.**.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
public class EamApplication {

    public static void main(String[] args) {
        SpringApplication.run(EamApplication.class, args);
    }

}
