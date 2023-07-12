package com.wzmtr.eam.soft.mdm.personquery.vo;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2702408200886040737L;

	private String syscode;// 系统编码

	private Integer operType;// 操作类型

	private String personId;// 用户Id

	private Integer ifPhoto;// 是否返回照片

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public Integer getIfPhoto() {
		return ifPhoto;
	}

	public void setIfPhoto(Integer ifPhoto) {
		this.ifPhoto = ifPhoto;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

}
