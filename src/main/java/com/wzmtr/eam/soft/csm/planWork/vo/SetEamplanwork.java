package com.wzmtr.eam.soft.csm.planWork.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
 
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setEamplanwork", propOrder = {"arg0"})
@Data
public class SetEamplanwork {
    RequestMessage arg0;
}