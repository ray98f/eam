/**
 * ResponseMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.wzmtr.eam.soft.mdm.personplusquery.vo;

import java.util.List;

public class ResponseMessage  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8604500171395988058L;

	private String content;

    private ErrorMessage errorMessage;

    private List<Result> result;

    private String state;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

   

}
