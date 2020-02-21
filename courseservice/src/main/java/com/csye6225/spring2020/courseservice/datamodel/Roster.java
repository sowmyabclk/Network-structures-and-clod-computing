package com.csye6225.spring2020.courseservice.datamodel;

import java.util.List;

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

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getProfId() {
		return profId;
	}

	public void setProfId(long profId) {
		this.profId = profId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

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
