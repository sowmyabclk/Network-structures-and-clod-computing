package com.csye6225.spring2020.courseserviceDynamo.datamodel;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


@DynamoDBTable(tableName="Students")
public class Student {
	private long studentId;
	private String firstName;
	private String secondName;
	private List<Long> courseEnrolled;
	private long programId;
	private String image;

	public Student() {

	}

	
	public Student(long studentId, String firstName,String secondName,List<Long> courseEnrolled,long programId,String image) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.secondName = secondName;
		this.courseEnrolled = courseEnrolled;
		this.programId = programId;
		this.image = image;

	}

	@DynamoDBHashKey(attributeName="StudentId")
	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
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

	@DynamoDBAttribute(attributeName="Image")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@DynamoDBAttribute(attributeName="Courses")
	public List<Long> getCourseEnrolled() {
		return courseEnrolled;
	}

	public void setCourseEnrolled(List<Long> courseEnrolled) {
		this.courseEnrolled = courseEnrolled;
	}

	@DynamoDBAttribute(attributeName="ProgramId")
	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", FirstName=" + firstName + ", SecondName=" + secondName + ", courseEnrolled=" + courseEnrolled + ", programId=" + programId + ", Image=" + image + "]";
	}

}
