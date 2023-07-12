package com.wzmtr.eam.soft.mdm.empjobinfoquery.vo;

import java.io.Serializable;

public class ErrorMessage implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -8576362539061337123L;

	private String errorCode;

	private String message;

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setErrorCode(String _errorCode) {
		this.errorCode = _errorCode;
	}

	public void setMessage(String _message) {
		this.message = _message;
	}

}
