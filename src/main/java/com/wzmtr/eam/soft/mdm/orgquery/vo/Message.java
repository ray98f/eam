package com.wzmtr.eam.soft.mdm.orgquery.vo;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -6936988529248640521L;

	/**
	 * 系统编码
	 */
	private String syscode;

	/**
	 * 操作类型
	 */
	private Integer operType;

	/**
	 * 组织机构ID
	 */
	private String orgId;

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

}
