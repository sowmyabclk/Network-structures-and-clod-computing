package com.csye6225.spring2020.courseserviceDynamo.datamodel;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Roster")
public class Roster {

	long courseId;
	long profId;
	String day;
	String time;

	public Roster() {

	}

	public Roster(long courseId, long profId, String day, String time) {
		super();
		this.courseId = courseId;
		this.profId = profId;
		this.day = day;
		this.time = time;
	}

	@DynamoDBHashKey(attributeName="CourseId")
	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	@DynamoDBAttribute(attributeName="ProfId")
	public long getProfId() {
		return profId;
	}

	public void setProfId(long profId) {
		this.profId = profId;
	}

	@DynamoDBAttribute(attributeName="Day")
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	@DynamoDBAttribute(attributeName="Time")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Roster [courseId=" + courseId + ", profId=" + profId + ", day=" + day + ", time=" + time + "]";
	}

}
