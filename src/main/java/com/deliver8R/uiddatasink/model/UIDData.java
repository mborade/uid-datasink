package com.deliver8R.uiddatasink.model;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UIDData")
public class UIDData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1466693813633419066L;

	private String uid;
	
	private Map<String, String> keyValueMap;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Map<String, String> getKeyValueMap() {
		return keyValueMap;
	}

	public void setKeyValueMap(Map<String, String> info) {
		this.keyValueMap = info;
	}
	
}
