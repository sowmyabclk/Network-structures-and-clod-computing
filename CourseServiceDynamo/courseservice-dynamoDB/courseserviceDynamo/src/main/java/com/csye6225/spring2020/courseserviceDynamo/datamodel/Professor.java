package com.csye6225.spring2020.courseserviceDynamo.datamodel;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Professors")
public class Professor {
	private long profId;
	private long courseId;
	private String joiningDate;
	private String firstName;
	private String secondName;
	private long programId;

	public Professor() {

	}

	public Professor(long profId, long courseId, String joiningDate, String firstName,String secondName,long programId) {
		System.out.println("here");
		this.profId = profId;
		this.courseId = courseId;
		this.joiningDate = joiningDate;
		this.firstName = firstName;
		this.secondName = secondName;
		this.programId = programId;
	}

	@DynamoDBAttribute(attributeName="FirstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@DynamoDBAttribute(attributeName="SecondName")
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	@DynamoDBHashKey(attributeName="ProfId")
	public long getProfId() {
		return profId;
	}

	public void setProfId(long profId) {
		this.profId = profId;
	}
	
	@DynamoDBAttribute(attributeName="JoiningDate")
	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	@DynamoDBAttribute(attributeName="ProgramId")
	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}

	@DynamoDBAttribute(attributeName="CourseId")
	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

}
