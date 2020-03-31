package com.csye6225.spring2020.courseserviceDynamo.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

	private String errorMessage;
	private int errorCode;
	
	public ErrorMessage() {
		
	}
	public ErrorMessage(String errorMessage,int errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
