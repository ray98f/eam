package com.wzmtr.eam.soft.mdm.supplierquery.vo;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -8949188345468702336L;

	/**
	 * 系统编码
	 */
	private String syscode;

	/**
	 * 操作类型
	 */
	private Integer operType;

	/**
	 * 供应商编码
	 */
	private String suppCode;

	/**
	 * 申请编码
	 */
	private String applyCode;

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}

	
}
