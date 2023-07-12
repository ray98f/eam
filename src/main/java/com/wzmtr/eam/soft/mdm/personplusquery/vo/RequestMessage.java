/**
 * RequestMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.wzmtr.eam.soft.mdm.personplusquery.vo;

public class RequestMessage  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3057338143192930030L;

	private Message message;

    private String noun;

    private String verb;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getNoun() {
		return noun;
	}

	public void setNoun(String noun) {
		this.noun = noun;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

   

}
