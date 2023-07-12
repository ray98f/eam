package com.wzmtr.eam.soft.mdm.suppcontactsquery.vo;

import java.io.Serializable;

/**
 * 外部单位人员
 * 
 * @author wqf
 *
 */

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113245899433887661L;

	// 明细行ID
	private String perId;

	// 供应商ID
	private String suppId;

	// 代码
	private String perCode;

	// 姓名
	private String perName;

	// 职务
	private String duty;

	// 电话
	private String phone;

	// 邮件
	private String mail;
	
	// 
	private String extraOrg;
	
	private String isUse;

	public String getPerId() {
		return perId;
	}

	public void setPerId(String perId) {
		this.perId = perId;
	}

	public String getSuppId() {
		return suppId;
	}

	public void setSuppId(String suppId) {
		this.suppId = suppId;
	}

	public String getPerCode() {
		return perCode;
	}

	public void setPerCode(String perCode) {
		this.perCode = perCode;
	}

	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getExtraOrg() {
		return extraOrg;
	}

	public void setExtraOrg(String extraOrg) {
		this.extraOrg = extraOrg;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	
}
