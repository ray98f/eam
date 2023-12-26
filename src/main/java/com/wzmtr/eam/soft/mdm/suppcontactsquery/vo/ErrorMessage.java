package com.wzmtr.eam.soft.mdm.suppcontactsquery.vo;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 7961040980876634320L;

	private String errorCode;

	private String message;

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
