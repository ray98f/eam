package com.wzmtr.eam.soft.mdm.suppcontactsquery.vo;

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
	 * 明细行ID
	 */
	private String perId;

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getPerId() {
		return perId;
	}

	public void setPerId(String perId) {
		this.perId = perId;
	}

}
