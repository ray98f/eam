package com.wzmtr.eam.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author lize
 * @Date 2023/8/22
 */
@Component
public class BpmnConstants {
    @Value("${bpmn.account}")
    private String bpmnAccount;
    public static String iBpmnAccount;
    @Value("${bpmn.password}")
    private String bpmnPassword;
    public static String iBpmnPassword;
    @Value("${eip.url}")
    private String eipUrl;
    public static String iEipUrl;
    @Value("${bpmn.url}")
    private String bpmnUrl;
    public static String iBpmnUrl;

    @PostConstruct
    private void init() {
        iBpmnAccount = bpmnAccount;
        iBpmnPassword = bpmnPassword;
        iEipUrl = eipUrl;
        iBpmnUrl = bpmnUrl;
    }
}
