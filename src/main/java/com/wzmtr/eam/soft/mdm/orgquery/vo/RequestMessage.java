package com.wzmtr.eam.soft.mdm.orgquery.vo;

import java.io.Serializable;

public class RequestMessage implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6302514848783912522L;

	private String verb;

	private String noun;

	private Message message;

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getNoun() {
		return noun;
	}

	public void setNoun(String noun) {
		this.noun = noun;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
