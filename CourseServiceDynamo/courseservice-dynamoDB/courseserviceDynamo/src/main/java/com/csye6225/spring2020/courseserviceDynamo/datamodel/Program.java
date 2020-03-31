package com.csye6225.spring2020.courseserviceDynamo.datamodel;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Programs")
public class Program {
	long programId;
	String programName;
	List<Long> course;
	String duration;

	
	public Program() {

	}

	public Program(long programId, String programName, List<Long> course,String duration) {
		this.setProgramName(programName);
		this.setProgramId(programId);
		this.setCourses(course);
		this.setDuration(duration);
	}

	@DynamoDBAttribute(attributeName="ProgramName")
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	@DynamoDBHashKey(attributeName="ProgramId")
	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}

	@DynamoDBAttribute(attributeName="Course")
	public List<Long> getCourses() {
		return course;
	}

	public void setCourses(List<Long> course) {
		this.course = course;
	}

	@DynamoDBAttribute(attributeName="Duration")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
