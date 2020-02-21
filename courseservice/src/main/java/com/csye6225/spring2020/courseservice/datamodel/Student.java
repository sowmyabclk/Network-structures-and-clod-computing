package com.csye6225.spring2020.courseservice.datamodel;

import java.util.List;

public class Student {
	private long studentId;
	private String firstName;
	private String lastName;
	private String img;
	private List<Long> courseEnrolled;
	private long programId;

	public Student() {

	}

	public Student(long studentId, String firstName, String lastName, String img, List<Long> courseEnrolled,
			long programId) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.img = img;
		this.courseEnrolled = courseEnrolled;
		this.programId = programId;

	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<Long> getCourseEnrolled() {
		return courseEnrolled;
	}

	public void setCourseEnrolled(List<Long> courseEnrolled) {
		this.courseEnrolled = courseEnrolled;
	}

	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", firstName=" + firstName + ", lastName=" + lastName + ", img="
				+ img + ", courseEnrolled=" + courseEnrolled + ", programId=" + programId + "]";
	}

}
