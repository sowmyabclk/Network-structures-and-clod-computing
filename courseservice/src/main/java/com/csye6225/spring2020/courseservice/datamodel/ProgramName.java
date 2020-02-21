package com.csye6225.spring2020.courseservice.datamodel;

import java.util.List;

public class ProgramName {
	long programId;
	String programName;
	List<Long> courses;
	String duration;

	public ProgramName() {

	}

	public ProgramName(long programId, String programName, List<Long> courses, String duration) {
		this.setProgramName(programName);
		this.setProgramId(programId);
		this.setCourses(courses);
		this.setDuration(duration);
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}

	public List<Long> getCourses() {
		return courses;
	}

	public void setCourses(List<Long> courses) {
		this.courses = courses;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
