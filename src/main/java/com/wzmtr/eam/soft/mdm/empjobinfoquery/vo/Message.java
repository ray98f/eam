package com.wzmtr.eam.soft.mdm.empjobinfoquery.vo;

import java.io.Serializable;

public class Message implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 3920212262223415377L;

	private String syscode;// 系统编码

	private Integer operType;// 操作类型

	private String empJobInfoId;// 任职信息ID

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getEmpJobInfoId() {
		return empJobInfoId;
	}

	public void setEmpJobInfoId(String empJobInfoId) {
		this.empJobInfoId = empJobInfoId;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

}
