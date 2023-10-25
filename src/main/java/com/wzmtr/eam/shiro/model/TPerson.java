package com.wzmtr.eam.shiro.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TPerson implements Serializable {

    private String id;

    private String no;

    private String name;

    private String companyId;

    private String companyName;

    private String companyAreaId;

    private String officeId;

    private String officeName;

    private String officeAreaId;

    private String mobile;

    private String phone;

    private String email;

}
