package com.wzmtr.eam.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/3 11:34
 */
@Data
public class CurrentLoginUser implements Serializable {

    private String personId;

    private String personNo;

    private String personName;

    private String companyId;

    private String officeId;

    private String email;

    private String mobile;

    private String phone;


    public CurrentLoginUser() {
    }

    public CurrentLoginUser(String personId,
                            String personNo,
                            String personName,
                            String companyId,
                            String officeId, String email, String mobile, String phone) {
        this.personId = personId;
        this.personNo = personNo;
        this.personName = personName;
        this.companyId = companyId;
        this.officeId = officeId;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
    }

    public CurrentLoginUser(String personId,
                            String personNo,
                            String personName,
                            String companyId, String officeId) {
        this.personId = personId;
        this.personNo = personNo;
        this.personName = personName;
        this.companyId = companyId;
        this.officeId = officeId;
    }
}
