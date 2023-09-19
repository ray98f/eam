package com.wzmtr.eam.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

/**
 * Author: Li.Wang
 * Date: 2023/9/19 19:43
 */
@Data
public class Message {
    /*     */   protected String discoverer;
    /*     */   protected String discoveryTime;
    /*     */   protected String dispatchTime;
    /*     */   protected String dispatchUser;
    /*     */   protected String equipType1;
    /*     */   protected String equipType2;
    /*     */   protected String equipType3;
    /*     */   protected String faultDetail;
    /*     */   protected String faultStatus;
    /*     */   protected String faultType;
    /*     */   protected String lineName;
    /*     */   protected String objectName;
    @XmlElement(name = "oper_type")
    /*     */   protected String operType;
    /*     */   protected String posName;
    /*     */   protected String syscode;
    /*     */   protected String uuid;
    /*     */   protected String workClass;
    /*     */   protected String workNo;
    /*     */   protected String workType;
    /*     */   protected String groupCode;
}
