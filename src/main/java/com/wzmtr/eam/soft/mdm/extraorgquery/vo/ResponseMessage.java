package com.wzmtr.eam.soft.mdm.extraorgquery.vo;

import java.io.Serializable;
import java.util.List;

public class ResponseMessage implements Serializable {

	private static final long serialVersionUID = -2168874414452520348L;

	private String state;

	private String content;

	private List<Result> result;

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	ErrorMessage errorMessage;

	public String getState() {
		return state;
	}

	public String getContent() {
		return content;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}
