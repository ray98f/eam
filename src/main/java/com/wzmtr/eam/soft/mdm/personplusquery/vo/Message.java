/**
 * Message.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.wzmtr.eam.soft.mdm.personplusquery.vo;

public class Message  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6437454886698196164L;

	private Integer operType;

    private String personId;

    private String syscode;

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

  

}
