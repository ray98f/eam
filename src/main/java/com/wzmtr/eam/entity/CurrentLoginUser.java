package com.wzmtr.eam.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CurrentLoginUser implements Serializable {

    private String personId;

    private String personNo;

    private String personName;

    private String companyId;

    private String companyName;

    private String officeId;

    private String officeName;

    private String email;

    private String mobile;

    private String phone;

    private String areaId;
}
