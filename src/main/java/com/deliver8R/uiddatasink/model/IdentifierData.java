package com.deliver8R.uiddatasink.model;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IdentifierData")
public class IdentifierData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1466693813633419066L;

	private String identifier;
	
	private Map<String, String> additionalData;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Map<String, String> getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Map<String, String> additionalData) {
		this.additionalData = additionalData;
	}
	
}
